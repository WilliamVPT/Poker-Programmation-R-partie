package edu.info0502.tp3;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost"; // Adresse du serveur
    private static final int SERVER_PORT = 1234; // Port du serveur

    public Client() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connecté au serveur.");
            System.out.println("Message du serveur : " + in.readLine());

            String message;
            while (true) {
                System.out.print("Entrez un message : ");
                message = userInput.readLine(); // Lire le message de l'utilisateur
                if (message.equalsIgnoreCase("exit")) {
                    break; // Sortir de la boucle si l'utilisateur tape "exit"
                }
                out.println(message); // Envoyer le message au serveur
                System.out.println("Réponse du serveur : " + in.readLine()); // Afficher la réponse
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
