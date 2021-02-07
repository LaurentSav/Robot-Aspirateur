package Agent;

import Environnement.Case;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Agent extends Thread {

    private Boolean isAlive;
    private Capteur capteur;
    private Effecteur effecteur;
    private static Point position;
    private int uElec;

    /* ETAT BDI */
    private ArrayList<Case> beliefs;
    private ArrayList<Case> desires;
    private ArrayList<Case> intentions;

    public Agent(){

        isAlive = true;

        capteur = new Capteur();
        effecteur = new Effecteur();

        position = new Point(0,0);

        beliefs = new ArrayList<Case>();
        desires = new ArrayList<Case>();
        intentions = new ArrayList<Case>();

        uElec = 20;

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
        beliefs = capteur.getCaseNonVide();

    }

    public void chooseAction(){

    }

    public void AlgorithmeNonInformee(){

        /* ALGORITHME BFS */

        /* FIFO */
        Stack<Noeud> pile = new Stack<Noeud>();
        /*Solution*/
        ArrayList<Noeud> solution = new ArrayList<Noeud>();

        Noeud currentN = null;
        /* Noeud Racine */
        currentN = new Noeud(position, null);
        solution.add(currentN);

    }

    public void DFS(Noeud n)


    public static void setPosition(Point position) {
        Agent.position = position;
    }
}
