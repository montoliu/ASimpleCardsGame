package game;

public class Play
{
    public static void main(String[] args)
    {
        Heuristic      h      = new Heuristic();
        GameParameters gp     = new GameParameters();
        Game           game   = new Game(gp);
        int            budget = 1000;

        Player p1 = new Player();
        Player p2 = new Player();

        game.start();
        while (game.notFinished())
        {
            // Player 1 turn
            for (int ap=0; ap<gp.number_of_action_points; ap++)
            {
                Action a = p1.think(game.getObservation(), h, budget);
                game.step(a);
            }

            if (game.notFinished())
            {
                // Player 2 turn
                for (int ap = 0; ap < gp.number_of_action_points; ap++)
                {
                    Action a = p2.think(game.getObservation(), h, budget);
                    game.step(a);
                }
            }
        }
        int winner = game.getWinner();

        System.out.println("The winner is the player " + winner + " -> Score: " + game.getP1Score() + " - " + game.getP2Score());
    }
}
