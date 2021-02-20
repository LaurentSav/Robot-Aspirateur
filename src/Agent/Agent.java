package Agent;

import Environnement.Case;
import com.sun.source.tree.ClassTree;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

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

        while(true){
            capteur.Observation();
            updateState();
            chooseAction();
            effecteur.doit(intentions);
            System.out.println("intentions : " +intentions);
            try{
                Thread.sleep(Math.round(2000));
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

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
            //La case que l'agent cible
            System.out.println(" But :  x = " + desires.get(desires.size()-1).getPosition().x + " y = " + desires.get(desires.size()-1).getPosition().y);

            //intentions = AlgorithmeNonInformee(agent);

            // Algorithme Informe
            ArrayList<Case> visited = new ArrayList<>();
            Noeud n = AlgorithmeAStar( desires.get(desires.size()-1), new Noeud(agent, null), null, visited);
            while(n.getParent() != null){

                // On ajoute une action si la case n'est pas vide.
                // Si la case a de la poussière et un bijou, on ajoute "vacuum" puis "pick up".
                // L'ordre d'action sera inversé par la suite.
                if(n.getC().isDirtyspace()){
                    Noeud action = new Noeud(n.getC(), n.getParent(), Noeud.Action.VACUUM);
                    intentions.add(action);
                }
                if(n.getC().isLostjewel()){
                    Noeud action = new Noeud(n.getC(), n.getParent(), Noeud.Action.PICKUP);
                    intentions.add(action);
                }
                intentions.add(n);
                n = n.getParent();
            }

            // On inverse la liste car la liste des actions est récupéré depuis le but jusqu'au noeud racine.
            // Ainsi, avant d'inverser la liste, l'agent ferait la dernière action en premier et inversement...
            Collections.reverse(intentions);

        }

    }

    /* ALGORITHME DFS */
    public ArrayList<Noeud> AlgorithmeNonInformee(Case agent){

        Noeud racine = new Noeud(agent, null);
        Stack<Noeud> stack = new Stack<Noeud>();
        ArrayList<Point> visit = new ArrayList<Point>();
        stack.push(racine);

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
                        path.add(new Noeud(beliefs[posX][posY],n, Noeud.Action.PICKUP));
                    }
                    if(beliefs[posX][posY].isDirtyspace()){
                        path.add(new Noeud(beliefs[posX][posY],n,Noeud.Action.VACUUM));
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
                    stack.push(new Noeud(beliefs[posX - 1][posY],n, Noeud.Action.UP));
                }

                /* Mouvement Bas */
                if(posX + 1 < 5){
                    stack.push(new Noeud(beliefs[posX + 1][posY],n, Noeud.Action.DOWN));
                }

                /* Mouvement Gauche */
                if(posY - 1 >= 0){
                    stack.push(new Noeud(beliefs[posX][posY - 1],n, Noeud.Action.LEFT));
                }

                /* Mouvement Droite */
                if(posY + 1 < 5){
                    stack.push(new Noeud(beliefs[posX][posY + 1],n, Noeud.Action.RIGHT));
                }
            }
        }
        return null;
    }

    /*
    * ALGORITHME A*
    * La fonction A* retourne le dernier noeud de la solution.
    * A partir de ce noeud, on peut reconstituer le chemin optimal à prendre
    *  pour atteindre le but grâce aux noeuds parents.
    * @param  ArrayList<Noeud> list:  Cette liste contient l'ensemble des noeud que l'on peut encore explorer.
    *                               Elle représente l'ensemble des f-contours.
    *
    */

    public Noeud AlgorithmeAStar(Case nBut, Noeud racine, ArrayList<Noeud> list, ArrayList<Case> visited) {

        if (list == null) {
            list = new ArrayList(List.of(racine));
        }

        /* Récupération des coordonnées de la case actuelle */
        int posX = racine.getC().getPosition().x;
        int posY = racine.getC().getPosition().y;

        // Si on se trouve sur la case but (et donc qu'on a atteint notre but), on retourne immédiatement la solution
        if (posX == nBut.getPosition().x && posY == nBut.getPosition().y) { return racine; }

        /* Mouvement UP */
        if (posX - 1 >= 0 && !visited.contains(beliefs[posX - 1][posY])) {
            visited.add(beliefs[posX - 1][posY]);

            // Si présénce de Bijou et Poussière en même temps dans la case du haut
            if (beliefs[posX - 1][posY].isLostjewel() && beliefs[posX - 1][posY].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX - 1][posY], racine, Noeud.Action.UP);
                setNodePerformance(-60, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX - 1][posY].isLostjewel()) {
                Noeud n = new Noeud(beliefs[posX - 1][posY], racine, Noeud.Action.UP);
                setNodePerformance(-45, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX - 1][posY].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX - 1][posY], racine, Noeud.Action.UP);
                setNodePerformance(-30, n, racine, nBut);
                list.add(n);
            }else {
                // Dans ce cas, la case du dessus est vide.
                Noeud n = new Noeud(beliefs[posX - 1][posY], racine, Noeud.Action.UP);
                setNodePerformance(10, n, racine, nBut);
                list.add(n);
            }
        }

        // Le principe est le même pour les autres mouvements

        /* Mouvement DOWN */
        if (posX + 1 < 5 && !visited.contains(beliefs[posX + 1][posY])) {
            visited.add(beliefs[posX + 1][posY]);
            if (beliefs[posX + 1][posY].isLostjewel() && beliefs[posX + 1][posY].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX + 1][posY], racine, Noeud.Action.DOWN);
                setNodePerformance(-60, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX + 1][posY].isLostjewel()) {
                Noeud n = new Noeud(beliefs[posX + 1][posY], racine, Noeud.Action.DOWN);
                setNodePerformance(-45, n, racine, nBut);
                list.add(n);
            }else if (racine.getC().isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX + 1][posY], racine, Noeud.Action.DOWN);
                setNodePerformance(-30, n, racine, nBut);
                list.add(n);
            }else {
                Noeud n = new Noeud(beliefs[posX + 1][posY], racine, Noeud.Action.DOWN);
                setNodePerformance(10, n, racine, nBut);
                list.add(n);
            }
        }

        /* Mouvement LEFT */
        if (posY - 1 >= 0 && !visited.contains(beliefs[posX][posY - 1])) {
            visited.add(beliefs[posX][posY - 1]);
            if (beliefs[posX][posY - 1].isLostjewel() && beliefs[posX][posY - 1].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX][posY - 1], racine, Noeud.Action.LEFT);
                setNodePerformance(-55, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX][posY - 1].isLostjewel()) {
                Noeud n = new Noeud(beliefs[posX][posY - 1], racine, Noeud.Action.LEFT);
                setNodePerformance(-45, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX][posY - 1].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX][posY - 1], racine, Noeud.Action.LEFT);
                setNodePerformance(-30, n, racine, nBut);
                list.add(n);
            }else {
                Noeud n = new Noeud(beliefs[posX][posY - 1], racine, Noeud.Action.LEFT);
                setNodePerformance(10, n, racine, nBut);
                list.add(n);
            }
        }

        /* Mouvement RIGHT */
        if (posY + 1 < 5 && !visited.contains(beliefs[posX][posY + 1])) {
            visited.add(beliefs[posX][posY + 1]);
            if (beliefs[posX][posY + 1].isLostjewel() && beliefs[posX][posY + 1].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX][posY + 1], racine, Noeud.Action.RIGHT);
                setNodePerformance(-55, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX][posY + 1].isLostjewel()) {
                Noeud n = new Noeud(beliefs[posX][posY + 1], racine, Noeud.Action.RIGHT);
                setNodePerformance(-45, n, racine, nBut);
                list.add(n);
            }else if (beliefs[posX][posY + 1].isDirtyspace()) {
                Noeud n = new Noeud(beliefs[posX][posY + 1], racine, Noeud.Action.RIGHT);
                setNodePerformance(-30, n, racine, nBut);
                list.add(n);
            }else {
                Noeud n = new Noeud(beliefs[posX][posY + 1], racine, Noeud.Action.RIGHT);
                setNodePerformance(10, n, racine, nBut);
                list.add(n);
            }
        }

        // On retire le noeud exploré de la liste des noeuds que l'on peut encore explorer
        list.remove(racine);
        return AlgorithmeAStar(nBut, evaluateBest(list), list, visited);

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


    private void setNodePerformance(double desireCost, Noeud node, Noeud parent, Case but){

        // Les coup dans l'arbre sont cumulatif entre chaque noeud.
        double distManhParent = Math.abs(but.getPosition().x - parent.getC().getPosition().x) + Math.abs(but.getPosition().y - parent.getC().getPosition().y);
        node.setPerformance(parent.getPerformance() - distManhParent);

        // f(n) = distance de manhattan + coup de désirabilité d'action
        // Le coup de désirabilité est le coup qui incitera l'agent a se déplacer vers une case car il risque d'y trouver quelque chose.
        double dist = Math.abs(but.getPosition().x - node.getC().getPosition().x) + Math.abs(but.getPosition().y - node.getC().getPosition().y);
        node.setPerformance(dist);
        node.setPerformance(desireCost);
    }


    /*
    * La fonction evaluateBest retourne le noeud dont la performance est minimale, i.e la meilleure performance.
     */
    private Noeud evaluateBest(List<Noeud> actions){
        Noeud bestNode = actions.get(0);
        for (Noeud n: actions) {
            if(n.getPerformance() <= bestNode.getPerformance()){
                bestNode = n;
            }
        }
        return bestNode;
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
