package Agent;

import Environnement.Case;
import Environnement.Environnement;

import java.awt.*;
import java.util.ArrayList;

public class Capteur {

    private Case[][] carte;
    private ArrayList<Case> CaseNonVide;

    public Capteur(){
        this.carte = null;
        this.CaseNonVide = new ArrayList<Case>() ;
    }

    public void Observation(){
        carte = Environnement.getCarte();
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte.length; j++) {
                if(carte[i][j].isDirtyspace() || carte[i][j].isLostjewel()){
                     CaseNonVide.add(carte[i][j]);
                }
            }
        }
    }

    public Case[][] getCarte() {
        return carte;
    }
    public ArrayList<Case> getCaseNonVide(){ return CaseNonVide;}
}
