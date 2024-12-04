package edu.info0502.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.C;

public class Main {
    private Carte[] main;
    private Talon talon;
    private int nb_carte;

    public Main() {
        nb_carte = 5;
        main = new Carte[nb_carte];
        talon = new Talon();
        talon.melanger();
        for (int i = 0; i < nb_carte; i++) {
            main[i] = talon.tirerCarte();
        }
    }

    public Main(Talon tal, int nb) {
        nb_carte = nb;
        main = new Carte[nb_carte];
        tal.melanger();
        for (int i = 0; i < nb_carte; i++) {
            main[i] = tal.tirerCarte();
        }
    }

    public Main(Carte[] combi){
        nb_carte = 5;
        main = new Carte[nb_carte];
        for(int i = 0; i< nb_carte; i++){
            main[i] = combi[i];
        }
    }

    public Carte[] getMain() {
        return main;
    }

    public String toString() {
        String s = "Main : ";
        for (Carte carte : main) {
            s += carte.toString() + "\n";
        }
        s += this.getCombinaison();
        return s;
    }

    public void distribuerCarte(int ind, Talon tal) {
        main[ind] = tal.tirerCarte();
    }

    public Combinaison getCombinaison() {
        Arrays.sort(main, (a, b) -> a.getValeur() - b.getValeur());

        boolean estQuinte = estQuinte();
        boolean estCouleur = estCouleur();

        if (estQuinte && estCouleur && main[0].getValeur() == 10)
            return Combinaison.Quinte_Flush_Royale;
        if (estQuinte && estCouleur)
            return Combinaison.Quinte_Flush;
        if (estCarre())
            return Combinaison.Carré;
        if (estFull())
            return Combinaison.Full;
        if (estCouleur)
            return Combinaison.Couleur;
        if (estQuinte)
            return Combinaison.Quinte;
        if (estBrelan())
            return Combinaison.Brelan;
        if (estDoublePaire())
            return Combinaison.Deux_Paires;
        if (estPaire())
            return Combinaison.Paire;
        return Combinaison.Carte_Hautes;
    }

    private boolean estCouleur() {
        Couleur couleur = main[0].getCouleur();
        for (Carte carte : main) {
            if (carte.getCouleur() != couleur)
                return false;
        }
        return true;
    }

    private boolean estQuinte() {
        for (int i = 1; i < main.length; i++) {
            if (main[i].getValeur() != main[i - 1].getValeur() + 1)
                return false;
        }
        return true;
    }

    private boolean estPaire() {
        return compterPaires() == 1;
    }

    private boolean estDoublePaire() {
        return compterPaires() == 2;
    }

    private boolean estBrelan() {
        return compterOccurrences().containsValue(3);
    }

    private boolean estFull() {
        Map<Integer, Integer> valeurs = compterOccurrences();
        return valeurs.containsValue(3) && valeurs.containsValue(2);
    }

    private boolean estCarre() {
        return compterOccurrences().containsValue(4);
    }

    private Map<Integer, Integer> compterOccurrences() {
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (Carte carte : main) {
            occurrences.put(carte.getValeur(), occurrences.getOrDefault(carte.getValeur(), 0) + 1);
        }
        return occurrences;
    }

    private int compterPaires() {
        int paires = 0;
        for (int count : compterOccurrences().values()) {
            if (count == 2) {
                paires++;
            }
        }
        return paires;
    }

    public boolean estPlusForte(Main m2) {
        Combinaison comb1 = this.getCombinaison();
        Combinaison comb2 = m2.getCombinaison();

        // Comparer les combinaisons par leur ordre
        if (comb1.ordinal() < comb2.ordinal())
            return true; // La main actuelle est plus forte
        else if (comb1.ordinal() > comb2.ordinal())
            return false; // La main m2 est plus forte

        // Si les combinaisons sont identiques, on compare les cartes (kickeurs)
        else if (comb1.ordinal() == comb2.ordinal()) {
            if (comb1 == Combinaison.Paire || comb1 == Combinaison.Deux_Paires) {
                Map<Integer, Integer> occurrences1 = this.compterOccurrences();
                Map<Integer, Integer> occurrences2 = m2.compterOccurrences();

                // Trouver les valeurs des paires dans chaque main
                List<Integer> paires1 = getPaires(occurrences1);
                List<Integer> paires2 = getPaires(occurrences2);

                // Trier les valeurs des paires en ordre décroissant pour comparer la paire la
                // plus haute d'abord
                Collections.sort(paires1, Collections.reverseOrder());
                Collections.sort(paires2, Collections.reverseOrder());

                // Comparer les paires
                for (int i = 0; i < Math.min(paires1.size(), paires2.size()); i++) {
                    if (paires1.get(i) > paires2.get(i)) {
                        return true; // La main actuelle a la paire la plus forte
                    } else if (paires1.get(i) < paires2.get(i)) {
                        return false; // La main m2 a la paire la plus forte
                    }
                }
            }

            // Comparer les cartes hautes si les combinaisons sont identiques
            if (comb1 == Combinaison.Carte_Hautes) {
                return compareCartesHautes(this.main, m2.main);
            }

            // Si la combinaison est plus complexe, comme Brelan, Full, Quinte, etc.
            // Comparer les cartes restantes (kickeurs)
            return compareKickers(this.main, m2.main);
        }

        return false; // Par défaut, si aucune condition n'est satisfaite
    }

