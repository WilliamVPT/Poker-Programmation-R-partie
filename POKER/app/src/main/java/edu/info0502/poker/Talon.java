package edu.info0502.poker;
import java.util.Arrays;
import java.util.Random;


public class Talon {
    int nb_paquet;
    public static int compt = 0;
    Paquet[] talon;

    public Talon(){
        nb_paquet = 1;
        talon = new Paquet[1];
        talon[0] = new Paquet();
    }

    public Talon(int nb){
        this.nb_paquet = nb;
        this.talon = new Paquet[this.nb_paquet];
        for(int i = 0; i < this.nb_paquet; i++){
            this.talon[i] = new Paquet();
        }
    }

    public void melanger() {
        for(int i = 0; i<this.nb_paquet; i++){
            talon[i].melangCartes();
        }
    }  

    public Carte tirerCarte(){
        if(compt == nb_paquet){
            compt = 0;
        }
        return talon[compt].tirerCarte();
    }

    public int getNbC(){
        return this.nb_paquet;
    }

    public Paquet[] getTalon(){
        return this.talon;
    }

    public String toString(){
        String s = "";
        for(int i = 0; i < this.nb_paquet; i++){
            s += this.talon[i].toString() + "\n";
        }
        s += "\n";
        return s;
    }
}
