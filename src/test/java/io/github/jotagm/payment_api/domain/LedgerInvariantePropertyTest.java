package io.github.jotagm.payment_api.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LedgerInvariantePropertyTest {

    private static final int QUANTIDADE_CONTAS = 10;
    private static final int QUANTIDADE_TRANSFERENCIAS = 1000;

    @Test
    void somaDosSaldosDeTodasAsContasSempreBateComZero() {
        Random random = new Random(42);
        List<UUID> contas = new ArrayList<>();
        for (int i = 0; i < QUANTIDADE_CONTAS; i++) {
            contas.add(UUID.randomUUID());
        }

        Ledger ledger = new Ledger();

        for (int i = 0; i < QUANTIDADE_TRANSFERENCIAS; i++) {
            UUID origem = contas.get(random.nextInt(QUANTIDADE_CONTAS));
            UUID destino = escolherContaDiferente(random, contas, origem);
            BigDecimal valor = new BigDecimal(1 + random.nextInt(1000));

            RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(
                    UUID.randomUUID(),
                    List.of(new Perna(origem, valor.negate()), new Perna(destino, valor))
            );

            ledger.registrar(comando);
        }

        BigDecimal somaDosSaldos = contas.stream()
                .map(conta -> ProjecaoDeSaldo.saldo(ledger.eventos(), conta))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(0, BigDecimal.ZERO.compareTo(somaDosSaldos));
    }

    private UUID escolherContaDiferente(Random random, List<UUID> contas, UUID origem) {
        UUID destino;
        do {
            destino = contas.get(random.nextInt(contas.size()));
        } while (destino.equals(origem));
        return destino;
    }
}
