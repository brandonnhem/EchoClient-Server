package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public class EchoClient {
	
	private static InetAddress ADDRESS; //address for socket
	private static int PORT; //port for socket
	public static boolean active; //continue to receive flag
	
	/* UDP receiver class used once connected for echo */
    public static class UDPEchoReader extends Thread {
    	
        public DatagramSocket datagramSocket; //socket for receiving
    	
        /* UDPEchoReader constructor */
        public UDPEchoReader(DatagramSocket socket) {
            datagramSocket = socket;
            active = true;
        }
        
        public void run() {
            byte[] buffer = new byte[1024];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            String receivedString;
            while(active) {
                try {
                	System.out.print("Enter a message to send: ");
                    // listen for incoming datagram packet
                    datagramSocket.receive(incoming);
                    // print out received string
                    receivedString = new String(incoming.getData(), 0, incoming.getLength());
                    System.out.println("Received from server: " + receivedString + "\n");
                } catch(IOException e) {
                    System.out.println(e);
                    System.exit(1);
                    active = false;
                }
            }
            System.out.println("Disconnected from " + ADDRESS.toString() + " on port number " + PORT);
        }
    }

    public static void main(String[] args) {

        DatagramSocket socket = null; //socket for sending
        BufferedReader keyboard = null; //user input reader

        try {
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            if(args.length == 2) {
                ADDRESS = InetAddress.getByName(args[0]);
                PORT = Integer.parseInt(args[1]);
            } else {
                String userInput;
                System.out.print("Enter IP Address: ");
                userInput = keyboard.readLine();
                ADDRESS = InetAddress.getByName(userInput);
                System.out.print("Enter port number: ");
                userInput = keyboard.readLine();
                PORT = Integer.parseInt(userInput);
            }
            socket = new DatagramSocket();
            SocketAddress sockaddr = new InetSocketAddress(ADDRESS, PORT);
            socket.connect(sockaddr);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
        	System.out.println(e.getMessage());
            System.exit(1);
        }

        UDPEchoReader reader = new UDPEchoReader(socket);
        reader.setDaemon(true);
        reader.start();

        System.out.println("\nConnected to " + ADDRESS.toString() + " on port number " + PORT);
        System.out.println("Input [bye] to disconnect at any time");
        System.out.println("Ready to send messages...\n");

        try {
            String input;
            while(true) {
                input = keyboard.readLine();
                DatagramPacket datagramPacket = new DatagramPacket(input.getBytes(), input.length(), ADDRESS, PORT);
                socket.send(datagramPacket);
                if(input.equalsIgnoreCase("bye")) {
                	active = false;
                	break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        //disconnect and close socket
        socket.disconnect();
        socket.close();
    }
}