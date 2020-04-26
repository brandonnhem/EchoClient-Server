package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	
	private static String SOCKET_ADDRESS;
	private static int SOCKET_PORT;
	
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean validSocket = false;
        
        //try arguments if used
        if(args.length == 2) {
        	SOCKET_ADDRESS = args[0];
        	SOCKET_PORT = Integer.parseInt(args[1]);
        	validSocket = socketCheck();
        }
        
        //loop until a valid socket is used
        while(!validSocket) {
            System.out.print("Enter IP address: ");
            SOCKET_ADDRESS = input.readLine();
            System.out.print("Enter Port Number: ");
            SOCKET_PORT = Integer.parseInt(input.readLine());
            validSocket = socketCheck();
        }
        
        //create the socket
        socket = new Socket(SOCKET_ADDRESS, SOCKET_PORT);
        //set socket output stream
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //set socket input stream
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        String str;
        System.out.print("\nEnter Message: ");
        while((str = input.readLine()) != null) {
            out.println(str);
            System.out.println("Echo: " + in.readLine());
            if(str.equalsIgnoreCase("Bye")) {
            	break;
            } else {
            	System.out.print("\nEnter Message: ");
            }
        }
        
        System.out.println("\nEnding connection with server");
        //closing buffer streams
        out.close();
        in.close();
        input.close();
        //closing socket
        socket.close();
    }
    
    /* Test the socket address and socket port */
    public static boolean socketCheck() {
    	try {
    		Socket s = new Socket(SOCKET_ADDRESS, SOCKET_PORT);
    		s.close();
    		return true;
    	} catch (IOException e) {
    		System.out.println("Invalid IP address or port number.\n");
    	}
    	return false;
    }
}