package io.github.jotagm.payment_api.service;

import io.github.jotagm.payment_api.domain.Perna;
import io.github.jotagm.payment_api.domain.RegistrarTransferenciaComando;
import io.github.jotagm.payment_api.domain.TransferenciaRegistrada;
import io.github.jotagm.payment_api.repository.TransferenciaJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(LedgerPostgres.class)
class LedgerPostgresTest {

    @Autowired
    private LedgerPostgres ledgerPostgres;

    @Autowired
    private TransferenciaJpaRepository repository;

    @Test
    void registraTransferenciaEProjetaSaldo() {
        UUID contaA = UUID.randomUUID();
        UUID contaB = UUID.randomUUID();
        RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(UUID.randomUUID(), List.of(
                new Perna(contaA, new BigDecimal("-100")),
                new Perna(contaB, new BigDecimal("100"))
        ));

        ledgerPostgres.registrar(comando);

        assertEquals(0, new BigDecimal("-100").compareTo(ledgerPostgres.saldo(contaA)));
        assertEquals(0, new BigDecimal("100").compareTo(ledgerPostgres.saldo(contaB)));
        assertEquals(1, repository.count());
    }

    @Test
    void comandoRepetidoNaoDuplicaLinhaNoBanco() {
        UUID chave = UUID.randomUUID();
        UUID contaA = UUID.randomUUID();
        UUID contaB = UUID.randomUUID();
        RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(chave, List.of(
                new Perna(contaA, new BigDecimal("-50")),
                new Perna(contaB, new BigDecimal("50"))
        ));

        TransferenciaRegistrada primeiro = ledgerPostgres.registrar(comando);
        TransferenciaRegistrada segundo = ledgerPostgres.registrar(comando);

        assertEquals(primeiro, segundo);
        assertEquals(1, repository.count());
    }
}
