package players;

import actions.Action;
import game.Game;
import game.GameState;

public interface Player
{
    public Action act(GameState gs, int budget);
    public String title();
    public void   setSeed(int seed);
}
