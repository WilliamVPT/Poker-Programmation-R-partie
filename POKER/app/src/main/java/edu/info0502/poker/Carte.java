package edu.info0502.poker;

public class Carte {
    private Couleur couleur;
    private Vrai_Couleur vrai_Couleur;
    private int valeur;

    public Carte(){
        this.couleur = null;
        this.vrai_Couleur = null;
        this.valeur = 0;
    }

    public Carte(Couleur c, Vrai_Couleur vc, int v){
        this.couleur = c;
        this.vrai_Couleur = vc;
        if(v >= 1 || v <= 13)
            this.valeur = v;
    }

    public Carte(Carte other){
        this.couleur = other.couleur;
        this.vrai_Couleur = other.vrai_Couleur;
        this.valeur = other.valeur;
    }

    public Couleur getCouleur(){
        return this.couleur;
    }

    public Vrai_Couleur getVraiCouleur(){
        return this.vrai_Couleur;
    }

    public int getValeur(){
        return this.valeur;
    }

    public void setCouleur(Couleur c){
        this.couleur = c;
    }

    public void setVraiCouleur(Vrai_Couleur vc){
        this.vrai_Couleur = vc;
    }

    public void setValeur(int v){
        if(v>=1 || v<=13)
            this.valeur = v;
        else 
            System.out.println("Le nombre doit être compris entre 1 et 13");
    }

    public String toString() {
        String s = "Couleur : " + this.couleur + " | Vrai couleur de la carte : " + this.vrai_Couleur + " | ";
    
        switch (this.valeur) {
            case 1:
                s += "valeur de la carte : as";
                break;
            case 11:
                s += "valeur de la carte : valet";
                break;
            case 12:
                s += "valeur de la carte : dame";
                break;
            case 13:
                s += "valeur de la carte : roi";
                break; // Ajouté un break ici
            default:
                s += "valeur de la carte : " + this.valeur;
                break;
        }
    
        return s;
    }
    
}
