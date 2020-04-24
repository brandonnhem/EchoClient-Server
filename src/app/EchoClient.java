package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        if(args.length == 0) {
            Scanner keyboard = new Scanner(System.in);
            System.out.print("Enter IP address: ");
            String ip = keyboard.nextLine();
            System.out.println();
            System.out.print("Enter Port Number: ");
            int portNumber = keyboard.nextInt();
            System.out.println();
            socket = new Socket(ip, portNumber);
        } else {
            socket = new Socket(args[0], Integer.parseInt(args[1]));
        }
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String str;
        while((str = input.readLine()) != null) {
            out.println(str);
            System.out.println("Echo: " + str);
        }
    }
}