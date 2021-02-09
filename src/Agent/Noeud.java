package Agent;

import Environnement.Case;

import java.awt.*;

public class Noeud {
    private Case c;
    private Noeud parent;
    private String action;
    private int cout;

    public Noeud(Case ca, Noeud parent){
        c = ca;
        this.parent = parent;
        int cout = 0;
        String action = null;
    }

    public Noeud(Case c){
        this.c = c;
        this.parent = null;
        int cout = 0;
        String action = null;
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
        return "Noeud{" + c.getPosition() + "}";
    }
}
