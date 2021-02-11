package Environnement;

import java.awt.*;

public class Case {

    private boolean dirtyspace;
    private boolean lostjewel;
    private boolean Agent;
    private Point position;

    public Case(Point p){
        position = p;
        dirtyspace = false;
        lostjewel = false;
        Agent = false;
    }

    public Case(Point p, boolean agent){
        position = p;
        dirtyspace = false;
        lostjewel = false;
        Agent = true;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean isDirtyspace() {
        return dirtyspace;
    }

    public boolean isLostjewel() {
        return lostjewel;
    }

    public  boolean isAgent(){ return Agent; }

    public void setAgent(boolean Agent){
        this.Agent = Agent;
    }

    public void setDirtyspace(boolean dirtyspace) {
        this.dirtyspace = dirtyspace;
    }

    public void setLostjewel(boolean lostjewel) {
        this.lostjewel = lostjewel;
    }

    public Point getPosition() {
        return position;
    }


    public String toString(){
        String affichage = "[   ";
        char[] aff = affichage.toCharArray();
        if(dirtyspace){
            aff[1] = 'D';
        }
        if(Agent){
            aff[2] = 'A';
        }
        if(lostjewel){
            aff[3] = 'J';
        }
        affichage = String.valueOf(aff) + "]";
        return affichage;
    }

}
