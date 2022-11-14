package de.medieninformatik;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6000)) {
            //Streams
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            boolean suc = (Boolean) ois.readObject();
            if (!suc) {
                System.err.println("Host denied access.");
                return;
            }
            System.out.println("Connected to " + socket.getInetAddress().getHostName());

            //Eingabe für Client
            Scanner scanner = new Scanner(System.in);

            //Update Text Messages
            Thread read = new Thread(() -> {
                do {
                    try {
                        Message s = (Message) ois.readObject();
                        System.out.println(s);
                    } catch (IOException e) {
                        System.err.println("Host disconnected.");
                        break;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Unknown Input received.");
                        break;
                    }
                } while (true);
            });
            read.start();

            //Username des Clients
            String user = InetAddress.getLocalHost().toString();

            //Schreiben
            do {
                Message m = new Message(scanner.nextLine());
                oos.writeObject(m);
            } while (true);
        } catch (SocketException | UnknownHostException e) {
            System.err.println("Host disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Received wrong input.");
        }
    }
}
