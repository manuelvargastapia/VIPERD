package base;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interactor;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IPresentador;
import interno.nucleo.Interfaces.IEntidad;

import java.util.Map;
import java.util.logging.Logger;

public class InteractorUsuarios extends Interactor {
    protected final static Logger TRAZADOR = Logger.getLogger(InteractorUsuarios.class.getName());

    public InteractorUsuarios(IPresentador presentador) {
        super(presentador);
    }
    public InteractorUsuarios(IPresentador presentador, Map<String, String> entorno) {
        super(presentador, entorno);
    }

    //ENUMERADOR DE LOS CASOS DE USO DE ESTE INTERACTOR
    public enum CASODEUSO {
        CONSULTAR_SESION, CONSULTAR_USUARIOS, CONSULTAR_USUARIO, NINGUNO
    }

    //IMPLEMENTACION DE INTERFACE "IInteractor"
    @Override protected void ejecutarCasoDeUso(IDTO dto) {
        TRAZADOR.info(dto.getCasoDeUso().toString());
        IEntidad usuario = new Usuario();
        switch ((CASODEUSO) dto.getCasoDeUso()) {
            case CONSULTAR_SESION:
                if (this.validarParametros(dto, usuario, "P")) {
                    this.prepararOperacion(dto, usuario, "ObtenerSesion");
                    dto.getAgente().Seleccionar(dto);
                    this.procesarRespuesta(dto, usuario, "R");
                }
                break;
            case CONSULTAR_USUARIOS:
                this.prepararOperacion(dto, usuario, "ListaUsuarios");
                dto.getAgente().Seleccionar(dto);
                this.procesarRespuesta(dto, usuario, "L");
                break;
            case CONSULTAR_USUARIO:
                this.validarParametros(dto, usuario, "C");
                this.prepararOperacion(dto, usuario, "SeleccionarUsuario");
                dto.getAgente().Seleccionar(dto);
                this.procesarRespuesta(dto, usuario, "S");
                break;
            case NINGUNO:
                break;
        }
        this.entregarResultado(dto);
    }
}