package de.medieninformatik;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6000)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to " + socket.getInetAddress().getHostName());

            Scanner scanner = new Scanner(System.in);

            Thread read = new Thread(() -> {
                do {
                    try {
                        String s = (String) ois.readObject();
                        System.out.println(s);
                    } catch (IOException e) {
                        System.err.println("Host has given up this conversation.");
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Host has given up this conversation.");
                        break;
                    }
                } while (true);
            });

            read.start();
            String user = InetAddress.getLocalHost().toString();
            do {
                String text = scanner.nextLine();
                oos.writeObject(user + ": " + text);
            } while (true);
        } catch (SocketException | UnknownHostException e) {
            System.err.println("Host has given up this conversation.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
