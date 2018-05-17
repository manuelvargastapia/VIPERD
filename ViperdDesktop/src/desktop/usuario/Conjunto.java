package desktop.usuario;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */


import desktop.usuario.Interfaces.IConjunto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

//CLASE Conjunto
public class Conjunto extends AbstractTableModel implements IConjunto {
    protected final static Logger TRAZADOR = Logger.getLogger(Conjunto.class.getName());

    protected Object oyente;
    protected List<Map<String, String>> conjunto;
    protected String[] columnas;

    public Conjunto(Object oyente) {
        TRAZADOR.info("");
        this.oyente = oyente;
        this.conjunto = new ArrayList<>();
    }
    
    //IMPLEMENTACION DE INTERFACE "IConjunto"
    @Override public void setConjunto(List<Map<String, String>> conjunto) {
        if (null != conjunto) {
            this.conjunto = conjunto;
            int num_columnas = getColumnCount();
            if (num_columnas > 0) {
                this.columnas = new String[num_columnas];
                int i = 0;
                for (Map.Entry<String, String> columna: this.conjunto.get(0).entrySet()) {
                    this.columnas[i] = columna.getKey();
                    i++;
                }
            }
        }
        this.fireTableStructureChanged();
        this.fireTableDataChanged();
    }
    
    //IMPLEMENTACION ESPECIFICA DE "AbstractTableModel"
    @Override public int getRowCount() {
        int total = 0;
        if (!this.conjunto.isEmpty()) {total = this.conjunto.size();}
        return total;
    }
    @Override public int getColumnCount() {
        int total = 0;
        if (!this.conjunto.isEmpty()) {total = this.conjunto.get(0).size();}
        return total;
    }
    @Override public String getColumnName(int i) {
        if (null != this.columnas && i < this.columnas.length) {return this.columnas[i];}
        return "";
    }
    @Override public Object getValueAt(int i, int i1) {
        String valor = "";
        if (this.conjunto.size() > i) {
            if (this.conjunto.get(i).size() > i1) {
                String[] aux = this.conjunto.get(i).values().toArray(new String[i1]);
                valor = aux[i1];
            }
        }
        return valor;
    }
    @Override public Class<?> getColumnClass(int i) {return String.class;}
    @Override public boolean isCellEditable(int i, int i1) {return false;}
}