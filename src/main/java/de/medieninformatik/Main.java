package de.medieninformatik;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.43.26", 6000)) {
            //Streams
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to " + socket.getInetAddress().getHostName());

            //Eingabe für Client
            Scanner scanner = new Scanner(System.in);

            //Update Text Messages
            Thread read = new Thread(() -> {
                do {
                    try {
                        String s = (String) ois.readObject();
                        System.out.println(s);
                    } catch (IOException e) {
                        System.err.println("Host disconnected.");
                        e.printStackTrace();
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Host disconnected.");
                        e.printStackTrace();
                        break;
                    }
                } while (true);
            });
            read.start();

            //Username des Clients
            String user = InetAddress.getLocalHost().toString();

            //Schreiben
            do {
                String text = scanner.nextLine();
                oos.writeObject(user + ": " + text);
            } while (true);
        } catch (SocketException | UnknownHostException e) {
            System.err.println("Host disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
