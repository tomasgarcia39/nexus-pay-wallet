package com.nexuspay.wallet.repository;

import com.nexuspay.wallet.entity.Reservation;
import com.nexuspay.wallet.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Trae todas las reservas de una cuenta
    List<Reservation> findByAccountId(Long accountId);

    // Trae solo las reservas con un estado específico (ej: solo las PENDING)
    List<Reservation> findByAccountIdAndStatus(Long accountId, ReservationStatus status);
}