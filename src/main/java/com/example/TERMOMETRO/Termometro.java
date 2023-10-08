package com.example.TERMOMETRO;

import java.net.*;
import java.util.Random;

public class Termometro {
    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        
        try {
            serverSocket = new DatagramSocket(9876);
            System.out.println("Servidor UDP do Termômetro esperando por conexões...");
            
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                
                double temperatura = lerTemperatura();
                
                String mensagem = "Temperatura: " + temperatura + "°C";
                byte[] sendData = mensagem.getBytes();
                
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                System.out.println("Enviando temperatura para " + clientAddress + ":" + clientPort);
                
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
    
    private static double lerTemperatura() {
        Random random = new Random();
        return 20 + random.nextDouble() * 10; 
    }
}
