package com.nexuspay.wallet.repository;

import com.nexuspay.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA crea la lógica automáticamente basándose en el nombre
    boolean existsByEmail(String email);
 // ... dentro de la interfaz ...
    java.util.Optional<com.nexuspay.wallet.entity.User> findByEmail(String email);
}