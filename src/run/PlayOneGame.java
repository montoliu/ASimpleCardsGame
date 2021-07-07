package run;

import actions.Action;
import game.Game;
import game.GameParameters;
import players.GreedyPlayer;
import players.Player;
import players.RandomPlayer;

public class PlayOneGame
{
    public static void main(String[] args)
    {
        GameParameters gp     = new GameParameters();
        Game game   = new Game(gp);
        int            budget = 1000;

        Player p1 = new GreedyPlayer();
        Player p2 = new RandomPlayer();

        //p1.setSeed(1);
        //p2.setSeed(1);
        //game.setSeed(1);

        game.start();
        while (game.notFinished())
        {
            // Player 1 turn
            for (int ap=0; ap<gp.number_of_action_points; ap++)
            {
                Action a = p1.act(game.getObservation(), budget);
                if (a == null)
                    break;
                game.step(a);
            }

            game.nextPlayerTurn();
            if (game.notFinished())
            {
                // Player 2 turn
                for (int ap = 0; ap < gp.number_of_action_points; ap++)
                {
                    Action a = p2.act(game.getObservation(), budget);
                    if (a == null)
                        break;
                    game.step(a);
                }
            }
            game.nextPlayerTurn();
        }
        int winner = game.getWinner();

        System.out.println("The winner is the player " + winner + " -> Score: " + game.getP1Score() + " , " + game.getP2Score());
    }
}
