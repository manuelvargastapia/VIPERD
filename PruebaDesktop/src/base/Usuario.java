package base;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Entidad;
import interno.nucleo.Interfaces.IEntidad;
import interno.nucleo.Interfaces.IEstructura;
import interno.nucleo.Interfaces.ICalculo;

import java.util.Map;

public class Usuario extends Entidad {

    //CONSTRUCTORES
    public Usuario() {
        super(ESTRUCTURA.class);
    }
    public Usuario(IEntidad usuario) {
        super(ESTRUCTURA.class);
        setAtributos(usuario.getAtributos());
    }
    public Usuario(Map<String, String> atributos) {
        super(ESTRUCTURA.class);
        setAtributos(atributos);
    }

    //DEFINICIONES DE ESTRUCTURA, CALCULOS Y OPERACIONES
    private enum ESTRUCTURA implements IEstructura {
        id("uid", "id", "ID", "CALR", "ID", "1", "999999999", "ID debe ser un número", "ID debe estar entre [min] y [max]", "ID no válido", "", ""),
        usuario("login", "login", "", "APSRI", "LOGIN", "3", "10", "Usuario sólo acepta letras y números", "Usuario debe tener [min] - [max] caracteres", "Nombre de usuario no válido", "", ""),
        password("password", "password", "", "P", "PASSWORD", "3", "10", "Contraseña sólo acepta letras y números", "Contraseña debe tener [min] - [max] caracteres", "", "", ""),
        rut("rut", "rut", "", "AS", "RUT", "8", "12", "RUT sólo acepta números, letra K y guión", "RUT debe tener [min] - [max] caracteres", "El RUT no es válido", "", ""),
        alias("alias", "alias", "Nombre", "ARSUIL", "NOMBRE", "3", "25", "Alias sólo acepta letras, números y espacios", "Alias debe tener [min] - [max] caracteres", "", "", ""),
        email("email", "email", "E-Mail", "ARSUIL", "EMAIL", "0", "50", "E-mail contiene caracteres no permitidos", "E-mail debe tener [min] - [max] caracteres", "E-mail tiene un formato no válido", "", ""),
        roles("roles", "roles", "", "ARS", "", "", "", "", "", "", "3", ""),
        grupos("grupos", "grupos", "", "ASR", "", "", "", "", "", "", "", ""),
        codigo("codigo", "codigo", "", "AR", "", "", "", "", "", "", "", ""),
        fecha("fecha", "fecha", "", "AR", "FECHA", "0", "10", "Fecha sólo acepta dígitos y separadores", "Fecha debe tener hasta [max] caracteres", "La Fecha no es válida", "", ""),
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
        ;
        CALCULO() {}
        @Override public abstract float Calcular(float[] valor);
    }
}