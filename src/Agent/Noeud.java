package Agent;

import java.awt.*;

public class Noeud {
    private Point position;
    private Noeud parent;
    private int profondeur;
    private int cout;

    public Noeud(Point pos, Noeud parent){
        position = pos;
        this.parent = parent;
    }

    public void setParent(Noeud parent) {
        this.parent = parent;
    }

    public void setProfondeur(int profondeur) {
        this.profondeur = profondeur;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public Noeud getParent() {
        return parent;
    }

    public int getCout() {
        return cout;
    }

    public int getProfondeur() {
        return profondeur;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
