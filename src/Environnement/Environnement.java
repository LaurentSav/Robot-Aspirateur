package Environnement;

import Agent.Agent;

import java.awt.*;
import java.util.Random;

public class Environnement extends Thread{

    private static Case[][] Carte;
    private double DirtPercentage = 50;
    private double JewelPercentage = 70;


    public Environnement() {


        Carte = new Case[5][5];
        for (int i = 0; i < Carte.length; i++) {
            for (int j = 0; j < Carte.length; j++) {
                Carte[i][j] = new Case(new Point(i,j));
            }
        }

        /* Position initial de l'agent */
        Carte[0][0].setAgent(true);

    }
    @Override
    public void run() {

        while(true){

            if(new Random().nextInt(100) <= DirtPercentage){
                GenerateDirt();
            }
            if(new Random().nextInt(100) <= JewelPercentage){
                GenerateJewel();
            }

            System.out.println(this);
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized Case[][] getCarte() {
        return Carte;
    }
    public static synchronized void setCarte(Case[][] carte){
        Carte = carte;
    }

    public Case[][] getCartes(){
        return Carte;
    }

    public void GenerateDirt(){
        int i = (int) (Math.random() * 5);
        int j = (int) (Math.random() * 5);
        Carte[i][j].setDirtyspace(true);
    }

    public void GenerateJewel(){
        int i = (int) (Math.random() * 5);
        int j = (int) (Math.random() * 5);
        Carte[i][j].setLostjewel(true);
    }



    public String toString() {
        String affichage = "";
        for (int i = 0; i < Carte.length; i++) {
            for (int j = 0; j < Carte.length; j++) {
                affichage += Carte[i][j].toString();
            }
            affichage += "\n";

        }
        return affichage;
    }

}

