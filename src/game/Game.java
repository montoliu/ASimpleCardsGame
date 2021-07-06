package game;

public class Game
{
    private       GameState      gs;
    private final GameParameters gp;
    private       ForwardModel   fm;

    public Game(GameParameters gp)
    {
        this.gp = gp;
    }

    public void start()
    {
        gs = new GameState(gp);
        fm = new ForwardModel();

        gs.initialize();
    }

    public boolean notFinished()  { return gs.isTerminal(); }

    public Observation getObservation()
    {
        return new Observation(gs);
    }

    public void step(Action a)
    {
        fm.step(a);
    }

    public int getWinner()
    {
        if (gs.getP1Score() > gs.getP2Score())
            return 1;
        else if (gs.getP1Score() < gs.getP2Score())
            return 2;
        else return 0;
    }

    public int getP1Score() { return gs.getP1Score(); }
    public int getP2Score() { return gs.getP2Score(); }
}
