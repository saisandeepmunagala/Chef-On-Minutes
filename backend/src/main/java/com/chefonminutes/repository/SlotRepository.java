package com.chefonminutes.repository;

import com.chefonminutes.model.Slot;
import com.chefonminutes.model.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByChefProfileIdAndDateAndStatus(Long chefProfileId, LocalDate date, SlotStatus status);
    List<Slot> findByChefProfileId(Long chefProfileId);
}
