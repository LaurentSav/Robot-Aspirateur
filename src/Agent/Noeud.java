package Agent;

import Environnement.Case;

import java.awt.*;

public class Noeud {

    private Case c;
    private Noeud parent;
    private String action;
    private int cout;
    private boolean isVisited;

    public Noeud(Case ca, Noeud parent){
        c = ca;
        this.parent = parent;
        int cout = 0;
        String action = null;
        isVisited = false;
    }

    public Noeud(Case ca, Noeud parent, String action){
        c = ca;
        this.parent = parent;
        int cout = 0;
        this.action = action;
        isVisited = false;
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


    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public Case getC() {
        return c;
    }
    public void setC(Case c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Noeud{" + action + "}";
    }
}
