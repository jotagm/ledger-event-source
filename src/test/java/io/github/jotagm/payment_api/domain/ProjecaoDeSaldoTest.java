package io.github.jotagm.payment_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjecaoDeSaldoTest {

    private final UUID contaA = UUID.randomUUID();
    private final UUID contaB = UUID.randomUUID();
    private final UUID contaC = UUID.randomUUID();

    @Test
    void saldoDeContaSemEventosEZero() {
        BigDecimal saldo = ProjecaoDeSaldo.saldo(List.of(), contaA);

        assertEquals(0, BigDecimal.ZERO.compareTo(saldo));
    }

    @Test
    void saldoSomaAsPernasDeUmaUnicaTransferencia() {
        TransferenciaRegistrada evento = new TransferenciaRegistrada(
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(
                        new Perna(contaA, new BigDecimal("-100")),
                        new Perna(contaB, new BigDecimal("100"))
                )
        );

        assertEquals(0, new BigDecimal("-100").compareTo(ProjecaoDeSaldo.saldo(List.of(evento), contaA)));
        assertEquals(0, new BigDecimal("100").compareTo(ProjecaoDeSaldo.saldo(List.of(evento), contaB)));
    }

    @Test
    void saldoAcumulaVariasTransferenciasEmOrdem() {
        List<TransferenciaRegistrada> eventos = List.of(
                new TransferenciaRegistrada(UUID.randomUUID(), UUID.randomUUID(), List.of(
                        new Perna(contaA, new BigDecimal("-100")),
                        new Perna(contaB, new BigDecimal("100"))
                )),
                new TransferenciaRegistrada(UUID.randomUUID(), UUID.randomUUID(), List.of(
                        new Perna(contaB, new BigDecimal("-40")),
                        new Perna(contaC, new BigDecimal("40"))
                )),
                new TransferenciaRegistrada(UUID.randomUUID(), UUID.randomUUID(), List.of(
                        new Perna(contaA, new BigDecimal("30")),
                        new Perna(contaC, new BigDecimal("-30"))
                ))
        );

        assertEquals(0, new BigDecimal("-70").compareTo(ProjecaoDeSaldo.saldo(eventos, contaA)));
        assertEquals(0, new BigDecimal("60").compareTo(ProjecaoDeSaldo.saldo(eventos, contaB)));
        assertEquals(0, new BigDecimal("10").compareTo(ProjecaoDeSaldo.saldo(eventos, contaC)));
    }

    @Test
    void ignoraPernasDeOutrasContas() {
        TransferenciaRegistrada evento = new TransferenciaRegistrada(
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(
                        new Perna(contaB, new BigDecimal("-100")),
                        new Perna(contaC, new BigDecimal("100"))
                )
        );

        assertEquals(0, BigDecimal.ZERO.compareTo(ProjecaoDeSaldo.saldo(List.of(evento), contaA)));
    }
}
