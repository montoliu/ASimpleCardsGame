package run;

import game.Game;
import game.GameParameters;
import players.GreedyPlayer;
import players.MCTSPlayer;
import players.Player;

public class PlayOneGame
{
    public static void main(String[] args)
    {
        GameParameters gp     = new GameParameters();
        //gp.seed = 2;

        Game game   = new Game(gp);
        int  budget = 1000;

        Player p1 = new GreedyPlayer();
        Player p2 = new MCTSPlayer(budget);

        game.start();
        game.run(p1, p2, budget);
        System.out.println(p1.title() + " vs " + p2.title() + " : " + game.getWinner() + " -> Score: " + game.getP1Score() + " , " + game.getP2Score());
    }
}
