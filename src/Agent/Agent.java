package Agent;

import Environnement.Case;
import Environnement.Environnement;

import java.awt.*;

public class Agent extends Thread {

    private Boolean isAlive;
    private Capteur capteur;
    private Effecteur effecteur;
    private static Point position;

    /* ETAT BDI */
    private Case[][] beliefs;
    private Case[][] desires;
    private Case[][] intentions;

    public Agent(){

        isAlive = true;

        effecteur = new Effecteur();
        position = new Point(0,0);

        /* Le désire de l'agent est une carte vide */
        desires = new Case[5][5];
        for (int i = 0; i < desires.length; i++) {
            for (int j = 0; j < desires.length; j++) {
                desires[i][j] = new Case(new Point(i,j));
            }
        }
        capteur = new Capteur(desires);

    }

    @Override
    public void run(){
        System.out.println("S");

        while(true){

            capteur.Observation();
            updateState();
            chooseAction();

            try{
                Thread.sleep(500);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    /* On met à jour les croyances de l'agent */
    public void updateState(){
        beliefs = capteur.getCarte();
    }

    public void chooseAction(){
        /* Algorithme ici */
        if(){
            /* Fais rien */
        }else{
            AlgorithmeNonInformee();
        }


    }

    public void AlgorithmeNonInformee(){

    }


    public static void setPosition(Point position) {
        Agent.position = position;
    }
}
