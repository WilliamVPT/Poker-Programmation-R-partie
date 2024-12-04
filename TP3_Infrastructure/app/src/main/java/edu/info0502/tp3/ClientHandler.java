package edu.info0502.tp3;

import java.io.*;
import java.net.Socket;

import edu.info0502.poker.*;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String pseudo;
    private static Paquet paquet = new Paquet();
    private static Talon talon = new Talon(3);
    private static Main jeu = new Main(talon, 2);
    private static Main Croupier = new Main(talon, 5);
    private static Main MeilleurCombi = new Main();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public Main getMeilleurCombi() {
        return MeilleurCombi;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                ObjectOutputStream outOb = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inOb = new ObjectInputStream(clientSocket.getInputStream())) {
            // Demande le pseudonyme au client
            out.println("Entrez votre pseudonyme : ");
            pseudo = in.readLine();

            // Vérifie si le pseudonyme est unique via la méthode connexion
            while (!Serveur.Connexion(pseudo)) {
                out.println("Erreur : Pseudonyme déjà utilisé. Déconnecté.");
                out.println("Entrez votre pseudonyme : ");
                pseudo = in.readLine();
            }

            out.println("Bienvenue sur le serveur, " + pseudo + "!");
            for (int i = 0; i < 2; i++) {
                jeu.distribuerCarte(i, talon);
            }
            outOb.writeObject(jeu);
            outOb.flush();
            System.out.println("Objet Main envoyé au client.");

            outOb.writeObject(Croupier);
            outOb.flush();
            System.out.println("Croupier envoyé au client.");

            try {
                Object receivedObject = inOb.readObject();
                Main receivedMain = (Main) receivedObject;
                if (receivedMain instanceof Main) {
                    MeilleurCombi = receivedMain;
                    System.out.println("Meilleur combi reçue pour le joueur : " + pseudo);
                    Serveur.StartGame(); // Vérifie si toutes les combinaisons sont prêtes à ce moment
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("Erreur de connexion avec le client.");
            e.printStackTrace();
        } finally {
            // Appelle la méthode deconnexion pour libérer le pseudonyme
            if (pseudo != null) {
                Serveur.Deconnexion(pseudo);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
