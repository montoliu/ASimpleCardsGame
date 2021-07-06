package game;

import java.util.List;

public class Player
{
    public Player()
    { }

    public Action think(Observation obs, Heuristic h, int budget)
    {
        List<Action> actions = obs.getPossibleActions();
        return new Action(1,2);
    }
}