    private List<Integer> getPaires(Map<Integer, Integer> occurrences) {
        List<Integer> paires = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            if (entry.getValue() == 2) {
                paires.add(entry.getKey());
            }
        }
        return paires;
    }

    private boolean compareKickers(Carte[] main1, Carte[] main2) {
        // Trier les cartes restantes de chaque main par leur valeur, en traitant l'As
        // comme 14
        Arrays.sort(main1, (a, b) -> {
            int valeurA = (a.getValeur() == 1) ? 14 : a.getValeur(); // As devient 14
            int valeurB = (b.getValeur() == 1) ? 14 : b.getValeur(); // As devient 14
            return valeurB - valeurA; // Trier de la plus forte à la plus faible
        });

        Arrays.sort(main2, (a, b) -> {
            int valeurA = (a.getValeur() == 1) ? 14 : a.getValeur(); // As devient 14
            int valeurB = (b.getValeur() == 1) ? 14 : b.getValeur(); // As devient 14
            return valeurB - valeurA; // Trier de la plus forte à la plus faible
        });

        // Comparer les kickeurs
        for (int i = 0; i < Math.min(main1.length, main2.length); i++) {
            if (main1[i].getValeur() > main2[i].getValeur()) {
                return true; // La main actuelle a un kicker plus fort
            } else if (main1[i].getValeur() < main2[i].getValeur()) {
                return false; // La main m2 a un kicker plus fort
            }
        }
        return false; // Les kickeurs sont égaux, donc les mains sont égales
    }

    private boolean compareCartesHautes(Carte[] main1, Carte[] main2) {
        // Trier les cartes par leur valeur, en traitant l'As comme 14
        Arrays.sort(main1, (a, b) -> {
            int valeurA = (a.getValeur() == 1) ? 14 : a.getValeur(); // As devient 14
            int valeurB = (b.getValeur() == 1) ? 14 : b.getValeur(); // As devient 14
            return valeurB - valeurA; // Trier de la plus forte à la plus faible
        });

        Arrays.sort(main2, (a, b) -> {
            int valeurA = (a.getValeur() == 1) ? 14 : a.getValeur(); // As devient 14
            int valeurB = (b.getValeur() == 1) ? 14 : b.getValeur(); // As devient 14
            return valeurB - valeurA; // Trier de la plus forte à la plus faible
        });

        // Comparer les cartes de la main
        for (int i = 0; i < main1.length; i++) {
            if (main1[i].getValeur() > main2[i].getValeur()) {
                return true; // La main actuelle a une carte haute plus forte
            } else if (main1[i].getValeur() < main2[i].getValeur()) {
                return false; // La main m2 a une carte haute plus forte
            }
        }
        return false; // Les mains ont les mêmes cartes hautes
    }

    // Récupère toutes les combinaisons possibles de 5 cartes avec les cartes du joueur et du croupier
    public List<Carte[]> getAllCombinations(Carte[] croupierCards) {
        List<Carte[]> combinations = new ArrayList<>();
        List<Carte[]> croupierComb3 = getCombinations(croupierCards, 3); // Combinaisons de 3 parmi les 5

        // Combiner chaque combinaison de 3 cartes du croupier avec les 2 cartes du joueur
        for (Carte[] comb3 : croupierComb3) {
            Carte[] combined = new Carte[5];
            System.arraycopy(main, 0, combined, 0, main.length); // Cartes du joueur
            System.arraycopy(comb3, 0, combined, main.length, comb3.length); // 3 cartes du croupier
            combinations.add(combined);
        }

        return combinations;
    }

    // Génère toutes les combinaisons possibles de 'k' cartes parmi un tableau donné
    private List<Carte[]> getCombinations(Carte[] cartes, int k) {
        List<Carte[]> result = new ArrayList<>();
        getCombinationsRecursive(cartes, k, 0, new Carte[k], result);
        return result;
    }

    private void getCombinationsRecursive(Carte[] cartes, int k, int start, Carte[] currentComb, List<Carte[]> result) {
        if (k == 0) {
            result.add(Arrays.copyOf(currentComb, currentComb.length));
            return;
        }
        for (int i = start; i <= cartes.length - k; i++) {
            currentComb[currentComb.length - k] = cartes[i];
            getCombinationsRecursive(cartes, k - 1, i + 1, currentComb, result);
        }
    }

}
