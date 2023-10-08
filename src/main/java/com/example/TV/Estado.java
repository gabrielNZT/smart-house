package com.example.TV;

public class Estado {
    private boolean ligado;
    private String canal;
    private int volume;

    public Estado(boolean ligado, String canal, int volume) {
        this.ligado = ligado;
        this.canal = canal;
        this.volume = volume;
    }

    public String getLigado() {
        if (ligado) {
            return "Ligado";
        } else {
            return "Desligado";
        }
    }

    public void setLigado(boolean ligado) {
        this.ligado = ligado;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getVolume() {
        return String.valueOf(volume);
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
