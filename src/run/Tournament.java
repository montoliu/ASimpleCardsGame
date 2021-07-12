package run;

import actions.Action;
import game.Game;
import game.GameParameters;
import players.GreedyPlayer;
import players.MCTSPlayer;
import players.Player;
import players.RandomPlayer;

import java.lang.reflect.GenericArrayType;

public class Tournament
{
    public static void main(String[] args)
    {
        GameParameters gp      = new GameParameters();
        int            budget  = 1000;
        int            n_games = 100;
        int            seed    = 1;

        Player p1 = new MCTSPlayer();
        Player p2 = new GreedyPlayer();

        p1.setSeed(seed);
        p2.setSeed(seed);

        int p1_wins = 0;
        int p2_wins = 0;
        int winner;

        for (int i = 0; i < n_games; i++)
        {
            System.out.println(i);
            winner = PlayAGame(p1, p2, gp, budget, seed);
            if (winner == 1)      p1_wins += 1;
            else if (winner == 2) p2_wins += 1;

            winner = PlayAGame(p2, p1, gp, budget, seed);
            if (winner == 1)      p2_wins += 1;
            else if (winner == 2) p1_wins += 1;

        }
        System.out.println("wins " + p1.title() + ":" + p1_wins + ", " + p2.title() + ":" + p2_wins);
    }

    private static int PlayAGame(Player p1, Player p2, GameParameters gp, int budget, int seed)
    {
        Game game = new Game(gp);

        game.setSeed(seed);
        game.start();
        game.run(p1, p2, budget);
        System.out.println(p1.title() + " vs " + p2.title() + " -> " + game.getWinner() + "[" + game.getP1Score() + ", " + game.getP2Score() + "]");
        return game.getWinner();
    }
}
