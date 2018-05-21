package interno.nucleo;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import java.text.SimpleDateFormat;
import java.util.Date;

public class Funciones {
    public static String reemplazarTexto(String cadena, String buscar, String reemplazar) {
        StringBuilder sb = null;
        int start = 0;
        for (int i; (i = cadena.indexOf(buscar, start)) != -1;) {
            if (sb == null) {
                sb = new StringBuilder();
            }
            sb.append(cadena, start, i);
            sb.append(reemplazar);
            start = i + buscar.length();
        }
        if (sb == null) {
            return cadena;
        }
        sb.append(cadena, start, cadena.length());
        return sb.toString();
    }
    public static String reemplazarComillas(String cadena) {
        if (cadena.contains("'")) {
            StringBuilder sb = null;
            int start = 0;
            for (int i; (i = cadena.indexOf("'", start)) != -1;) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(cadena, start, i);
                sb.append("''");
                start = i + 1;
            }
            if (sb == null) {
                return cadena;
            } else {
                sb.append(cadena, start, cadena.length());
                return sb.toString();
            }
        } else {
            return cadena;
        }
    }
    public static String unirTextos(String[] lista, String separador) {
        StringBuilder str = new StringBuilder();
        for (int i = 0, largo = lista.length; i < largo; i++) {
            if (lista[i] != null) {
                if (i > 0) {
                    str.append(separador);
                }
                str.append(lista[i]);
            }
        }
        return str.toString();
    }
    public static String textoNoNulo(String valor) {
        String cadena = "";
        if (valor != null) {
            cadena = valor;
        }
        if (cadena.equalsIgnoreCase("null")) {
            cadena = "";
        }
        return cadena;
    }
    public static String convertirFechaHora(String valor, String ini, String fin) {
        String resultado = valor;
        SimpleDateFormat df;
        SimpleDateFormat di;
        Date fe;
        try {
            if (valor.equalsIgnoreCase("ahora")) {
                df = new SimpleDateFormat(fin);
                resultado = df.format(new Date());
            } else if (valor.length() > 0) {
                di = new SimpleDateFormat(ini);
                df = new SimpleDateFormat(fin);
                fe = di.parse(valor);
                resultado = df.format(fe);
            }
        } catch (Exception e) {}
        return resultado;
    }
    public static Integer enteroNoNulo(String valor) {
        Integer numero = 0;
        if (valor.length() > 0) {
            for (int i = 0; i < valor.length(); i++) {
                if (!Character.isDigit(valor.charAt(i))) {
                    return numero;
                }
            }
            try {
                numero = Integer.parseInt(valor);
            } catch (Exception e) {}
        }
        return numero;
    }
}
