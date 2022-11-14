package de.medieninformatik;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.178.26", 6000)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to "+socket.getInetAddress().getHostName());

            Scanner scanner = new Scanner(System.in);
            do {
                String text = scanner.nextLine();
                oos.writeObject(text);
                String received = (String) ois.readObject();
                System.out.println(received);
            } while (true);
        } catch (SocketException | UnknownHostException e){
            System.err.println("Host has given up this conversation.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
