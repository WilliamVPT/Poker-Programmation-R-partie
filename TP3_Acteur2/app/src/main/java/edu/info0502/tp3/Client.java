package edu.info0502.tp3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import edu.info0502.poker.*;

public class Client {
    private static final String SERVER_ADDRESS = "10.11.18.44"; // Adresse du serveur
    private static final int SERVER_PORT = 1234; // Port du serveur

    public Client() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                ObjectOutputStream outOb = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inOb = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Connecté au serveur.");

            // Lire la demande de pseudonyme du serveur
            String serverMessage = in.readLine();
            if (serverMessage == null) {
                System.out.println("Connexion fermée par le serveur.");
                return;
            }
            System.out.println("Message du serveur : " + serverMessage);

            // Envoyer le pseudonyme
            System.out.println("Envoie du pseudo ");
            String pseudo = "Remi"; // Lire le pseudonyme de l'utilisateur
            out.println(pseudo); // Envoyer le pseudonyme au serveur

            // Lire la réponse du serveur (acceptation ou refus)
            serverMessage = in.readLine();
            if (serverMessage == null) {
                System.out.println("Connexion fermée par le serveur.");
                return;
            }
            System.out.println("Message du serveur : " + serverMessage);

            // Si la connexion est refusée, quitter
            if (serverMessage.contains("refusée")) {
                return;
            }
            Main jeu = new Main();
            try {
                Object receivedObject = inOb.readObject();
                Main receivedMain = (Main) receivedObject;
                if (receivedMain instanceof Main) {
                    jeu = receivedMain;
                    System.out.println(" Jeu reçu : \n" + jeu.toString());
                } else {
                    System.out.println("Erreur : Objet inattendu reçu du serveur.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            Main Croupier = new Main();
            try {
                Object receivedObject = inOb.readObject();
                Main receivedMain = (Main) receivedObject;
                if (receivedMain instanceof Main) {
                    Croupier = receivedMain;
                    System.out.println("Jeu du croupier : \n" + Croupier.toString());
                } else {
                    System.out.println("Erreur : Objet inattendu reçu du serveur.");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Carte[] cartesCroupier = Croupier.getMain();

            List<Carte[]> combinaisons = jeu.getAllCombinations(cartesCroupier);
            Carte[] meilleureCombinaison = combinaisons.get(0); // On part du principe que la première est la plus forte

            // Comparer chaque combinaison pour trouver la plus forte
            for (int j = 1; j < combinaisons.size(); j++) {
                Carte[] combinaisonActuelle = combinaisons.get(j);

                // Créer des objets `Main` temporaires pour utiliser `estPlusForte`
                Main mainTemp1 = new Main(meilleureCombinaison);
                Main mainTemp2 = new Main(combinaisonActuelle);

                // Si la combinaison actuelle est plus forte, on met à jour la meilleure
                // combinaison
                if (mainTemp2.estPlusForte(mainTemp1)) {
                    meilleureCombinaison = combinaisonActuelle;
                }
            }
            Main meilleurcombi = new Main(meilleureCombinaison);
            System.out.println("Meilleur conbinaison : \n" + meilleurcombi.toString());

            outOb.writeObject(meilleurcombi);
            outOb.flush();
            System.out.println("Meilleur combinaison envoyé au serveur.");

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
