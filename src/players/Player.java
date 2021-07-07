package players;

import actions.Action;
import game.Observation;

public interface Player
{
    public Action act(Observation obs, int budget);
    public String title();
    public void   setSeed(int seed);
}
