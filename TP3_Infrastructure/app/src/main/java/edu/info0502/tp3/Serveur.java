package edu.info0502.tp3;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.info0502.poker.*;

public class Serveur {
    private static final int PORT = 1234;
    private static Set<String> pseudos = new HashSet<>(); // Ensemble pour stocker les pseudonymes des clients
    private static int nb_joueurs = 0;
    private static Paquet paquet = new Paquet();
    private static Talon talon = new Talon(3);
    private static Main[] Joueurs = new Main[4];
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static boolean gameStarted = false;
    private static boolean running = true; // Indique si le serveur est actif
    private ServerSocket serverSocket;

    public Serveur() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur en attente de connexions...");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connecté : " + clientSocket.getInetAddress());

                    // Démarre un nouveau thread pour gérer chaque client
                    ClientHandler handler = new ClientHandler(clientSocket);
                    clientHandlers.add(handler);
                    new Thread(handler).start();
                    nb_joueurs++;
                } catch (SocketException e) {
                    if (!running) {
                        System.out.println("Le serveur a été arrêté.");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopServer(); // Assurez-vous que le serveur est correctement arrêté
        }
    }

    // Méthode pour arrêter le serveur
    public void stopServer() {
        System.out.println("Arrêt du serveur...");
        running = false;

        // Ferme le ServerSocket
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Serveur arrêté.");
    }

    // Méthode pour gérer la connexion d'un client
    public static boolean Connexion(String pseudo) {
        synchronized (pseudos) {
            if (pseudos.contains(pseudo)) {
                return false; // Pseudonyme déjà pris
            } else {
                pseudos.add(pseudo); // Ajoute le pseudonyme
                return true;
            }
        }
    }

    // Méthode pour gérer la déconnexion d'un client
    public static void Deconnexion(String pseudo) {
        synchronized (pseudos) {
            pseudos.remove(pseudo);
            nb_joueurs--;
        }
    }

    public static void StartGame() {
        if (!gameStarted && clientHandlers.size() >= 2 && toutesLesCombisRecues()) {
            gameStarted = true; // Marque le jeu comme démarré
            List<Main> meilleurCombi = getMeilleuresCombis();
            int JV = 0;
            for (int i = 1; i < meilleurCombi.size(); i++) {
                Main maintemp1 = meilleurCombi.get(i);
                Main maintemp2 = meilleurCombi.get(JV);

                if (maintemp1.estPlusForte(maintemp2)) {
                    JV = i;
                }
            }

            Main JoueurGagnant = meilleurCombi.get(JV);
            System.out.println("Victoire joueur " + (JV + 1) + " avec : \n" + JoueurGagnant.toString());
        } else if (!gameStarted) {
            System.out.println("Pas assez de combinaisons ou de joueurs pour commencer.");
        }
        
    }

    private static boolean toutesLesCombisRecues() {
        for (ClientHandler handler : clientHandlers) {
            if (handler.getMeilleurCombi() == null) {
                return false;
            }
        }
        return true;
    }

    public static List<Main> getMeilleuresCombis() {
        List<Main> meilleuresCombis = new ArrayList<>();
        for (ClientHandler handler : clientHandlers) {
            meilleuresCombis.add(handler.getMeilleurCombi());
        }
        return meilleuresCombis;
    }
}
