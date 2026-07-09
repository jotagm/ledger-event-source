package io.github.jotagm.payment_api.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Perna(
        UUID conta,
        BigDecimal valor
) {
}
