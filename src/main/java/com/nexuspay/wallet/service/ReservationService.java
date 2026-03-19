package com.nexuspay.wallet.service;

import com.nexuspay.wallet.entity.Account;
import com.nexuspay.wallet.entity.Reservation;
import com.nexuspay.wallet.entity.ReservationStatus;
import com.nexuspay.wallet.exception.InsufficientFundsException;
import com.nexuspay.wallet.exception.ReservationNotFoundException;
import com.nexuspay.wallet.repository.AccountRepository;
import com.nexuspay.wallet.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WalletService walletService;

    /**
     * Crea una reserva de dinero.
     * El monto queda retenido pero no se descuenta del saldo todavía.
     */
    @Transactional
    public Reservation createReservation(Long userId, BigDecimal amount, String description) {
        Account account = walletService.getAccountByUserId(userId);

        BigDecimal availableBalance = account.getBalance()
                .subtract(account.getReservedBalance());

        if (availableBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        account.setReservedBalance(account.getReservedBalance().add(amount));
        accountRepository.save(account);

        Reservation reservation = new Reservation();
        reservation.setAmount(amount);
        reservation.setDescription(description);
        reservation.setAccount(account);

        return reservationRepository.save(reservation);
    }

    /**
     * Confirma una reserva: libera el dinero de vuelta al saldo disponible.
     */
    @Transactional
    public Reservation confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Solo se pueden confirmar reservas en estado PENDING.");
        }

        Account account = reservation.getAccount();

        // Liberamos el saldo reservado — el balance vuelve a estar disponible
        account.setReservedBalance(account.getReservedBalance().subtract(reservation.getAmount()));
        accountRepository.save(account);

        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    /**
     * Cancela una reserva: libera el saldo retenido.
     */
    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Solo se pueden cancelar reservas en estado PENDING.");
        }

        Account account = reservation.getAccount();

        account.setReservedBalance(account.getReservedBalance().subtract(reservation.getAmount()));
        accountRepository.save(account);

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    /**
     * Lista todas las reservas de un usuario.
     */
    public List<Reservation> getReservationsByUser(Long userId) {
        Account account = walletService.getAccountByUserId(userId);
        return reservationRepository.findByAccountId(account.getId());
    }
}