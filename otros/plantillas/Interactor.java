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
           Dominio llamada "Persona" definida en una clase con ese nombre,
           y que en el DAO a usar existen las operaciones: ListaPersonas, 
           LeerPersona, EditarPersona, AgregarPersona y BorrarPersona.
            //1-Listar personas (supone que existe una Entidad "Persona"):
                IEntidad entidad = new Persona();
                this.prepararOperacion(dto, entidad, "ListaPersonas");
                dto.getAgente().Seleccionar(dto);
                this.procesarRespuesta(dto, entidad, "L");
            //2-Leer una Persona a partir de su ID:
                IEntidad entidad = new Persona();
                entidad.set("id", "1");
                this.prepararOperacion(dto, entidad, "LeerPersona");
                dto.getAgente().Seleccionar(dto);
                this.procesarRespuesta(dto, entidad, "R");
                TRAZADOR.info("LEIDO - " + entidad.entidadToString());
            //3-Editar una Persona a partir de su ID:
                IEntidad entidad = new Persona();
                entidad.set("id", "1");
                dto.setElemento("nombre", "Juan Gonzalez");
                dto.setElemento("email", "juangonzalez@correo.com");
                if (this.validarParametros(dto, entidad, "U")) {
                    this.prepararOperacion(dto, entidad, "EditarPersona");
                    dto.getAgente().Editar(dto);
                    this.procesarRespuesta(dto, entidad, "");
                    TRAZADOR.info("EDITADO - " + entidad.entidadToString());
                }
            //4-Crear una Persona y obtener su ID:
                IEntidad entidad = new Persona();
                dto.setElemento("alias", "Juan Gonzalez");
                dto.setElemento("email", "juangonzalez@correo.com");
                if (this.validarParametros(dto, entidad, "I")) {
                    this.prepararOperacion(dto, entidad, "AgregarPersona");
                    dto.getAgente().Agregar(dto);
                    this.procesarRespuesta(dto, entidad, "C");
                    TRAZADOR.info("AGREGADO: " + entidad.entidadToString());
                    TRAZADOR.info("ID = " + entidad.get("id"));
                }
            //5-Borrar una Persona a partir de su ID:
                IEntidad entidad = new Persona();
                entidad.set("id", "1");
                this.prepararOperacion(dto, entidad, "BorrarPersona");
                dto.getAgente().Borrar(dto);
                this.procesarRespuesta(dto, entidad, "X");
                TRAZADOR.info("BORRADO: " + entidad.entidadToString());
                */

        /* EJEMPLOS DE USO DE FUNCIONES:
            IEntidad entidad = new Persona();
            //1-Funciones incorporadas:
                float resultado = entidad.Calcular(Entidad.CALCULO.PrecioBrutoDesdeNeto, new float[]{75000});
                float resultado = entidad.Calcular(Entidad.CALCULO.HonBrutoDesdeNeto, new float[]{250000});
            //2-Funciones personalizadas definidas en la clase "Persona":
                float resultado = entidad.Calcular(Persona.CALCULO.Personalizado, new float[]{5});
            */
        }
        this.entregarResultado(dto);
    }
}