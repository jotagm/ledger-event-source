package io.github.jotagm.payment_api.domain;

import java.util.List;
import java.util.UUID;

public record TransferenciaRegistrada(
        UUID id,
        UUID chaveIdempotencia,
        List<Perna> pernas
) {
}
