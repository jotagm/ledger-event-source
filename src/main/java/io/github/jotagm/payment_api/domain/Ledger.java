package io.github.jotagm.payment_api.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Ledger {

    private final List<TransferenciaRegistrada> eventos = new ArrayList<>();
    private final Map<UUID, TransferenciaRegistrada> eventosPorChaveIdempotencia = new HashMap<>();

    public TransferenciaRegistrada registrar(RegistrarTransferenciaComando comando) {
        TransferenciaRegistrada eventoExistente = eventosPorChaveIdempotencia.get(comando.chaveIdempotencia());
        if (eventoExistente != null) {
            return eventoExistente;
        }

        TransferenciaRegistrada evento = RegistradorDeTransferencia.registrar(comando);
        eventos.add(evento);
        eventosPorChaveIdempotencia.put(comando.chaveIdempotencia(), evento);
        return evento;
    }

    public List<TransferenciaRegistrada> eventos() {
        return List.copyOf(eventos);
    }
}
