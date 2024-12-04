package edu.info0502.poker;

import java.io.Serializable;
import java.util.Random;

public class Paquet implements Serializable{
    private static final long serialVersionUID = 1L;
    private Carte[] paquet = new Carte[52];
    public static int compt = 0;

    public Paquet(){
        for(int i = 0; i<13; i++){
            paquet[i] = new Carte(Couleur.Carreau, Vrai_Couleur.Rouge, i+1);
            paquet[i+13] = new Carte(Couleur.Coeur, Vrai_Couleur.Rouge, i+1);
            paquet[i+26] = new Carte(Couleur.Pique, Vrai_Couleur.Noir, i+1);
            paquet[i+39] = new Carte(Couleur.Trèfle, Vrai_Couleur.Noir, i+1);
        }
    }

    public String toString(){
        String s = "";
        for(int i = 0; i<52; i++){
            s += paquet[i].toString() + "\n";
        }
        s += "\n";
        return s;
    }

    public Carte[] getPaquet(){
        return this.paquet;
    }

    public void melangCartes() {
        Random random = new Random();

        for (int i = paquet.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1); 

            Carte temp = paquet[i];
            paquet[i] = paquet[j];
            paquet[j] = temp;
        }

    }

    public Carte tirerCarte(){
        if(compt == 52){
            compt = 0;
        }
        return paquet[compt++];
    }
}
