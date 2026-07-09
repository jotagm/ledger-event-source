package io.github.jotagm.payment_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistradorDeTransferenciaTest {

    @Test
    void geraEventoComIdNovoEPreservaChaveEPernas() {
        UUID chaveIdempotencia = UUID.randomUUID();
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), new BigDecimal("-100")),
                new Perna(UUID.randomUUID(), new BigDecimal("100"))
        );
        RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(chaveIdempotencia, pernas);

        TransferenciaRegistrada evento = RegistradorDeTransferencia.registrar(comando);

        assertNotNull(evento.id());
        assertEquals(chaveIdempotencia, evento.chaveIdempotencia());
        assertEquals(pernas, evento.pernas());
    }

    @Test
    void cadaChamadaGeraIdDeEventoDiferente() {
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), new BigDecimal("-50")),
                new Perna(UUID.randomUUID(), new BigDecimal("50"))
        );
        RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(UUID.randomUUID(), pernas);

        TransferenciaRegistrada primeiroEvento = RegistradorDeTransferencia.registrar(comando);
        TransferenciaRegistrada segundoEvento = RegistradorDeTransferencia.registrar(comando);

        assertNotEquals(primeiroEvento.id(), segundoEvento.id());
    }

    @Test
    void rejeitaComandoComPernasInvalidas() {
        List<Perna> pernas = List.of(
                new Perna(UUID.randomUUID(), new BigDecimal("-100")),
                new Perna(UUID.randomUUID(), new BigDecimal("99"))
        );
        RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(UUID.randomUUID(), pernas);

        assertThrows(PernasInvalidasException.class, () -> RegistradorDeTransferencia.registrar(comando));
    }
}
