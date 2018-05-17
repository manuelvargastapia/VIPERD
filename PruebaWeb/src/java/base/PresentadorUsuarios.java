package base;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interfaces.IVista;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IInteractor;
import interno.nucleo.Interfaces.IAccion;
import interno.usuario.Presentador;
import base.InteractorUsuarios.CASODEUSO;

import java.util.Map;
import java.util.logging.Logger;

public class PresentadorUsuarios extends Presentador {
    protected final static Logger TRAZADOR = Logger.getLogger(PresentadorUsuarios.class.getName());

    public PresentadorUsuarios(IVista vista) {
        super(vista);
    }
    public PresentadorUsuarios(IVista vista, Map<String, String> entorno) {
        super(vista, entorno);
    }
    
    //ENUMERADOR DE LAS ACCIONES DE ESTE PRESENTADOR
    public enum ACCION implements IAccion {
        Nada(""),
        Autenticarse(""),
        IniciarSesion(""), 
        CerrarSesion(""), 
        ListaUsuarios("1,2"), 
        VerUsuario("1,2"), 
        ;
        ACCION(String req) {this.requisitos = req;}
        private final String requisitos;
        public String Requisitos() {return this.requisitos;}
    }

    //IMPLEMENTACION DE INTERFACE "IPresentador"
    @Override public void pedirAccion(Enum accion, String caso) {
        TRAZADOR.info(accion.toString());
        if (!autorizarAcceso(accion, this.entorno.get("usuario"))) {
            vista.Redirigir("NoAutorizado");
        } else {
            IInteractor interactor = new InteractorUsuarios(this);
            IDTO dto = interactor.crearDTO(accion);

            //SELECTOR DE ACCIONES PARA SOLICITAR CASOS DE USO
            switch ((ACCION) accion) {
                case Nada:
                case Autenticarse:
                    break;
                case IniciarSesion:
                    dto.setElementos(vista.getValores());
                    dto.setCasoDeUso(CASODEUSO.CONSULTAR_SESION);
                    break;
                case CerrarSesion:
                    break;
                case ListaUsuarios:
                    dto.setCasoDeUso(CASODEUSO.CONSULTAR_USUARIOS);
                    break;
                case VerUsuario:
                    dto.setCasoDeUso(CASODEUSO.CONSULTAR_USUARIO);
                    dto.setElemento("uid", caso);
                    break;
            }
            if (comprobarSolicitud(accion, dto.getCasoDeUso(), true)) {
                dto.setAgente(new AgenteUsuarios());
                interactor.solicitarCasoDeUso(dto);
            } else {
                mostrarResultado(dto);
            }
        }
    }
    @Override public void mostrarResultado(IDTO dto) {
        TRAZADOR.info("");
        if (comprobarSolicitud(dto.getAccion(), dto.getCasoDeUso(), false)) {
            switch ((ACCION) dto.getAccion()) {
                case Nada:
                    break;
                case Autenticarse:
                    vista.mostrarVista(true);
                    break;
                case IniciarSesion:
                    vista.mostrarMensajes(dto.getMensajes());
                    if (dto.getEstado()) {
                        vista.setEntorno("usuario", dto.getElemento("uid"));
                        vista.setEntorno("roles", dto.getElemento("roles"));
                        vista.Redirigir(vista.getValor("direccion"));
                    } else {
                        vista.mostrarVista(true);
                    }
                    break;
                case CerrarSesion:
                    vista.Cerrar();
                    vista.mostrarVista(true);
                    break;
                case ListaUsuarios: 
                    vista.mostrarMensajes(dto.getMensajes());
                    vista.actualizarConjunto(dto.getConjunto("ListaUsuarios"));
                    vista.mostrarVista(true);
                    break;
                case VerUsuario: 
                    vista.actualizarContenedor(dto.getElementos());
                    vista.mostrarVista(true);
                    break;
            }
        }
    }
}