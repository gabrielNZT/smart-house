package com.example.LED;

import java.io.*;
import java.net.*;

import com.example.ComunicationObject;

class Led {
	public static void main(String argv[]) {
		ServerSocket listenSocket;

		Estado estado = new Estado(null, null, false);
		try {
			listenSocket = new ServerSocket(6789);
			while (true) {
				Socket connectionSocket = listenSocket.accept();
				ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
				ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
				ComunicationObject p = (ComunicationObject) inFromClient.readObject();

				if ("dispositivo".equals(p.metodo)) {
					p.response = "dispositivo: led/porta: 6789/addres: localhost";
					outToClient.writeObject(p);
				} else if ("getLigado".equals(p.metodo)) {
					p.response = estado.getLigado();
					outToClient.writeObject(p);
				} else if ("setLigado".equals(p.metodo)) {
					boolean newLigado = p.payload.equals("ligar");
					estado.setLigado(newLigado);
					p.response = estado.getLigado();
					outToClient.writeObject(p);
				} else if ("getCor".equals(p.metodo)) {
					p.response = estado.getCor();
					outToClient.writeObject(p);
				} else if ("getCores".equals(p.metodo)) {
					p.response = estado.getCores();
					outToClient.writeObject(p);
				} else if ("setCor".equals(p.metodo)) {
					String payload = p.payload;
					if (payload.length() == 0) {
						p.response = "Requisicao invalida";
						outToClient.writeObject(p);
					}

					if (estado.getLigado().equals("ligado")) {
						estado.setCor(payload);
						p.response = estado.getCor();
						outToClient.writeObject(p);
					} else {
						p.response = "O led está desligado";
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