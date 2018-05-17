package desktop.usuario;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Interfaces {
    interface IRuteador {
        static Map<String, String> procesarSolicitud(String[] args) {return null;};
        static void Despachar(Map<String, String> variables) {};
    }
    interface ISesion {
        static ISesion Abrir() {return null;};
        static void setEntorno(String nombre, String valor) {};
        static String getEntorno(String nombre) {return null;};
        static Map<String, String> Entorno() {return null;};
        static void setFechaHora(Date fecha) {};
        static void Cerrar() {};
    }
    interface IContenedor {
        IComponente agregarComponente(String nombre, Object componente, Enum tipo);
        IComponente getComponente(String nombre);
        Map<String, IComponente> getComponentes();
    }
    interface IComponente {
        String getNombre();
        void setVisible(Boolean valor);
        void setActivo(Boolean valor);
        void setEnfocado();
        void setValor(String valor);
        String getValor();
    }
    interface IConjunto {
        void setConjunto(List<Map<String, String>> conjunto);
    }
    interface IMenu {
        String ID();
        String Etiqueta();
        String Imagen();
        String Antes();
        String Tipo();
        String Visible();
        String Accion();
        String Permisos();
    }
    interface IItem {
        void setItem(String nombre, String valor);
        String getItem(String nombre);
    }
    interface IModelo {
        String Nombre();
        String Tipo();
        String Texto();
        String Imagen();
        String Visible();
        String Formato();
    }
}
