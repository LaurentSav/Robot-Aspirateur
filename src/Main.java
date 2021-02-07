import Agent.Agent;
import Environnement.Environnement;

public class Main {

    public static void main(String[] args) {

        Agent agent = new Agent();
        Environnement environnement = new Environnement();
        environnement.start();
        agent.start();


    }

}
