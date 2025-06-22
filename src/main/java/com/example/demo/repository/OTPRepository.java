package com.example.demo.repository;

import com.example.demo.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByUsernameAndVerifiedTrue(String username);
    Optional<OTP> findByUsernameAndVerifiedFalse(String username);
    void deleteByRequestedAtBefore(LocalDateTime dateTime);
}
