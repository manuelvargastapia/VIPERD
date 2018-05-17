package base;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interfaces.IInteractor;
import interno.nucleo.Interfaces.IDTO;
import desktop.usuario.Sesion;
import base.InteractorUsuarios.CASODEUSO;

import java.util.logging.Logger;

public class Probador {
    protected final static Logger TRAZADOR = Logger.getLogger(Probador.class.getName());

    public static void main(String[] args) {
        Sesion.Abrir();
        
        interactor = new InteractorUsuarios(null, Sesion.Entorno());
        IDTO dto = interactor.crearDTO(null);
        dto.setAgente(new AgenteUsuarios());
        dto.setAsincrono(true);
        dto.setCasoDeUso(CASODEUSO.CONSULTAR_USUARIOS);

        interactor.solicitarCasoDeUso(dto);
    }

    static IInteractor interactor;
}