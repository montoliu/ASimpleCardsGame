package tests;

import game.Game;
import game.GameParameters;
import game.Observation;

public class TestObservation
{
    public static void main(String[] args)
    {
        GameParameters gp     = new GameParameters();
        Game           game   = new Game(gp);
        game.start();

        Observation obs = game.getObservation();

        System.out.println(obs);
    }
}
