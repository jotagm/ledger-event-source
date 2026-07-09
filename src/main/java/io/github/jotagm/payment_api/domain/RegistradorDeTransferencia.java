package io.github.jotagm.payment_api.domain;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class RegistradorDeTransferencia {

    public TransferenciaRegistrada registrar(RegistrarTransferenciaComando comando) {
        TransferenciaValidator.validar(comando.pernas());

        return new TransferenciaRegistrada(
                UUID.randomUUID(),
                comando.chaveIdempotencia(),
                comando.pernas()
        );
    }
}
