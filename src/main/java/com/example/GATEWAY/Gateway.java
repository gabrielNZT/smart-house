package com.example.GATEWAY;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.ComunicationObject;

class UDPCommunicationThread extends Thread {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;

    public UDPCommunicationThread(DatagramSocket socket, InetAddress serverAddress, int serverPort) {
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String temperatura = comunicarComServidorUDP(socket, serverAddress, serverPort);
                System.out.println("Temperatura recebida: " + temperatura);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String comunicarComServidorUDP(DatagramSocket socket, InetAddress serverAddress, int serverPort)
            throws IOException {
        byte[] sendData = new byte[1024];
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        socket.send(sendPacket);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }
}

class TCPCommunicationThread extends Thread {
    private Scanner scanner;
    private int serverPort;
    private String dispositivo;

    public TCPCommunicationThread(Scanner scanner, int serverPort, String dispositivo) {
        this.scanner = scanner;
        this.serverPort = serverPort;
        this.dispositivo = dispositivo;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = new Socket("localhost", serverPort);
                String response = comunicarComServidorTCP(socket, scanner, dispositivo);
                JOptionPane.showMessageDialog(null, "Resposta " + dispositivo + ": " + response);
                Thread.sleep(15000);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String comunicarComServidorTCP(Socket socket, Scanner scanner, String dispositivo)
            throws IOException, ClassNotFoundException {
        ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField metodoField = new JTextField(20);
        panel.add(new JLabel("Método:"));
        panel.add(metodoField);

        JTextField payloadField = new JTextField(20);
        panel.add(new JLabel("Payload:"));
        panel.add(payloadField);

        int result = JOptionPane.showConfirmDialog(null, panel, dispositivo,
                JOptionPane.OK_CANCEL_OPTION);

        String metodo = null;
        String payload = null;
        if (result == JOptionPane.OK_OPTION) {
            metodo = metodoField.getText();
            payload = payloadField.getText();
        }

        ComunicationObject p1 = new ComunicationObject(metodo, payload, null);

        outToServer.writeObject(p1);
        ComunicationObject p2 = (ComunicationObject) inFromServer.readObject();
        return p2.response;
    }
}

public class Gateway {
    public static void main(String argv[]) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        try {
            DatagramSocket termometroSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int udpServerPort = 9876;
            int tcpServerPort1 = 6789;
            int tcpServerPort2 = 6589;

            UDPCommunicationThread udpThread = new UDPCommunicationThread(termometroSocket, serverAddress,
                    udpServerPort);

            TCPCommunicationThread tcpThread1 = new TCPCommunicationThread(scanner, tcpServerPort1, "LED");
            TCPCommunicationThread tcpThread2 = new TCPCommunicationThread(scanner, tcpServerPort2, "TV");

            udpThread.start();
            tcpThread1.start();
            tcpThread2.start();

            udpThread.join();
            tcpThread1.join();
            tcpThread2.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
