package io.github.jotagm.payment_api.domain;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class TransferenciaValidator {

    private final int QUANTIDADE_PERNAS_ESPERADA = 2;

    public void validar(List<Perna> pernas) {
        if (pernas == null || pernas.size() != QUANTIDADE_PERNAS_ESPERADA) {
            throw new PernasInvalidasException(
                    "transferencia deve ter exatamente " + QUANTIDADE_PERNAS_ESPERADA + " pernas");
        }

        BigDecimal soma = pernas.stream()
                .map(Perna::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (soma.compareTo(BigDecimal.ZERO) != 0) {
            throw new PernasInvalidasException("pernas devem somar zero, soma atual: " + soma);
        }
    }
}
