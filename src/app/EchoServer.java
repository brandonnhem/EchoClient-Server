package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class EchoServer {
    public static int PORT;
    public static void main(String[] args) {
        if(args.length == 1){
            PORT = Integer.parseInt(args[0]);
        } else if(args.length > 1) {
            System.out.println("Expected one argument, received " + args.length);
            System.out.println("Exiting program..");
            System.exit(1);
        } else {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter port number for server: ");
                PORT = Integer.parseInt(input.readLine());
                while(PORT > 65535 || PORT < 1) {
                    System.out.print("Enter valid port number (1 - 65535) for server: ");
                    PORT = Integer.parseInt(input.readLine());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Exiting program..");
                System.exit(1);
            }
        }

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Echo server on port " + PORT + " successfully created.");
            System.out.println("Awaiting messages...");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting program..");
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
            System.out.println("Exiting program..");
            System.exit(1);
        }
    }    
}