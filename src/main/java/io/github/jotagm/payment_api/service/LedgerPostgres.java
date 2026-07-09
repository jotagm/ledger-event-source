package io.github.jotagm.payment_api.service;

import io.github.jotagm.payment_api.domain.Perna;
import io.github.jotagm.payment_api.domain.ProjecaoDeSaldo;
import io.github.jotagm.payment_api.domain.RegistrarTransferenciaComando;
import io.github.jotagm.payment_api.domain.RegistradorDeTransferencia;
import io.github.jotagm.payment_api.domain.TransferenciaRegistrada;
import io.github.jotagm.payment_api.persistence.PernaEmbeddable;
import io.github.jotagm.payment_api.persistence.TransferenciaEntity;
import io.github.jotagm.payment_api.repository.TransferenciaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerPostgres {

    private final TransferenciaJpaRepository repository;

    @Transactional
    public TransferenciaRegistrada registrar(RegistrarTransferenciaComando comando) {
        return repository.findByChaveIdempotencia(comando.chaveIdempotencia())
                .map(this::paraDominio)
                .orElseGet(() -> {
                    TransferenciaRegistrada evento = RegistradorDeTransferencia.registrar(comando);
                    repository.save(paraEntidade(evento));
                    return evento;
                });
    }

    @Transactional(readOnly = true)
    public BigDecimal saldo(UUID conta) {
        List<TransferenciaRegistrada> eventos = repository.findAll().stream()
                .map(this::paraDominio)
                .toList();
        return ProjecaoDeSaldo.saldo(eventos, conta);
    }

    private TransferenciaEntity paraEntidade(TransferenciaRegistrada evento) {
        List<PernaEmbeddable> pernas = evento.pernas().stream()
                .map(perna -> new PernaEmbeddable(perna.conta(), perna.valor()))
                .toList();
        return new TransferenciaEntity(evento.id(), evento.chaveIdempotencia(), pernas);
    }

    private TransferenciaRegistrada paraDominio(TransferenciaEntity entity) {
        List<Perna> pernas = entity.getPernas().stream()
                .map(perna -> new Perna(perna.getConta(), perna.getValor()))
                .toList();
        return new TransferenciaRegistrada(entity.getId(), entity.getChaveIdempotencia(), pernas);
    }
}
