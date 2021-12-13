package run;

import game.Game;
import game.GameParameters;
import heuristics.SimpleHeuristic;
import players.GreedyPlayer;
import players.MCTSPlayer;
import players.Player;
import players.RandomPlayer;
import players.ntbea.OnlineNTBEAGenomeBased;
import players.oe.OnlineEvolutionPlayer;
import players.oe.OnlineEvolutionPlayerUseIllegal;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class League
{
    //Class that can be run to play an all vs all league with any number of players and print the results to a csv file
    public static void main(String[] args) throws IOException
    {
        GameParameters gp      = new GameParameters();
        int            budget  = 100;
        int            n_games = 10;
        int            repetitions  = 10;
        String         outputFile = "testResults//Results.csv";

        // Players
        Player pl1 = new RandomPlayer();
        Player pl2 = new GreedyPlayer();
        Player pl3 = new MCTSPlayer(1.4, budget);
        Player pl4 = new MCTSPlayer(7, budget);
        Player pl5 = new MCTSPlayer(14, budget);

        Player pl6 = new OnlineEvolutionPlayerUseIllegal(100, 0.1, 0.5, budget, new SimpleHeuristic());
        Player pl7 = new OnlineEvolutionPlayerUseIllegal(100, 0.1, 0.25, budget, new SimpleHeuristic());
        Player pl8 = new OnlineEvolutionPlayerUseIllegal(100, 0.1, 0.75, budget, new SimpleHeuristic());

        Player pl9 = new OnlineEvolutionPlayer(100, 0.1, 0.25, budget, new SimpleHeuristic());
        Player pl10 = new OnlineEvolutionPlayer(100, 0.1, 0.5, budget, new SimpleHeuristic());
        Player pl11 = new OnlineEvolutionPlayer(100, 0.1, 0.75, budget, new SimpleHeuristic());

        Player pl12 = new OnlineNTBEAGenomeBased(budget, 100, 1.4, 0.01, true, true, false, false, false, true, new SimpleHeuristic());
        Player pl13 = new OnlineNTBEAGenomeBased(budget, 100, 1.4, 0.01, true, true, false, false, false, false, new SimpleHeuristic());
        Player pl14 = new OnlineNTBEAGenomeBased(budget, 100, 1.4, 0.01, true, false, false, false, false, false, new SimpleHeuristic());

        Player pl15 = new OnlineNTBEAGenomeBased(budget, 100, 7  , 0.01, true, true, false, false, false, true, new SimpleHeuristic());
        Player pl16 = new OnlineNTBEAGenomeBased(budget, 100, 14 , 0.01, true, true, false, false, false, true, new SimpleHeuristic());

        List<Player>             players = new ArrayList<>();

        players.add(pl1);  players.add(pl2);  players.add(pl3);  players.add(pl4);  players.add(pl5);
        players.add(pl6);  players.add(pl7);  players.add(pl8);  players.add(pl9);  players.add(pl10);
        players.add(pl11);  players.add(pl12);  players.add(pl13);  players.add(pl14);  players.add(pl15);
        players.add(pl16);


        PrintWriter writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);

        while (repetitions > 0)
        {
            int p1_idx = 1;
            for (Player p1 : players)
            {
                int p2_idx = 1;
                for (Player p2 : players)
                {
                    if (p1 != p2)
                    {
                        System.out.print(repetitions + "," + p1.title() + "," + p2.title());
                        writer.print(repetitions + "," + p1.title() + "," + p2.title());
                        List<Integer> results = playTournament(p1,p2,n_games, budget, gp);
                        System.out.println("," + results.get(0) + "," + results.get(1) + "," + results.get(2));
                        writer.println("," + results.get(0) + "," + results.get(1) + "," + results.get(2));
                        writer.flush();
                    }
                    p2_idx += 1;
                }
                p1_idx += 1;
            }

            repetitions --;
        }
        writer.close();
    }

    private static List<Integer> playTournament(Player p1, Player p2, int n_games, int budget, GameParameters gp)
    {
        int p1_wins = 0;
        int p2_wins = 0;
        int ties    = 0;
        int winner;

        for (int i = 0; i < n_games; i++)
        {
            //System.out.println(i);
            winner = PlayAGame(p1, p2, gp, budget);
            if      (winner == 1) p1_wins += 1;
            else if (winner == 2) p2_wins += 1;
            else                  ties    += 1;
        }

        List<Integer> results = new ArrayList<>();
        results.add(p1_wins);
        results.add(p2_wins);
        results.add(ties);

        return results;
    }

    private static int PlayAGame(Player p1, Player p2, GameParameters gp, int budget)
    {
        Game game = new Game(gp);

        game.Setup();
        game.run(p1, p2, budget);
        return game.getWinner();
    }
}
