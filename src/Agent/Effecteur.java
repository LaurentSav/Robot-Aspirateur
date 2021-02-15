package Agent;

import Environnement.Environnement;
import Environnement.Case;

import java.awt.*;
import java.util.ArrayList;

public class Effecteur {

    private int perf;

    public Effecteur(){
        perf = 1;
    }
    /* Effectue les actions contenues dans le tableau des intentions */

    public synchronized void doit(ArrayList<Noeud> intention){
        if(intention != null){
            perf = 0;
            for(Noeud n : intention){
                Case[][] temp = Environnement.getCarte();
                Point p = n.getC().getPosition();
                switch(n.getAction()){
                    case "droite":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x][p.y - 1].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10;
                        break;

                    case "gauche":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x][p.y + 1].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10 ;
                        break;

                    case "haut":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x + 1][p.y].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10 ;
                        break;

                    case "bas":
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x - 1][p.y].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10 ;
                        break;
                    case "dirt":
                        /* Si il y'a un bijoux et une poussière sur la case, alors le bijoux et la poussière seront aspirés */
                        if(temp[p.x][p.y].isLostjewel()){
                        }
                        temp[p.x][p.y].setDirtyspace(false);
                        temp[p.x][p.y].setLostjewel(false);
                        Environnement.setCarte(temp);
                        perf += 30;
                        break;
                    case "jewel":
                        Environnement.getCarte()[p.x][p.y].setLostjewel(false);
                        perf += 60;
                }
            }
        }
    }



    public int getPerf() {
        return perf;
    }

    public void setPerf(int perf) {
        this.perf = perf;
    }
}
