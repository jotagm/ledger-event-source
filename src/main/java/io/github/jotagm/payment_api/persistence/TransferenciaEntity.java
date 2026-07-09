package io.github.jotagm.payment_api.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transferencia_registrada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaEntity {

    @Id
    private UUID id;

    @Column(name = "chave_idempotencia", nullable = false, unique = true)
    private UUID chaveIdempotencia;

    @ElementCollection
    @CollectionTable(name = "perna", joinColumns = @JoinColumn(name = "transferencia_id"))
    private List<PernaEmbeddable> pernas;
}
