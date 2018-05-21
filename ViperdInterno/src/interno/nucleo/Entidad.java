package interno.nucleo;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IEntidad;
import interno.nucleo.Interfaces.IValueObject;
import interno.nucleo.Interfaces.IEstructura;
import interno.nucleo.Interfaces.ICalculo;
import interno.nucleo.Interfaces.IRegla;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//SUPERCLASE ABSTRACTA Entidad
public abstract class Entidad implements IEntidad {
    protected final static Logger TRAZADOR = Logger.getLogger(Entidad.class.getName());
    
    private Map<String, IValueObject> estructura;
    private Map<String, String> atributos, validaciones;
    private Calculadora calculadora;
    private Boolean estado;

    //ENUMERADOS PUBLICOS
    public enum REGLA implements IRegla {
        EMAIL("", "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", "", ""),
        PASSWORD("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", "", "", ""),
        LOGIN("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", "", "", ""),
        NOMBRE("abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ.- áéíóúäëïöüÁÉÍÓÚÄËÏÖÜÇçª", "", "", ""),
        TEXTO("abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ- áéíóúäëïöüÁÉÍÓÚÄËÏÖÜÇç\"\n\b\r\t\\@°ºª!#$%/()=?¡¿1234567890*+[]{}~_.:,;<>&'", "", "", ""),
        FECHA("", "^\\d{1,2}\\/\\d{1,2}\\/\\d{2,4}$", "dd/MM/yyyy", "yyyy-MM-dd"),
        HORA("", "^(0[1-9]|1\\d|2[0-3]):([0-5]\\d))$", "HH:mm", "HH:mm:ss"),
        FONO("0123456789+ ()-", "", "", ""),
        RUT("", "^[0-9]+-[0-9kK]{1}$", "", ""),
        DECIMAL("0123456789.,-", "", "", ""),
        ENTERO("0123456789-", "", "", ""),
        URL("", "^(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)( [a-zA-Z0-9\\-\\.\\?\\,\\’\\/\\\\\\+&%\\$#_]*)?$", "", ""),
        ID("0123456789", "", "", ""),
        ;
        REGLA(String inc, String exp, String fv, String fd) {this.inc = inc; this.exp = exp; this.fv = fv; this.fd = fd;}
        private final String inc, exp, fv, fd;
        public String Inclusion() {return this.inc;}
        public String ExpRegular() {return this.exp;}
        public String FormatoVista() {return this.fv;}
        public String FormatoDatos() {return this.fd;}
    }
    public enum CALCULO implements ICalculo {
        IvaDesdeNeto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] * 19 / 100);}},
        IvaDesdeBruto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] * 19 / 119);}},
        PrecioNetoDesdeBruto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] / 1.19);}},
        PrecioBrutoDesdeNeto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] * 1.19);}},
        HonBrutoDesdeNeto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] / 0.9);}},
        HonNetoDesdeBruto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] * 0.9);}},
        RetenHonDesdeBruto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] * 0.1);}},
        RetenHonDesdeNeto() {@Override public float Calcular(float[] valor) {return Math.round(valor[0] / 9);}},
        ;
        CALCULO() {}
        @Override public abstract float Calcular(float[] valor);
    }
    
    protected <V extends Enum<V> & IEstructura> Entidad(Class<V> configuracion) {
        this.atributos = new LinkedHashMap<>();
        this.estructura = new LinkedHashMap<>();
        this.validaciones = new LinkedHashMap<>();
        this.estado = false;
        this.calculadora = new Calculadora();
        for (V item: (V[])configuracion.getEnumConstants()) {
            IValueObject definicion = new Estructura();
            String nombre = item.Nombre();
            definicion.set("nombre", nombre);
            definicion.set("filtro", item.Filtro());
            definicion.set("regla", item.Regla());
            definicion.set("min", item.Min());
            definicion.set("max", item.Max());
            definicion.set("e1", item.E1());
            definicion.set("e2", item.E2());
            definicion.set("e3", item.E3());
            definicion.set("v", item.V());
            definicion.set("d", item.D());
            definicion.set("etiqueta", item.Etiqueta());
            definicion.set("tipo", item.Tipo());
            this.estructura.put(nombre, definicion);
            if (item.Filtro().contains("A")) {
                String valor = "";
                if (!item.Inicial().isEmpty()) {valor = item.Inicial();}
                this.atributos.put(nombre, valor);
            }
        }
    }
    
    //IMPLEMENTACION DE DE INTERFACE "IValueObject"
    @Override public String get(String nombre) {
        String valor = "";
        if (this.atributos.containsKey(nombre)) {
            valor = this.atributos.get(nombre);
        }
        return valor;
    }
    @Override public Boolean set(String nombre, String valor) {
        Boolean resultado = false;
        if (this.atributos.containsKey(nombre)) {
            resultado = validar(this.estructura.get(nombre), valor);
            if (resultado) {this.atributos.put(nombre, valor);}
        }
        return resultado;
    }

    //IMPLEMENTACION DE INTERFACE "IEntidad"
    @Override public Boolean setAtributos(Map<String, String> datos) {
        Boolean resultado = true;
        if (null == datos) {resultado = false;}
        else {
            Map<String, String> auxiliar = new LinkedHashMap<>();
            for (Map.Entry<String, IValueObject> item: this.estructura.entrySet()) {
                String nombre = item.getKey();
                IValueObject definicion = item.getValue();
                if (definicion.get("filtro").contains("A")) {
                    if (datos.containsKey(nombre)) {
                        String valor = datos.get(nombre);
                        if (validar(this.estructura.get(nombre), valor)) {
                            auxiliar.put(nombre, valor);
                        } else {
                            resultado = false;
                        }
                    }
                }
            }
            if (resultado && auxiliar.size()>0) {
                for (Map.Entry<String, String> entry : auxiliar.entrySet()) {
                    this.atributos.put(entry.getKey(), entry.getValue());
                }
            } else {resultado = false;}
        }
        this.estado = resultado;
        return resultado;
    }
    @Override public Map<String, String> getAtributos() {return this.atributos;}
    @Override public Boolean getEstado() {return this.estado;}
    @Override public Map<String, String> getValidaciones() {return this.validaciones;}
    @Override public String getEntidad() {return this.getClass().getSimpleName();}
    @Override public Map<String, IValueObject> getEstructura() {
        return this.estructura;
    }
    @Override public Float Calcular(ICalculo calculo, float[] valor) {
        this.calculadora.setCalculo(calculo);
        return this.calculadora.getResultado(valor);
    }
    @Override public Map<String, String> TransformarDatos(Map<String, String> datos, String destino, String filtro, Boolean evaluar) {
        String valor, regla, v, d, nombre;
        Map<String, String> temporal = new LinkedHashMap<>();
        if (datos.isEmpty()) {
            this.estado = false;
            return temporal;
        } else {this.estado = true;}
        for (IValueObject item: this.estructura.values()) {
            v = item.get("v");
            d = item.get("d");
            valor = "";
            nombre = "";
            Boolean resultado_item = true;
            if (
                !filtro.isEmpty() && 
                (item.get("filtro").contains(filtro) || item.get("filtro").contains("C")) && 
                    (
                    (!d.isEmpty() && destino.equalsIgnoreCase("D")) || 
                    (!v.isEmpty() && destino.equalsIgnoreCase("V"))
                    )
                ) {
                regla = item.get("regla");
                if (destino.equalsIgnoreCase("D")) {
                    nombre = d;
                    valor = Funciones.textoNoNulo(datos.get(v));
                } else if (destino.equalsIgnoreCase("V")) {
                    nombre = v;
                    valor = Funciones.textoNoNulo(datos.get(d));
                }
                if (!regla.isEmpty() && !item.get("filtro").contains("C")) {
                    if (evaluar) {resultado_item = this.validar(item, valor);}
                    if (resultado_item) {valor = cambiarFormato(regla, valor, destino);}
                    else {this.estado = false;}
                }
                if (resultado_item && !nombre.isEmpty()) {
                    temporal.put(nombre, valor);
                    this.set(item.get("nombre"), valor);
                    //TRAZADOR.info("dato: " + nombre + " = " + valor);
                }
            }
        }
        if (this.estado) {
            int cuenta = 0;
            for (Map.Entry<String, IValueObject> atributo: this.estructura.entrySet()) {
                IValueObject item = atributo.getValue();
                if (destino.equalsIgnoreCase("V")) {nombre = item.get("d");}
                else {nombre = item.get("v");}
                valor = Funciones.textoNoNulo(datos.get(nombre));
                if (datos.containsKey(nombre)) {
                    if (item.get("filtro").contains("A")) {
                        this.atributos.put(atributo.getKey(), valor);
                        //TRAZADOR.info("atributo: " + atributo.getKey() + " = " + valor);
                        cuenta++;
                    }
                }
            }
            //TODO: Revisar
            if (cuenta == 0 && destino.equalsIgnoreCase("V")) {
                this.estado = false;
            }
        }
        return temporal;
    }
    @Override public void Vaciar() {
        for (Map.Entry<String, IValueObject> item: this.estructura.entrySet()) {
            String nombre = item.getKey();
            IValueObject definicion = item.getValue();
            if (definicion.get("filtro").contains("A")) {
                this.atributos.put(nombre, "");
            }
        }
        this.validaciones.clear();
        this.estado = false;
    }
    @Override public String entidadToString() {
        StringBuilder str = new StringBuilder();
        str.append("ENTIDAD.").append(this.getClass().getSimpleName()).append(", estado: ").append(this.estado.toString()).append("\n");
        for (Map.Entry<String, String> atributo: this.atributos.entrySet()) {
            str.append(atributo.getKey()).append("=").append(atributo.getValue()).append(" | ");
        }
        str.append("\n");
        return str.toString();
    }
    @Override public String validacionesToString() {
        StringBuilder str = new StringBuilder();
        str.append("VALIDACIONES (").append(this.validaciones.size()).append("):\n");
        for (Map.Entry<String, String> validacion: this.validaciones.entrySet()) {
            str.append(validacion.getKey()).append(": ").append(validacion.getValue()).append("\n");
        }
        str.append("\n");
        return str.toString();
    }
    @Override public String entidadToJson() {
        StringBuilder str = new StringBuilder();
        str.append("{\"").append(this.getClass().getSimpleName()).append("\":{");
        for (Map.Entry<String, String> atributo: this.atributos.entrySet()) {
            str.append("\"").append(atributo.getKey()).append("\":\"").append(atributo.getValue()).append("\", ");
        }
        String json = str.toString();
        json = json.substring(0, json.length()-2) + "}}\n";
        return json;
    }
    
    //FUNCIONES INTERNAS
    private Boolean validar(IValueObject item, String valor) {
        Boolean resultado = true;
        String regla = item.get("regla");
        String nombre = item.get("nombre");
        if (!regla.isEmpty()) {
            Integer min = Funciones.enteroNoNulo(item.get("min"));
            Integer max = Funciones.enteroNoNulo(item.get("max"));
            String inclus = REGLA.valueOf(regla).Inclusion();
            String expreg = REGLA.valueOf(regla).ExpRegular();
            if (min >0 || max >0) {
                if (!evaluarMinMax(valor, min, max, regla)) {
                    this.validaciones.put("ERROR-MINMAX-" + nombre, Funciones.reemplazarTexto(Funciones.reemplazarTexto(item.get("e2"), "[min]", min.toString()), "[max]", max.toString()));
                    resultado = false;
                }
                if (!inclus.isEmpty() && !evaluarInclusion(valor, inclus)) {
                    this.validaciones.put("ERROR-INCLUS-" + nombre, item.get("e1"));
                    resultado = false;
                }
                if (!expreg.isEmpty() && !evaluarExpRegular(valor, expreg)) {
                    this.validaciones.put("ERROR-EXPREG-" + nombre, item.get("e3"));
                    resultado = false;
                }
                if (regla.equalsIgnoreCase("RUT")) {
                    if (!evaluarRUT(valor)) {
                        this.validaciones.put("ERROR-RUT-" + nombre, item.get("e3"));
                        resultado = false;
                    }
                }
            }
        }
        return resultado;
    }
    private String cambiarFormato(String regla, String valor, String destino) {
        if (valor == null) {valor = "";}
        if (!regla.isEmpty() && !valor.isEmpty()) {
            String fv = REGLA.valueOf(regla).FormatoVista();
            String fd = REGLA.valueOf(regla).FormatoDatos();
            if (!fv.isEmpty() && !fd.isEmpty()) {
                if (regla.equalsIgnoreCase("FECHA") || regla.equalsIgnoreCase("HORA")) {
                    switch (destino) {
                        case "D": valor = Funciones.convertirFechaHora(valor, fv, fd);break;
                        case "V": valor = Funciones.convertirFechaHora(valor, fd, fv); break;
                    }
                }
            }
        }
        return valor.toString();
    }
    private boolean evaluarMinMax(String valor, Integer minimo, Integer maximo, String regla) {
        boolean resultado;
        if (null == valor) {valor = "";}
        if (regla.equals("ENTERO") || regla.equals("ID")) {
            Integer largo = Funciones.enteroNoNulo(valor);
            resultado = (largo <= maximo && largo >= minimo);
        } else {
            Integer largo2 = (Funciones.textoNoNulo(valor)).length();
            resultado = (largo2 <= maximo && largo2 >= minimo);
        }
        return resultado;
    }
    private boolean evaluarInclusion(String valor, String criterio) {
        boolean resultado = true;
        if (null == valor) {valor = "";}
        int largo = criterio.length();
        if (largo > 0) {
            for (int i = 0, max = valor.length(); i < max; i++) {
                int j;
                for (j = 0; j < largo; j++) {
                    if (valor.charAt(i) == criterio.charAt(j)) {
                        break;
                    }
                }
                if (j == largo) {
                    resultado = false;
                    break;
                }
            }
        }
        return resultado;
    }
    private boolean evaluarExpRegular(String valor, String criterio) {
        boolean resultado = true;
        if (null == valor) {valor = "";}
        if (!criterio.isEmpty() && !valor.isEmpty()) {
            Pattern pat = Pattern.compile(criterio);
            Matcher mat = pat.matcher(valor);
            if (!mat.matches()) {resultado = false;}
        }
        return resultado;
    }
    private boolean evaluarRUT(String valor) {
        //Basado en: http://cesarg.cl/valida-rut-chileno-java/
        boolean resultado;
        if (null == valor) {valor = "";}
        String[] rut = valor.split("-");
        if (rut.length==2) {
            Integer M = 0, S = 1, T = Integer.parseInt(rut[0]);
            for (;T!=0; T=(int) Math.floor(T/=10)) {S=(S+T%10*(9-M++%6))%11;}
            String dv = (S > 0) ? String.valueOf(S-1) : "k";
            resultado = dv.equalsIgnoreCase(rut[1].toLowerCase());
        } else {resultado = false;}
        return resultado;
    }
    
    //CLASES INTERNAS
    private class Calculadora {
        private ICalculo calculo;
        Calculadora() {}
        Calculadora(ICalculo calculo) {this.calculo = calculo;}
        public float getResultado(float[] valor) {
            return calculo.Calcular(valor);
        }
        public void setCalculo(ICalculo calculo) {
            this.calculo = calculo;
        }
    }
    private class Estructura implements IValueObject {
        private String nombre, filtro, regla, min, max, e1, e2, e3, v, d, etiqueta, tipo;
        Estructura() {}
        @Override public Boolean set(String nombre, String valor) {
            switch (nombre) {
                case "nombre": this.nombre = valor; break;
                case "filtro": this.filtro = valor; break;
                case "regla": this.regla = valor; break;
                case "min": this.min = valor; break;
                case "max": this.max = valor; break;
                case "e1": this.e1 = valor; break;
                case "e2": this.e2 = valor; break;
                case "e3": this.e3 = valor; break;
                case "v": this.v = valor; break;
                case "d": this.d = valor; break;
                case "etiqueta": this.etiqueta = valor; break;
                case "tipo": this.tipo = valor; break;
            }
            return true;
        }
        @Override public String get(String nombre) {
            String valor = "";
            switch (nombre) {
                case "nombre": valor = this.nombre; break;
                case "filtro": valor = this.filtro; break;
                case "regla": valor = this.regla; break;
                case "min": valor = this.min; break;
                case "max": valor = this.max; break;
                case "e1": valor = this.e1; break;
                case "e2": valor = this.e2; break;
                case "e3": valor = this.e3; break;
                case "v": valor = this.v; break;
                case "d": valor = this.d; break;
                case "etiqueta": valor = this.etiqueta; break;
                case "tipo": valor = this.tipo; break;
            }
            return Funciones.textoNoNulo(valor);
        }
    }
}