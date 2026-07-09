package io.github.jotagm.payment_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LedgerTest {

    private final UUID contaA = UUID.randomUUID();
    private final UUID contaB = UUID.randomUUID();

    @Test
    void comandoRepetidoNaoDuplicaEventoNemAlteraSaldoDeNovo() {
        Ledger ledger = new Ledger();
        RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(UUID.randomUUID(), List.of(
                new Perna(contaA, new BigDecimal("-100")),
                new Perna(contaB, new BigDecimal("100"))
        ));

        TransferenciaRegistrada primeiroResultado = ledger.registrar(comando);
        BigDecimal saldoAposPrimeira = ProjecaoDeSaldo.saldo(ledger.eventos(), contaA);

        TransferenciaRegistrada segundoResultado = ledger.registrar(comando);
        BigDecimal saldoAposSegunda = ProjecaoDeSaldo.saldo(ledger.eventos(), contaA);

        assertEquals(1, ledger.eventos().size());
        assertEquals(primeiroResultado, segundoResultado);
        assertEquals(0, saldoAposPrimeira.compareTo(saldoAposSegunda));
    }

    @Test
    void chavesDeIdempotenciaDiferentesGeramEventosDistintos() {
        Ledger ledger = new Ledger();
        RegistrarTransferenciaComando primeiroComando = new RegistrarTransferenciaComando(UUID.randomUUID(), List.of(
                new Perna(contaA, new BigDecimal("-100")),
                new Perna(contaB, new BigDecimal("100"))
        ));
        RegistrarTransferenciaComando segundoComando = new RegistrarTransferenciaComando(UUID.randomUUID(), List.of(
                new Perna(contaA, new BigDecimal("-50")),
                new Perna(contaB, new BigDecimal("50"))
        ));

        TransferenciaRegistrada primeiroEvento = ledger.registrar(primeiroComando);
        TransferenciaRegistrada segundoEvento = ledger.registrar(segundoComando);

        assertEquals(2, ledger.eventos().size());
        assertNotEquals(primeiroEvento.id(), segundoEvento.id());
        assertEquals(0, new BigDecimal("-150").compareTo(ProjecaoDeSaldo.saldo(ledger.eventos(), contaA)));
    }

    @Test
    void eventosExpostosSaoImutaveis() {
        Ledger ledger = new Ledger();

        assertThrows(UnsupportedOperationException.class, () -> ledger.eventos().add(null));
    }
}
