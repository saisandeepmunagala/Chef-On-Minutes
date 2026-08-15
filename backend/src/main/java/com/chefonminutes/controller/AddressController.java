package com.chefonminutes.controller;

import com.chefonminutes.dto.AddressDTO;
import com.chefonminutes.dto.AddressRequestDTO;
import com.chefonminutes.exception.ResourceNotFoundException;
import com.chefonminutes.model.Address;
import com.chefonminutes.model.User;
import com.chefonminutes.repository.AddressRepository;
import com.chefonminutes.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressRepository.findByUserIdAndActiveTrue(userId).stream().map(this::toDTO).toList());
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Address address = Address.builder()
                .user(user)
                .line1(request.getLine1())
                .line2(request.getLine2())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .label(request.getLabel())
                .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                .build();
        return ResponseEntity.ok(toDTO(addressRepository.save(address)));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> removeAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + addressId));
        address.setActive(false);
        addressRepository.save(address);
        return ResponseEntity.noContent().build();
    }

    private AddressDTO toDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .line1(address.getLine1())
                .line2(address.getLine2())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .label(address.getLabel() != null ? address.getLabel().name() : null)
                .isDefault(address.getIsDefault())
                .build();
    }
}
