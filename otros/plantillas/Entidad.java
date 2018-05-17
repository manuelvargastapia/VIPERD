<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Entidad;
import interno.nucleo.Interfaces.IEntidad;
import interno.nucleo.Interfaces.IEstructura;
import interno.nucleo.Interfaces.ICalculo;

import java.util.Map;

//CLASE ${name} - Autor: ${user}
public class ${name} extends Entidad {

    //CONSTRUCTORES
    public ${name}() {
        super(ESTRUCTURA.class);
    }
    public ${name}(IEntidad usuario) {
        super(ESTRUCTURA.class);
        setAtributos(usuario.getAtributos());
    }
    public ${name}(Map<String, String> atributos) {
        super(ESTRUCTURA.class);
        setAtributos(atributos);
    }

    //DEFINICIONES DE ESTRUCTURA Y CALCULOS
    private enum ESTRUCTURA implements IEstructura {
        /* COLUMNAS: 
        1-Nombre en componente de VISTA.
        2-Nombre en campo de DATOS.
        3-ETIQUETA del ítem en Formularios y Listados.
        4-FILTRO para determinar losusos del ítem: A,R,P,C,S,I,U,L.
        5-REGLA de validación a aplicar (definidas en Entidad).
        6-Valor o tamaño MÍNIMO que debe tener el dato.
        7-Valor o tamaño MÁXIMO que debe tener el dato.
        8-Mensaje de error cuando dato contiene caracteres no permitidos.
        9-Mensaje de error cuando dato está fuera de rango (valor o tamaño).
        10-Mensaje de error cuando dato no cumple con el patrón de una expresión regular.
        FILTROS: 
        A-Atributo R-Respuesta P-Petición C-Clave S-Select I-Insert U-Update L-Lista */
        /* Agregar todos los ítems de ESTRUCTURA que se requieran para definir a la
           Entidad, tanto atributos como otros valores que se necesite que ésta use, 
           valide o transforme. Todos los ítems deben tener 10 columnas. */
        id("uid", "id", "ID", "CALR", "ID", "1", "2147483647", "ID debe ser un número", "ID debe estar entre [min] y [max]", "ID no válido", "", ""),
        ;
        ESTRUCTURA(String v, String d, String et, String f, String re, String mi, String ma, String ei, String em, String ex, String ini, String ti) {this.D = d; this.V = v; this.etiqueta = et; this.filtro = f; this.regla = re; this.min = mi; this.max = ma; this.err_inclus = ei; this.err_minmax = em; this.err_expreg = ex; this.inicial = ini; this.tipo = ti;}
        private final String V, D, etiqueta, min, max, err_inclus, err_minmax, err_expreg, regla, filtro, inicial, tipo;
        @Override public String V() {return this.V;}
        @Override public String D() {return this.D;}
        @Override public String Etiqueta() {return this.etiqueta;}
        @Override public String Filtro() {return this.filtro;}
        @Override public String Regla() {return this.regla;}
        @Override public String Min() {return this.min;}
        @Override public String Max() {return this.max;}
        @Override public String E1() {return this.err_inclus;}
        @Override public String E2() {return this.err_minmax;}
        @Override public String E3() {return this.err_expreg;}
        @Override public String Inicial() {return this.inicial;}
        @Override public String Tipo() {return this.tipo;}
        @Override public String Nombre() {return name();}
    }
    protected enum CALCULO implements ICalculo {
        Personalizado() {@Override public float Calcular(float[] valor) {return valor[0] + 1;}},
        /* Agregar las definiciones de funciones de cálculo que esta Entidad vaya
           a implementar, si así se requiere. El argumento de entrada debe ser un
           Array de float (float[]), y el valor de retorno será un número float. */
        ;
        CALCULO() {}
        @Override public abstract float Calcular(float[] valor);
    }
}