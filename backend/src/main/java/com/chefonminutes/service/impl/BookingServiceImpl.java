package com.chefonminutes.service.impl;

import com.chefonminutes.dto.BookingItemRequestDTO;
import com.chefonminutes.dto.BookingItemResponseDTO;
import com.chefonminutes.dto.BookingRequestDTO;
import com.chefonminutes.dto.BookingResponseDTO;
import com.chefonminutes.event.BookingCancelledEvent;
import com.chefonminutes.event.BookingConfirmedEvent;
import com.chefonminutes.exception.InvalidStateException;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.Address;
import com.chefonminutes.model.Booking;
import com.chefonminutes.model.BookingItem;
import com.chefonminutes.model.BookingStatus;
import com.chefonminutes.model.ChefDish;
import com.chefonminutes.model.Role;
import com.chefonminutes.model.Slot;
import com.chefonminutes.model.User;
import com.chefonminutes.repository.AddressRepository;
import com.chefonminutes.repository.BookingRepository;
import com.chefonminutes.repository.ChefDishRepository;
import com.chefonminutes.repository.PaymentRepository;
import com.chefonminutes.repository.SlotRepository;
import com.chefonminutes.repository.UserRepository;
import com.chefonminutes.service.BookingService;
import com.chefonminutes.service.SlotManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final Map<BookingStatus, EnumSet<BookingStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(BookingStatus.class);
    static {
        ALLOWED_TRANSITIONS.put(BookingStatus.PENDING, EnumSet.of(BookingStatus.CONFIRMED, BookingStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(BookingStatus.CONFIRMED, EnumSet.of(BookingStatus.IN_PROGRESS, BookingStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(BookingStatus.IN_PROGRESS, EnumSet.of(BookingStatus.COMPLETED));
        ALLOWED_TRANSITIONS.put(BookingStatus.COMPLETED, EnumSet.noneOf(BookingStatus.class));
        ALLOWED_TRANSITIONS.put(BookingStatus.CANCELLED, EnumSet.noneOf(BookingStatus.class));
    }

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ChefDishRepository chefDishRepository;
    private final SlotRepository slotRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final SlotManager slotManager;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));
        if (customer.getRole() != Role.CUSTOMER) {
            throw new InvalidStateException("User " + customer.getId() + " is not a customer");
        }
        User chef = userRepository.findById(request.getChefId())
                .orElseThrow(() -> new ResourceNotFoundException("Chef not found: " + request.getChefId()));
        if (chef.getRole() != Role.CHEF) {
            throw new InvalidStateException("User " + chef.getId() + " is not a chef");
        }
        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + request.getSlotId()));

        String addressSnapshot = null;
        if (request.getAddressId() != null) {
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + request.getAddressId()));
            addressSnapshot = address.toSnapshotText();
        }

        Booking booking = Booking.builder()
                .customer(customer)
                .chef(chef)
                .slot(slot)
                .sessionDate(slot.getDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .addressSnapshot(addressSnapshot)
                .status(BookingStatus.PENDING)
                .build();

        double total = 0.0;
        for (BookingItemRequestDTO itemRequest : request.getItems()) {
            ChefDish chefDish = chefDishRepository.findById(itemRequest.getChefDishId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chef dish not found: " + itemRequest.getChefDishId()));
            BookingItem item = BookingItem.builder()
                    .chefDish(chefDish)
                    .dishName(chefDish.getDish().getName())
                    .quantity(itemRequest.getQuantity())
                    .priceAtBooking(chefDish.getPricePerUnit())
                    .build();
            booking.addItem(item);
            total += item.lineTotal();
        }
        booking.setTotalAmount(total);

        // reserve the slot only after the booking object is fully built, so a failure above never leaves a stranded reservation
        slotManager.reserveSlot(slot.getId(), null);
        Booking saved = bookingRepository.save(booking);
        slot.setBookingId(saved.getId());
        slotRepository.save(slot);

        return toDTO(saved);
    }

    @Override
    public List<BookingResponseDTO> getBookingsForCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream().map(this::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsForChef(Long chefId) {
        return bookingRepository.findByChefId(chefId).stream().map(this::toDTO).toList();
    }

    @Override
    public BookingResponseDTO cancelBooking(Long bookingId) {
        Booking booking = transition(bookingId, BookingStatus.CANCELLED);
        Long slotId = booking.getSlot() != null ? booking.getSlot().getId() : null;
        eventPublisher.publishEvent(new BookingCancelledEvent(booking.getId(), slotId));
        return toDTO(booking);
    }

    @Override
    public BookingResponseDTO confirmBooking(Long bookingId) {
        Booking booking = transition(bookingId, BookingStatus.CONFIRMED);
        eventPublisher.publishEvent(new BookingConfirmedEvent(booking.getId()));
        return toDTO(booking);
    }

    @Override
    public BookingResponseDTO startBooking(Long bookingId) {
        return toDTO(transition(bookingId, BookingStatus.IN_PROGRESS));
    }

    @Override
    public BookingResponseDTO completeBooking(Long bookingId) {
        return toDTO(transition(bookingId, BookingStatus.COMPLETED));
    }

    private Booking transition(Long bookingId, BookingStatus target) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        if (!ALLOWED_TRANSITIONS.get(booking.getStatus()).contains(target)) {
            throw new InvalidStateException("Cannot move booking " + bookingId + " from " + booking.getStatus() + " to " + target);
        }
        booking.setStatus(target);
        return bookingRepository.save(booking);
    }

    private BookingResponseDTO toDTO(Booking booking) {
        List<BookingItemResponseDTO> items = booking.getItems().stream()
                .map(item -> BookingItemResponseDTO.builder()
                        .dishName(item.getDishName())
                        .quantity(item.getQuantity())
                        .priceAtBooking(item.getPriceAtBooking())
                        .lineTotal(item.lineTotal())
                        .build())
                .toList();
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .customerName(booking.getCustomer().getName())
                .chefName(booking.getChef().getName())
                .sessionDate(booking.getSessionDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .addressSnapshot(booking.getAddressSnapshot())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .paymentStatus(paymentRepository.findByBookingId(booking.getId())
                        .map(p -> p.getStatus()).orElse(null))
                .items(items)
                .build();
    }
}

