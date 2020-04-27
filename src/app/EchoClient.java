package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class EchoClient {
    public static class UDPEchoReader extends Thread { // Extending Thread allows for infinte loops to occur
        public boolean active;
        public DatagramSocket datagramSocket;

        public UDPEchoReader(DatagramSocket socket) {
            datagramSocket = socket;
            active = true;
        }
        
        public void run() {
            byte[] buffer = new byte[1024]; // used to store incoming data
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length); // incoming data packet
            String receivedString;
            while(active) {
                try {
                    // listen for incoming datagram packet
                    datagramSocket.receive(incoming);
                    // print out received string
                    receivedString = new String(incoming.getData(),
                    0, incoming.getLength());
                    System.out.println("Received from server: " + receivedString + "\n");
                } catch(IOException e) {
                    System.out.println(e);
                    active = false;
                }
            }
        }
    }

    private static int PORT; // port number

    public static void main(String[] args) {
        InetAddress address = null; // address, can be "localhost" or actual IP address

        DatagramSocket socket = null;
        BufferedReader keyboard = null; // used to read input

        try {
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            if(args.length == 2) {
                address = InetAddress.getByName(args[0]);
                PORT = Integer.parseInt(args[1]);
            } else if(args.length == 0) {
                String userInput;
                System.out.print("Enter IP Address: ");
                userInput = keyboard.readLine();
                address = InetAddress.getByName(userInput);
                System.out.print("Enter port number: ");
                userInput = keyboard.readLine();
                PORT = Integer.parseInt(userInput);
            } else {
                System.out.println("Expected 2 arguments, received " + args.length);
                System.out.println("Exiting program..");
                System.exit(1);
            }
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting program..");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting program..");
            System.exit(1);
        }

        UDPEchoReader reader = new UDPEchoReader(socket);
        reader.setDaemon(true); // sets thread to be a daemon thread
        reader.start(); // starts execution of Thread

        System.out.println("Connected to " + address.toString() + " on port number " + PORT);
        System.out.println("Ready to send messages...");

        try {
            String input;
            while(true) {
                input = keyboard.readLine();
                DatagramPacket datagramPacket = new DatagramPacket(input.getBytes(), input.length(), address, PORT);
                socket.send(datagramPacket);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting program..");
            System.exit(1);
        }
    }
}