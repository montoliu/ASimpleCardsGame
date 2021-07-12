package game;

import actions.Action;
import players.Player;
import rules.ForwardModel;
import rules.SimpleForwardModel;

public class Game
{
    private       GameState      gs;
    private final GameParameters gp;
    private final ForwardModel   fm;
    int           seed;

    public Game(GameParameters gp)
    {
        this.gp   = gp;
        this.seed = gp.seed;
        this.fm   = gp.fm;
    }

    public void start()
    {
        gs = new GameState(gp);
        gs.setSeed(seed);
        gs.initialize();
    }

    public void run(Player p1, Player p2, int budget)
    {
        p1.setForwardModel(fm);
        p2.setForwardModel(fm);

        p1.setSeed(seed);
        p2.setSeed(seed);

        while (notFinished())
        {
            // Player 1 turn
            while (gs.getActionPointsLeft() > 0)
            {
                Action a = p1.act(getObservation(), budget);
                if (a == null) break;
                step(a);
            }

            nextPlayerTurn();
            if (notFinished())
            {
                // Player 2 turn
                while (gs.getActionPointsLeft() > 0)
                {
                    Action a = p2.act(getObservation(), budget);
                    if (a == null) break;
                    step(a);
                }
            }
            nextPlayerTurn();
        }
    }

    public void        setSeed(int seed)  { this.seed = seed;           }
    public boolean     notFinished()      { return !gs.isTerminal();    }
    public void        step(Action a)     { fm.step(gs, a);             }
    public GameState   getObservation()   { return gs.getObservation(); }
    public void        nextPlayerTurn()   { gs.nextPlayerTurn();        }


    public int getWinner()
    {
        if (gs.getP1Score() > gs.getP2Score())      return 1;
        else if (gs.getP1Score() < gs.getP2Score()) return 2;
        else                                        return 0;
    }

    public double getP1Score() { return gs.getP1Score(); }
    public double getP2Score() { return gs.getP2Score(); }
}
