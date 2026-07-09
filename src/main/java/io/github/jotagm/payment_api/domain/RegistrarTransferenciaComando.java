package io.github.jotagm.payment_api.domain;

import java.util.List;
import java.util.UUID;

public record RegistrarTransferenciaComando(
        UUID chaveIdempotencia,
        List<Perna> pernas
) {
}
