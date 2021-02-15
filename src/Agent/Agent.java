package Agent;

import Environnement.Case;
import com.sun.source.tree.ClassTree;

import java.awt.*;
import java.util.*;

public class Agent extends Thread {

    private Boolean isAlive;
    private Capteur capteur;
    private Effecteur effecteur;
    private static Case agent;
    private int loopEpisode;
    private int perfMini;
    private final double LEARN_COEF = 0.08;//Coefficient d'apprentissage
    private final int MAX_CAPACITY = 5;//Capacite maximale du reservoir de l'agent

    /* ETAT BDI */
    private Case[][] beliefs; //Croyances
    private ArrayList<Case> desires; //Désires
    private ArrayList<Noeud> intentions; //Intentions

    public Agent(){

        isAlive = true;
        loopEpisode = 0;
        perfMini = Integer.MAX_VALUE;

        capteur = new Capteur();
        effecteur = new Effecteur();

        agent = new Case(new Point(0,0), true);

        beliefs = new Case[5][5];
        desires = new ArrayList<Case>();
        intentions = new ArrayList<Noeud>();

    }

    @Override
    public void run(){
        System.out.println("S");
        int roundPerf = 1;
        double waitCoef = 10.000;
        double learnValue = 0;
        while(true){
            capteur.Observation();
            updateState();
            chooseAction();
            effecteur.doit(intentions);
            learnValue = evaluation(roundPerf)*LEARN_COEF;
            System.out.println("learn value : " + learnValue);
            waitCoef += waitCoef*learnValue;

            System.out.println("This round's wait time : " +Math.abs(waitCoef*1000) + " ms");
            try{
                Thread.sleep(Math.round(1000*Math.abs(waitCoef)));
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            roundPerf = effecteur.getPerf();

        }
    }

    public void mesurePerformance(){

        if(loopEpisode < 3){


            loopEpisode++;
        }else{
            /* On réinitialise l'épisode */
            loopEpisode = 0;

        }
    }

    /* On met à jour les états de l'agent */
    public void updateState(){
        beliefs = capteur.getCarte();
        updateDesires();
    }

    /* Mise à jour des désires */
    public void updateDesires(){

        /* On vide le tableau des désires */
        desires.clear();

        /* On remplit le tableau des désires avec les cases non vides de l'environnement */
        for (int i = 0; i < beliefs.length; i++) {
            for (int j = 0; j < beliefs.length; j++) {
                if(beliefs[i][j].isDirtyspace() || beliefs[i][j].isLostjewel()){
                    desires.add(beliefs[i][j]);
                }
            }
        }

        for (int i = 0; i < desires.size(); i++) {
            desires.get(i).setDistance(distanceManhattan(desires.get(i), agent));
        }

        /* On tri le tableau des désires en fonction de la distance entre l'agent et la case non vide */
        desires.sort(Comparator.comparing(a -> a.getDistance()));

    }

    /* On remplit le tableau des intentions grâce aux algorithmes informé ou non informé. */

    public void chooseAction(){
        int learnValue = 0;
        /* Si le tableau des désires est vide, le robot ne fait rien */
        if(desires.isEmpty()){
            intentions.clear();
        }else{
            //intentions = AlgorithmeNonInformee();

            // Algo informee
            intentions = AlgorithmeInformee(agent);
            for (int i = 1; i < desires.size() && i<MAX_CAPACITY; i++){
                //Algo recherche autant de case vides qu'on peut dans la limite de capacite de l'aspirateur
                intentions.addAll(AlgorithmeInformee(intentions.get(intentions.size() - 1).getC()));
            }
        }

    }

    /* ALGORITHME DFS */
    public ArrayList<Noeud> AlgorithmeNonInformee(Case agent){

        Noeud nRacine = new Noeud(agent, null);
        Stack<Noeud> stack = new Stack<Noeud>();
        ArrayList<Point> visit = new ArrayList<Point>();
        stack.push(nRacine);

        while(!stack.isEmpty()){
            Noeud n = stack.pop();
            if(visit.contains(n.getC().getPosition())){
                n.setVisited(true);
            }
            if(!n.isVisited()){
                n.setVisited(true);
                visit.add(n.getC().getPosition());
                int posX = n.getC().getPosition().x;
                int posY = n.getC().getPosition().y;

                /* Robot sur case non vide */
                if(beliefs[posX][posY].isLostjewel() || beliefs[posX][posY].isDirtyspace()){
                    ArrayList<Noeud> path = new ArrayList<Noeud>();
                    if(beliefs[posX][posY].isLostjewel()){
                        path.add(new Noeud(beliefs[posX][posY],n,"jewel"));
                    }
                    if(beliefs[posX][posY].isDirtyspace()){
                        path.add(new Noeud(beliefs[posX][posY],n,"dirt"));
                    }
                    while(n.getParent() != null){
                        path.add(n);
                        n = n.getParent();
                    }
                    Collections.reverse(path);

                    return path;
                }

                /* Mouvement Haut */
                if(posX - 1 >= 0){
                    stack.push(new Noeud(beliefs[posX - 1][posY],n,"haut"));
                }

                /* Mouvement Bas */
                if(posX + 1 < 5){
                    stack.push(new Noeud(beliefs[posX + 1][posY],n,"bas"));
                }

                /* Mouvement Gauche */
                if(posY - 1 >= 0){
                    stack.push(new Noeud(beliefs[posX][posY - 1],n,"gauche"));
                }

                /* Mouvement Droite */
                if(posY + 1 < 5){
                    stack.push(new Noeud(beliefs[posX][posY + 1],n,"droite"));
                }
            }
        }
        return null;
    }

    /* ALGORITHME BFS */

    public ArrayList<Noeud> AlgorithmeInformee(Case agent){

        /* Noeud Racine */
        Noeud nRacine = new Noeud(agent, null);
        Stack<Noeud> stack = new Stack<Noeud>();
        Noeud nBut = new Noeud(desires.get(0), null);
        stack.push(nRacine);
        boolean firstloop = true;
        int dParent = 0;

        while(!stack.isEmpty()){
            Noeud n = stack.pop();
            int posX = n.getC().getPosition().x;
            int posY = n.getC().getPosition().y;

            int dFils = distanceManhattan(n.getC(), nBut.getC());

            if(!firstloop){
                dParent = distanceManhattan(n.getParent().getC(), nBut.getC());
            }

            if(posX == nBut.getC().getPosition().x && posY == nBut.getC().getPosition().y){
                ArrayList<Noeud> path = new ArrayList<Noeud>();
                if(beliefs[posX][posY].isLostjewel()){
                    path.add(new Noeud(beliefs[posX][posY],n,"jewel"));
                }
                if(beliefs[posX][posY].isDirtyspace()){
                    path.add(new Noeud(beliefs[posX][posY],n,"dirt"));
                }
                while(n.getParent() != null){
                    path.add(n);
                    n = n.getParent();
                }

                Collections.reverse(path);
                desires.remove(0);
                return path;
            }

            if(dFils < dParent || firstloop){
                /* Mouvement Haut */
                if(posX - 1 >= 0){
                    stack.push(new Noeud(beliefs[posX - 1][posY],n,"haut"));
                }

                /* Mouvement Bas */
                if(posX + 1 < 5){
                    stack.push(new Noeud(beliefs[posX + 1][posY],n,"bas"));
                }

                /* Mouvement Gauche */
                if(posY - 1 >= 0){
                    stack.push(new Noeud(beliefs[posX][posY - 1],n,"gauche"));
                }

                /* Mouvement Droite */
                if(posY + 1 < 5){
                    stack.push(new Noeud(beliefs[posX][posY + 1],n,"droite"));
                }
                firstloop = false;
            }

        }
        return null;
    }

/*
    public float learn(float waitTime, int Perf){
        updateState();

        effecteur.setPerf( effecteur.getPerf()* (int)(Math.pow(0.8,desires.size())));
        System.out.println("Current perf " + effecteur.getPerf());
        System.out.println("Last perf " + Perf);
        float learningCoef = effecteur.getPerf() / Perf;
        float newTime = (float) (waitTime + 0.2*(waitTime*learningCoef));
        if (newTime >=  Math.abs(waitTime*2)){ newTime = waitTime*2;}
        return newTime;
    }*/

    public double evaluation(int lastPerf){
        updateState();
        effecteur.setPerf(effecteur.getPerf()-10*desires.size());
        double learnValue = 0;

        if (lastPerf != 0 ) {
            learnValue = (effecteur.getPerf() - lastPerf)/lastPerf;

        }else {
            learnValue = 1;
        }
        return learnValue;
    }

    public int distanceManhattan(Case c1, Case c2){
        return Math.abs(c1.getPosition().x - c2.getPosition().x) + Math.abs(c1.getPosition().y - c2.getPosition().y);
    }

    public static void setAgent(Case agent) {
        Agent.agent = agent;
    }

    public static Case getAgent() {
        return agent;
    }

}
