package io.github.jotagm.payment_api.repository;

import io.github.jotagm.payment_api.persistence.TransferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransferenciaJpaRepository extends JpaRepository<TransferenciaEntity, UUID> {

    Optional<TransferenciaEntity> findByChaveIdempotencia(UUID chaveIdempotencia);
}
