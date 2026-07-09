package io.github.jotagm.payment_api.domain;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ProjecaoDeSaldo {

    public BigDecimal saldo(List<TransferenciaRegistrada> eventos, UUID conta) {
        return eventos.stream()
                .flatMap(evento -> evento.pernas().stream())
                .filter(perna -> perna.conta().equals(conta))
                .map(Perna::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
