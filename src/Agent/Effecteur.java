package Agent;

import Environnement.Environnement;
import Environnement.Case;

import java.awt.*;

public class Effecteur {

    public synchronized void removeDirt(Point p){
        Case[][] temp = Environnement.getCarte();
        temp[p.x][p.y].setDirtyspace(false);
        Environnement.setCarte(temp);


    }

    public synchronized void removeJewel(Point p){
        Environnement.getCarte()[p.x][p.y].setLostjewel(false);
    }

    public synchronized void move(Point p, String movement){
        Case[][] temp = Environnement.getCarte();
        switch (movement){
            case "droite":
                if(p.y + 1 < 5){
                    temp[p.x][p.y].setAgent(false);
                    temp[p.x][p.y + 1].setAgent(true);
                    Agent.setPosition(new Point(p.x, p.y + 1));
                    Environnement.setCarte(temp);
                }
                break;

            case "gauche":
                if (p.y - 1 < 0) {
                    temp[p.x][p.y].setAgent(false);
                    temp[p.x][p.y - 1].setAgent(true);
                    Agent.setPosition(new Point(p.x, p.y - 1));
                    Environnement.setCarte(temp);
                }

                break;

            case "haut":
                if (p.x - 1 < 0) {
                    temp[p.x][p.y].setAgent(false);
                    temp[p.x - 1][p.y].setAgent(true);
                    Agent.setPosition(new Point(p.x - 1, p.y));
                    Environnement.setCarte(temp);
                }

                break;

            case "bas":
                if (p.x + 1 < 5) {
                    temp[p.x][p.y].setAgent(false);
                    temp[p.x + 1][p.y].setAgent(true);
                    Agent.setPosition(new Point(p.x + 1, p.y));
                    Environnement.setCarte(temp);
                }
                break;
        }


    }


}
