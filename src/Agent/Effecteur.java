package Agent;

import Environnement.Environnement;
import Environnement.Case;

import java.awt.*;
import java.util.ArrayList;

public class Effecteur {

    public synchronized void doit(ArrayList<Noeud> intention){
        if(intention != null){
            for(Noeud n : intention){
                Case[][] temp = Environnement.getCarte();
                Point p = n.getC().getPosition();
                switch(n.getAction()){
                    case "droite":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x][p.y - 1].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        break;

                    case "gauche":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x][p.y + 1].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        break;

                    case "haut":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x + 1][p.y].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        break;

                    case "bas":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x - 1][p.y].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        break;
                    case "dirt":
                        temp[p.x][p.y].setDirtyspace(false);
                        Environnement.setCarte(temp);
                        break;
                    case "jewel":
                        Environnement.getCarte()[p.x][p.y].setLostjewel(false);
                }
            }
        }
    }
}
