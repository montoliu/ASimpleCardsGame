package tests;

import game.GameParameters;
import game.GameState;

public class TestGameState
{
    public static void main(String[] args)
    {
        GameParameters gp = new GameParameters();
        GameState      gs = new GameState(gp);

        int seed = 1;

        //gs.setSeed(seed);
        gs.initialize();

        System.out.println(gs);
    }
}
