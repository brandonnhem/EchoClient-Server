package app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class EchoServer {
    public static void main(String[] args) {
        int port = 6789;

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            System.out.println("UDP Echo server on port " + port + " successfully created.");
            System.out.println("Awaiting messages...");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            byte buffer [] = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            String input;
            while(true) {
                socket.receive(datagramPacket);
                input = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println("Received from server: " + input);
                socket.send(datagramPacket);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }    
}