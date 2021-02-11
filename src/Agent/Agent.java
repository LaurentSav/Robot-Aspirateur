package Agent;

import Environnement.Case;

import java.awt.*;
import java.util.*;

public class Agent extends Thread {

    private Boolean isAlive;
    private Capteur capteur;
    private Effecteur effecteur;
    private static Case agent;

    /* ETAT BDI */
    private Case[][] beliefs;
    private ArrayList<Case> desires;
    private ArrayList<Noeud> intentions;

    public Agent(){

        isAlive = true;

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

        while(true){

            capteur.Observation();
            updateState();
            chooseAction();
            System.out.println(intentions);
            effecteur.doit(intentions);

            try{
                Thread.sleep(1000);
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
        desires.clear();
        for (int i = 0; i < beliefs.length; i++) {
            for (int j = 0; j < beliefs.length; j++) {
                if(beliefs[i][j].isDirtyspace() || beliefs[i][j].isLostjewel()){
                    desires.add(beliefs[i][j]);
                }
            }
        }
    }

    public ArrayList<Noeud> chooseAction(){

        if(desires.isEmpty()){
            intentions.clear();
        }else{
            //intentions = AlgorithmeNonInformee();
            intentions = AlgorithmeInformee();
        }
        return intentions;
    }

    /* ALGORITHME DFS */
    public ArrayList<Noeud> AlgorithmeNonInformee(){

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

    public ArrayList<Noeud> AlgorithmeInformee(){

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
