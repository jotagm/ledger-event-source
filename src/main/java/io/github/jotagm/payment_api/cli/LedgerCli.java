package io.github.jotagm.payment_api.cli;

import io.github.jotagm.payment_api.domain.Perna;
import io.github.jotagm.payment_api.domain.RegistrarTransferenciaComando;
import io.github.jotagm.payment_api.domain.TransferenciaRegistrada;
import io.github.jotagm.payment_api.service.LedgerPostgres;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Component
@Profile("cli")
@RequiredArgsConstructor
public class LedgerCli implements CommandLineRunner {

    private final LedgerPostgres ledgerPostgres;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("payment-api ledger cli. comandos:");
        System.out.println("  transferir <origem> <destino> <valor> [chave]");
        System.out.println("  saldo <conta>");
        System.out.println("  sair");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String linha;
        while ((linha = reader.readLine()) != null) {
            String[] partes = linha.trim().split("\\s+");
            if (partes.length == 0 || partes[0].isBlank()) {
                continue;
            }
            if (partes[0].equalsIgnoreCase("sair")) {
                System.out.println("saindo.");
                break;
            }

            try {
                executar(partes);
            } catch (Exception e) {
                System.out.println("erro: " + e.getMessage());
            }
        }
    }

    private void executar(String[] partes) {
        switch (partes[0].toLowerCase()) {
            case "transferir" -> {
                UUID origem = contaDe(partes[1]);
                UUID destino = contaDe(partes[2]);
                BigDecimal valor = new BigDecimal(partes[3]);
                UUID chave = partes.length > 4 ? contaDe(partes[4]) : UUID.randomUUID();

                RegistrarTransferenciaComando comando = new RegistrarTransferenciaComando(
                        chave,
                        List.of(new Perna(origem, valor.negate()), new Perna(destino, valor))
                );

                TransferenciaRegistrada evento = ledgerPostgres.registrar(comando);
                System.out.println("evento registrado: " + evento.id());
            }
            case "saldo" -> {
                UUID conta = contaDe(partes[1]);
                System.out.println("saldo de " + partes[1] + ": " + ledgerPostgres.saldo(conta));
            }
            default -> System.out.println("comando desconhecido: " + partes[0]);
        }
    }

    private UUID contaDe(String nome) {
        return UUID.nameUUIDFromBytes(nome.getBytes(StandardCharsets.UTF_8));
    }
}
