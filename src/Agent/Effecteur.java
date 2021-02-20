package Agent;

import Environnement.Environnement;
import Environnement.Case;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Effecteur {

    private int perf;

    public Effecteur(){
        perf = 0;
    }
    /* Effectue les actions contenues dans le tableau des intentions */

    public synchronized void doit(ArrayList<Noeud> intention){
        if(intention != null){
            perf = 0;
            for(Noeud n : intention){
                Case[][] temp = Environnement.getCarte();
                Point p = n.getC().getPosition();
                if (n.getAction() == null){ continue;}
                switch(n.getAction()){
                    case RIGHT:
                        temp[p.x][p.y - 1].setAgent(false);
                        temp[p.x][p.y].setAgent(true);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10;
                        break;

                    case LEFT:
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x][p.y + 1].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10 ;
                        break;

                    case UP:
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x + 1][p.y].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10 ;
                        break;

                    case DOWN:
                        temp[p.x][p.y].setAgent(true);
                        temp[p.x - 1][p.y].setAgent(false);
                        Agent.getAgent().setPosition(new Point(p.x, p.y));
                        Environnement.setCarte(temp);
                        perf -= 10 ;
                        break;
                    case VACUUM:
                        /* Si il y'a un bijoux et une poussière sur la case, alors le bijoux et la poussière seront aspirés */
                        if(temp[p.x][p.y].isLostjewel()){
                            perf -= 30;
                        }
                        temp[p.x][p.y].setDirtyspace(false);
                        temp[p.x][p.y].setLostjewel(false);
                        Environnement.setCarte(temp);
                        perf += 30;
                        break;
                    case PICKUP:
                        Environnement.getCarte()[p.x][p.y].setLostjewel(false);
                        perf += 60;
                        break;

                }

                /* On affiche la liste d'actions de l'agent, nous permettant de retracer son chemin, et ce qu'il a nettoyer */
                System.out.println( " action : " + n.getAction() + " x = " + n.getC().getPosition().x + " y = " + n.getC().getPosition().y );

                if(n == intention.get(intention.size()-1)){
                    System.out.println(" ---FIN ACTION--- ");
                }
            }
            intention.clear();

        }
    }


    public int getPerf() {
        return perf;
    }

    public void setPerf(int perf) {
        this.perf = perf;
    }
}
