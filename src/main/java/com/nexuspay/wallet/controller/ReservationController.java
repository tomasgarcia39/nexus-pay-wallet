package com.nexuspay.wallet.controller;

import com.nexuspay.wallet.dto.ReservationRequestDTO;
import com.nexuspay.wallet.entity.Reservation;
import com.nexuspay.wallet.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequestDTO dto) {
        Reservation reservation = reservationService.createReservation(
                dto.getUserId(),
                dto.getAmount(),
                dto.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

  
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Reservation> confirmReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(reservation);
    }

   
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(reservation);
    }

  
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservations(@PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }
}