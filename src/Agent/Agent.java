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
            intentions = AlgorithmeNonInformee();
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

    public static void setAgent(Case agent) {
        Agent.agent = agent;
    }

    public static Case getAgent() {
        return agent;
    }
}
