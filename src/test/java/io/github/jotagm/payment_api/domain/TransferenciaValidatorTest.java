package io.github.jotagm.payment_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransferenciaValidatorTest {

    @Test
    void aceitaDuasPernasQueSomamZero() {
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), new BigDecimal("-100")),
                new Perna(UUID.randomUUID(), new BigDecimal("100"))
        );

        assertDoesNotThrow(() -> TransferenciaValidator.validar(pernas));
    }

    @Test
    void rejeitaPernasQueNaoSomamZero() {
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), new BigDecimal("-100")),
                new Perna(UUID.randomUUID(), new BigDecimal("99"))
        );

        assertThrows(PernasInvalidasException.class, () -> TransferenciaValidator.validar(pernas));
    }

    @Test
    void rejeitaMenosDeDuasPernas() {
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), BigDecimal.ZERO)
        );

        assertThrows(PernasInvalidasException.class, () -> TransferenciaValidator.validar(pernas));
    }

    @Test
    void rejeitaMaisDeDuasPernas() {
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), new BigDecimal("-100")),
                new Perna(UUID.randomUUID(), new BigDecimal("60")),
                new Perna(UUID.randomUUID(), new BigDecimal("40"))
        );

        assertThrows(PernasInvalidasException.class, () -> TransferenciaValidator.validar(pernas));
    }

    @Test
    void rejeitaListaNula() {
        assertThrows(PernasInvalidasException.class, () -> TransferenciaValidator.validar(null));
    }
}
