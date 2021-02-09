package Agent;

import Environnement.Case;

import java.awt.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class Agent extends Thread {

    private Boolean isAlive;
    private Capteur capteur;
    private Effecteur effecteur;
    private static Case agent;

    /* ETAT BDI */
    private Case[][] beliefs;
    private ArrayList<Case> desires;
    private ArrayList<Case> intentions;

    public Agent(){

        isAlive = true;

        capteur = new Capteur();
        effecteur = new Effecteur();

        agent = new Case(new Point(0,0), true);

        beliefs = new Case[5][5];
        desires = new ArrayList<Case>();
        intentions = new ArrayList<Case>();

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
        updateDesires();
    }

    public void updateDesires(){
        for (int i = 0; i < beliefs.length; i++) {
            for (int j = 0; j < beliefs.length; j++) {
                if(beliefs[i][j].isDirtyspace() || beliefs[i][j].isLostjewel()){
                    desires.add(beliefs[i][j]);
                }
            }
        }
    }

    public ArrayList<Case> chooseAction(){

        if(desires.isEmpty()){
            intentions.clear();
        }else{
            intentions = AlgorithmeNonInformee();
        }
        return intentions;
    }

    public ArrayList<Case> AlgorithmeNonInformee(){

        Noeud nRacine = new Noeud(agent, null);
        Queue<Noeud> queue = null;
        queue.add(nRacine);

        while(!queue.isEmpty()){

        }


    }



}
