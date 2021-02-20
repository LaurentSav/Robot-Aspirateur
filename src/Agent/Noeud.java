package Agent;

import Environnement.Case;

import java.awt.*;

public class Noeud {

    enum Action {
        START, RIGHT, LEFT,  UP, DOWN, VACUUM, PICKUP
    }

    private Case c;
    private Noeud parent;
    private Action action;
    private int cout;
    private boolean isVisited;
    private double Performance ;

    public Noeud(Case ca, Noeud parent){
        c = ca;
        this.parent = parent;

        if(parent == null){
            int cout = 0;
        }else {
            cout = parent.getCout();
        }
        action = Action.START;
        isVisited = false;
    }

    public Noeud(Case ca, Noeud parent, Action action){
        c = ca;
        this.parent = parent;
        this.action = action;
        isVisited = false;
        setPerformance(parent.getPerformance());
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public void setParent(Noeud parent) {
        this.parent = parent;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public int getCout() {
        return cout;
    }

    public Noeud getParent() {
        return parent;
    }


    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public Case getC() {
        return c;
    }
    public void setC(Case c) {
        this.c = c;
    }

    public void setPerformance(double performance){ this.Performance += performance;}



    public double getPerformance() {
        return Performance;
    }

    @Override
    public String toString() {
        return "Noeud{" + action + "}";
    }
}
