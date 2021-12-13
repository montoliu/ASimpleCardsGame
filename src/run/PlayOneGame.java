package run;

import game.Game;
import game.GameParameters;
import players.*;

//Class that can be run to play one game and print its results
public class PlayOneGame
{
    public static void main(String[] args)
    {
        GameParameters gp = new GameParameters();
        gp.seed           = 1;

        Game game   = new Game(gp);
        int  budget = 1000;

        Player p1 = new RandomPlayer();
        Player p2 = new GreedyPlayer();

        //Player p2 = new HumanPlayer();
        //Player p2 = new RandomPlayer();
        //Player p2 = new GreedyPlayer();
        //Player p2 = new MCTSPlayer(1.4, budget);
        //Player p2 = new OnlineNTBEAGenomeBased(budget, 100, 1.0, 0.01, true, true, true, false, false, false, new SimpleHeuristic());
        //Player p2 = new OnlineEvolutionPlayerUseIllegal(100, 0.1, 0.5, budget, new SimpleHeuristic());
        //Player p2 = new OnlineEvolutionPlayer(100, 0.1, 0.5, budget, new SimpleHeuristic());

        game.Setup();
        game.setVerboseOn();
        game.run(p1, p2, budget);
        System.out.println(p1.title() + " vs " + p2.title() + " : " + game.getWinner() + " -> Score: " + game.getP1Score() + " " + game.getP2Score());
    }
}
