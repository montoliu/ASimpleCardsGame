package run;

import actions.Action;
import game.Game;
import game.GameParameters;
import players.GreedyPlayer;
import players.MCTSPlayer;
import players.Player;
import players.RandomPlayer;

public class PlayOneGame
{
    public static void main(String[] args)
    {
        GameParameters gp     = new GameParameters();
        Game           game   = new Game(gp);
        int            budget = 1000;
        int            seed   = 3;     // random seed

        Player p1 = new GreedyPlayer();
        Player p2 = new MCTSPlayer();


        p1.setSeed(seed);
        p2.setSeed(seed);
        game.setSeed(seed);

        game.start();
        game.run(p1, p2, budget);
        System.out.println(p1.title() + " vs " + p2.title() + " : " + game.getWinner() + " -> Score: " + game.getP1Score() + " , " + game.getP2Score());
    }
}
