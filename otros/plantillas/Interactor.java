<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interactor;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IPresentador;
import interno.nucleo.Interfaces.IEntidad;

import java.util.Map;
import java.util.logging.Logger;

//CLASE ${name} - Autor: ${user}
public class ${name} extends Interactor {
    protected final static Logger TRAZADOR = Logger.getLogger(${name}.class.getName());

    public ${name}(IPresentador presentador) {
        super(presentador);
    }
    public ${name}(IPresentador presentador, Map<String, String> entorno) {
        super(presentador, entorno);
    }

    //ENUMERADOR DE LOS CASOS DE USO DE ESTE INTERACTOR
    public enum CASODEUSO {
        NINGUNO
    }

    //IMPLEMENTACION DE INTERFACE "IInteractor"
    @Override protected void ejecutarCasoDeUso(IDTO dto) {
        TRAZADOR.info(dto.getCasoDeUso().toString());
        switch ((CASODEUSO) dto.getCasoDeUso()) {
            case NINGUNO:
                break;

        /* Agregue aquí otros "case" con los casos de uso definidos en el 
           Enum "CASODEUSO", y programe para cada uno las istrucciones
           necesarias para llevarlo a cabo. Utilice los métodos que provee
           la interfaz "IDTO". Para simplificar la programación puede crear
           clases que definan las Entidades del Dominio extendiendo de la 
           clase "Entidad" que implementa la interface "IEntidad". */

        /* EJEMPLOS DE USO DE OPERACIONES (supone que existe una Entidad de 
           Dominio llamada "Usuario" definida en una clase con ese nombre,
           y que en el DAO a usar existen las operaciones: AgregarUsuario, 
           SeleccionarUsuario, EditarUsuario, BorrarUsuario y ListaUsuarios.

                IEntidad usuario = new Usuario();
                String uid = "";

                //AGREGAR USUARIO
                TRAZADOR.info("----------AGREGAR USUARIO----------");
                dto.getPeticion().Limpiar();
                dto.setElemento("alias", "Juan Gonzalez");
                dto.setElemento("email", "juangonzalez@correo.com");
                dto.setElemento("login", "jgonzalez");
                if (this.validarParametros(dto, usuario, "I")) {
                    this.prepararOperacion(dto, usuario, "AgregarUsuario");
                    dto.getAgente().Agregar(dto);
                    this.procesarRespuesta(dto, usuario, "C");
                    uid = usuario.get("id");
                    TRAZADOR.info("AGREGADO ID = " + uid + " - " + usuario.entidadToString());
                }
                
                //LEER USUARIO
                TRAZADOR.info("----------LEER USUARIO----------");
                dto.getPeticion().Limpiar();
                dto.setElemento("uid", uid);
                this.validarParametros(dto, usuario, "C");
                this.prepararOperacion(dto, usuario, "SeleccionarUsuario");
                dto.getAgente().Seleccionar(dto);
                this.procesarRespuesta(dto, usuario, "S");
                TRAZADOR.info("LEIDO - " + usuario.entidadToString());
                
                //EDITAR USUARIO
                TRAZADOR.info("----------EDITAR USUARIO----------");
                dto.getPeticion().Limpiar();
                dto.setElemento("uid", uid);
                dto.setElemento("email", "jgonzalezp@correo.com");
                dto.setElemento("alias", "Juan Antonio Gonzalez");
                if (this.validarParametros(dto, usuario, "U")) {
                    this.prepararOperacion(dto, usuario, "EditarUsuario");
                    dto.getAgente().Editar(dto);
                    this.procesarRespuesta(dto, usuario, "");
                    TRAZADOR.info("EDITADO - " + usuario.entidadToString());
                }

                //BORRAR USUARIO
                TRAZADOR.info("----------BORRAR USUARIO----------");
                dto.getPeticion().Limpiar();
                dto.setElemento("uid", uid);
                this.validarParametros(dto, usuario, "C");
                this.prepararOperacion(dto, usuario, "BorrarUsuario");
                dto.getAgente().Borrar(dto);
                this.procesarRespuesta(dto, usuario, "X");
                TRAZADOR.info("BORRADO: " + usuario.entidadToString());

                //LISTA DE USUARIOS
                TRAZADOR.info("----------LISTA DE USUARIOS----------");
                dto.getPeticion().Limpiar();
                this.prepararOperacion(dto, usuario, "ListaUsuarios");
                dto.getAgente().Seleccionar(dto);
                this.procesarRespuesta(dto, usuario, "L");

        */
        /* EJEMPLOS DE USO DE FUNCIONES:
            IEntidad usuario = new Usuario();
            //1-Funciones incorporadas:
                float resultado = usuario.Calcular(Entidad.CALCULO.PrecioBrutoDesdeNeto, new float[]{75000});
                float resultado = usuario.Calcular(Entidad.CALCULO.HonBrutoDesdeNeto, new float[]{250000});
            //2-Funciones personalizadas definidas en la clase "Usuario":
                float resultado = usuario.Calcular(Usuario.CALCULO.Personalizado, new float[]{5});
        */

        }
        this.entregarResultado(dto);
    }
}