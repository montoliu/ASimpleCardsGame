package players;

import actions.Action;
import game.Game;
import game.GameState;
import rules.ForwardModel;

public interface Player
{
    public Action act(GameState gs, int budget);
    public String title();
    public void   setSeed(int seed);
    public void   setForwardModel(ForwardModel fm);
    //public void   setHueristic(Heuristic fm);
}
