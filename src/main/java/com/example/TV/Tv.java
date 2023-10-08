package com.example.TV;

import java.io.*;
import java.net.*;

import com.example.ComunicationObject;

class Tv {
    public static void main(String argv[]) {
        ServerSocket listenSocket;

        Estado estado = new Estado(false, "Canal 1", 50);

        try {
            listenSocket = new ServerSocket(6589);
            while (true) {
                Socket connectionSocket = listenSocket.accept();
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ComunicationObject p = (ComunicationObject) inFromClient.readObject();

                if ("dispositivo".equals(p.metodo)) {
                    p.response = "dispositivo: tv/porta: 6789/endereco: localhost";
                    outToClient.writeObject(p);
                } else if ("getLigado".equals(p.metodo)) {
                    p.response = estado.getLigado();
                    outToClient.writeObject(p);
                } else if ("setLigado".equals(p.metodo)) {
                    boolean newLigado = p.payload.equals("ligar");
                    estado.setLigado(newLigado);
                    p.response = estado.getLigado();
                    outToClient.writeObject(p);
                } else if ("getCanal".equals(p.metodo)) {
                    p.response = estado.getCanal();
                    outToClient.writeObject(p);
                } else if ("setCanal".equals(p.metodo)) {
                    if (estado.getLigado().equals("Ligado")) {
                        String payload = p.payload;
                        if (payload.length() == 0) {
                            p.response = "Requisicao invalida";
                            outToClient.writeObject(p);
                        }

                        estado.setCanal(payload);
                        p.response = estado.getCanal();
                        outToClient.writeObject(p);
                    } else {
                        p.response = "A tv está desligada";
                        outToClient.writeObject(p);
                    }
                } else if ("getVolume".equals(p.metodo)) {
                    p.response = estado.getVolume();
                    outToClient.writeObject(p);
                } else if ("setVolume".equals(p.metodo)) {
                    if (estado.getLigado().equals("Ligado")) {
                        if (p.payload.length() == 0) {
                            p.response = "Requisicao invalida";
                            outToClient.writeObject(p);
                        }
                        try {
                            int newVolume = Integer.parseInt(p.payload);
                            estado.setVolume(newVolume);
                            p.response = estado.getVolume();
                        } catch (NumberFormatException e) {
                            p.response = "Valor do volume inválido";
                        }
                        outToClient.writeObject(p);
                    } else {
                        p.response = "A tv está desligada";
                        outToClient.writeObject(p);
                    }

                } else {
                    p.response = "Requisicao invalida";
                    outToClient.writeObject(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}