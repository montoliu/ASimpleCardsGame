package tests;

import actions.Action;
import game.Game;
import game.GameParameters;
import game.Observation;

import java.util.List;

public class TestObservation
{
    public static void main(String[] args)
    {
        GameParameters gp     = new GameParameters();
        Game           game   = new Game(gp);
        game.start();

        Observation obs = game.getObservation();

        System.out.println("Observation: ");
        System.out.println(obs);


        System.out.println("\nPossible actions: ");
        List<Action> actions = obs.getPossibleActions();
        for (Action a: actions)
            System.out.println(a);
    }
}
