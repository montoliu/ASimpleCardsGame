package game;

import actions.Action;

public class Game
{
    private       GameState      gs;
    private final GameParameters gp;
    private       ForwardModel   fm;
    int           seed;

    public Game(GameParameters gp)
    {
        this.gp   = gp;
        this.seed = -1;
    }

    public void start()
    {
        gs = new GameState(gp);
        fm = new ForwardModel();

        gs.setSeed(seed);
        gs.initialize();
    }

    public void        setSeed(int seed)  { this.seed = seed;           }
    public boolean     notFinished()      { return !gs.isTerminal();    }
    public Observation getObservation()   { return new Observation(gs); }
    public void        step(Action a)     { fm.step(gs, a);             }

    public void       nextPlayerTurn()    { gs.nextPlayerTurn();        }

    public int getWinner()
    {
        if (gs.getP1Score() > gs.getP2Score())      return 1;
        else if (gs.getP1Score() < gs.getP2Score()) return 2;
        else                                        return 0;
    }

    public double getP1Score() { return gs.getP1Score(); }
    public double getP2Score() { return gs.getP2Score(); }
}
