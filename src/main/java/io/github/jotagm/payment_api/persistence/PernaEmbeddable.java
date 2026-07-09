package io.github.jotagm.payment_api.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PernaEmbeddable {

    @Column(name = "conta", nullable = false)
    private UUID conta;

    @Column(name = "valor", nullable = false, precision = 19, scale = 4)
    private BigDecimal valor;
}
