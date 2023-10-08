package com.example.LED;

import java.util.ArrayList;
import java.util.Arrays;

public class Estado {
    private String porta;
    private String ipv4;
    private boolean ligado;
    private String cor;
    private ArrayList<String> cores = new ArrayList<String>(Arrays.asList(
            "#FF0000",
            "#00FF00",
            "#0000FF",
            "#FFFF00",
            "#FF00FF",
            "#00FFFF",
            "#FFA500",
            "#800080",
            "#008000",
            "#FFC0CB",
            "#808080",
            "#800000",
            "#008080",
            "#A52A2A",
            "#000080",
            "#FFFFFF",
            "#000000",
            "#FFFFF0",
            "#F0E68C",
            "#D2691E"));

    public Estado(String porta, String ipv4, boolean ligado) {
        this.porta = porta;
        this.ipv4 = ipv4;
        this.ligado = ligado;
    }

    void setPorta(String porta) {
        this.porta = porta;
    }

    void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    // Seta somente cores que pertecem as possibilidades de cores;
    void setCor(String cor) {
        for (int i = 0; i < this.cores.size(); i++) {
            String currentColor = this.cores.get(i);
            if (currentColor.equals(cor)) {
                this.cor = currentColor;
                break;
            }
        }
    }

    public String getLigado() {
        if (this.ligado) {
            return "ligado";
        } else {
            return "desligado";
        }
    }

    public String getCor() {
        return this.cor;
    }

    public String getCores() {
 
        String[] array = new String[this.cores.size()];
        array = this.cores.toArray(array);

        StringBuilder result = new StringBuilder(array[0]);

        for (int i = 1; i < array.length; i++) {
            result.append(", ").append(array[i]);
        }

        return result.toString();
    }

    public int getPorta() {
        return Integer.parseInt(this.porta);
    }

    public String getAddress() {
        return this.ipv4 + ":" + this.porta;
    }
}