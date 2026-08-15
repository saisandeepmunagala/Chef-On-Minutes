package com.chefonminutes.config;

import com.chefonminutes.model.ChefDish;
import com.chefonminutes.model.ChefProfile;
import com.chefonminutes.model.Dish;
import com.chefonminutes.model.Role;
import com.chefonminutes.model.Slot;
import com.chefonminutes.model.SlotStatus;
import com.chefonminutes.model.User;
import com.chefonminutes.repository.ChefDishRepository;
import com.chefonminutes.repository.ChefProfileRepository;
import com.chefonminutes.repository.DishRepository;
import com.chefonminutes.repository.SlotRepository;
import com.chefonminutes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/** Seeds the global dish catalog plus a few sample chefs (menu + open slots) on first boot. */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final ChefProfileRepository chefProfileRepository;
    private final ChefDishRepository chefDishRepository;
    private final SlotRepository slotRepository;
    private final PasswordEncoder passwordEncoder;

    private record DishPrice(Dish dish, double price) {
    }

    @Override
    public void run(String... args) {
        if (dishRepository.count() > 0) {
            return;
        }

        List<Dish> dishes = dishRepository.saveAll(List.of(
                Dish.builder().name("Veg Biryani").description("Basmati rice cooked with mixed vegetables and spices").build(),
                Dish.builder().name("Paneer Butter Masala").description("Cottage cheese in a rich tomato-butter gravy").build(),
                Dish.builder().name("Sambar Rice").description("South Indian lentil stew with rice").build(),
                Dish.builder().name("Chicken Curry").description("Home-style chicken curry").build(),
                Dish.builder().name("Dal Tadka").description("Yellow lentils tempered with spices").build()
        ));

        seedChef("Ramesh Iyer", "ramesh.chef@example.com", "South Indian",
                "10+ years cooking authentic South Indian home meals.",
                List.of(dishPrice(dishes, "Sambar Rice", 180.0), dishPrice(dishes, "Dal Tadka", 120.0)));

        seedChef("Anita Sharma", "anita.chef@example.com", "North Indian",
                "Specializes in rich North Indian curries made fresh daily.",
                List.of(dishPrice(dishes, "Paneer Butter Masala", 220.0), dishPrice(dishes, "Dal Tadka", 130.0)));

        seedChef("Farhan Khan", "farhan.chef@example.com", "Biryani & Non-Veg",
                "Known for slow-cooked biryani and chicken curry.",
                List.of(dishPrice(dishes, "Veg Biryani", 200.0), dishPrice(dishes, "Chicken Curry", 260.0)));
    }

    private DishPrice dishPrice(List<Dish> dishes, String name, double price) {
        Dish dish = dishes.stream().filter(d -> d.getName().equals(name)).findFirst().orElseThrow();
        return new DishPrice(dish, price);
    }

    /** Every seeded chef account uses the password "password123" for local demo/testing. */
    private void seedChef(String name, String email, String specialty, String bio, List<DishPrice> menu) {
        User chefUser = userRepository.save(User.builder()
                .name(name)
                .email(email)
                .phone("9999999999")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(Role.CHEF)
                .build());

        ChefProfile chefProfile = chefProfileRepository.save(ChefProfile.builder()
                .user(chefUser)
                .bio(bio)
                .specialty(specialty)
                .available(true)
                .build());

        menu.forEach(entry -> chefDishRepository.save(ChefDish.builder()
                .chefProfile(chefProfile)
                .dish(entry.dish())
                .pricePerUnit(entry.price())
                .available(true)
                .build()));

        for (int day = 1; day <= 3; day++) {
            slotRepository.save(Slot.builder()
                    .chefProfile(chefProfile)
                    .date(LocalDate.now().plusDays(day))
                    .startTime(LocalTime.of(12, 0))
                    .endTime(LocalTime.of(13, 0))
                    .status(SlotStatus.AVAILABLE)
                    .build());
        }
    }
}

