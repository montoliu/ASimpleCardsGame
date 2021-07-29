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
import java.util.HashMap;
import java.util.List;

public class League
{
    public static void main(String[] args) throws IOException
    {
        GameParameters gp      = new GameParameters();
        int            budget  = 500;
        int            n_games = 100;
        int            repetitions  = 10;
        String         outputFile = "testResults//Resultados_500(10x100).txt";


        // Players
        Player pl1 = new RandomPlayer();
        Player pl2 = new GreedyPlayer();
        Player pl3 = new MCTSPlayer(1.4, budget);
        Player pl4 = new OnlineEvolutionPlayerUseIllegal(100, 0.1, 0.5, budget, new SimpleHeuristic());
        Player pl5 = new OnlineEvolutionPlayer(100, 0.1, 0.5, budget, new SimpleHeuristic());
        Player pl6 = new OnlineNTBEAGenomeBased(budget, 100, 10, 0.01, true, true, false, false, false, true, new SimpleHeuristic());

        List<Player>             players = new ArrayList<>();
        HashMap<Integer, Double> points  = new HashMap<>();

        players.add(pl1);
        players.add(pl2);
        players.add(pl3);
        players.add(pl4);
        players.add(pl5);
        players.add(pl6);

        PrintWriter writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);

        while (repetitions > 0)
        {
            points.put(1, 0.0);
            points.put(2, 0.0);
            points.put(3, 0.0);
            points.put(4, 0.0);
            points.put(5, 0.0);
            points.put(6, 0.0);

            int p1_idx = 1;
            for (Player p1 : players)
            {
                int p2_idx = 1;
                for (Player p2 : players)
                {
                    if (p1 != p2)
                    {
                        System.out.print(p1.title() + " vs " + p2.title());
                        writer.print(p1.title() + " vs " + p2.title());
                        List<Integer> results = playTournament(p1,p2,n_games, budget, gp);
                        points.put(p1_idx, points.get(p1_idx) + results.get(0) +results.get(2) / 2.0 );
                        points.put(p2_idx, points.get(p2_idx) + results.get(1) +results.get(2) / 2.0 );
                        System.out.println(" -> " + results.get(0) + " " + results.get(1) + " " + results.get(2));
                        writer.println(" -> " + results.get(0) + " " + results.get(1) + " " + results.get(2));
                        writer.flush();
                    }
                    p2_idx += 1;
                }
                p1_idx += 1;
            }

            System.out.println("\nResults: ");
            writer.println("\nResults: ");
            int idx = 1;
            for (Player p :players)
            {
                System.out.println(p.title() + " -> " + points.get(idx));
                writer.println(p.title() + " -> " + points.get(idx));
                idx += 1;
            }
            repetitions --;
            System.out.println("\n----------------------------\n");
            writer.println("\n----------------------------\n");
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

        game.start();
        game.run(p1, p2, budget);
        return game.getWinner();
    }
}
