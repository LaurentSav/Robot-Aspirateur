package Agent;

import Environnement.Case;
import Environnement.Environnement;

import java.awt.*;
import java.util.ArrayList;

public class Capteur {

    private Case[][] carte;

    public Capteur(Case[][] carte){
        this.carte = carte;
    }

    public void Observation(){
        carte = Environnement.getCarte();
    }

    public Case[][] getCarte() {
        return carte;
    }
}