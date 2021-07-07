package players;

import actions.Action;
import game.GameState;
import game.Observation;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player
{
    int seed;

    public RandomPlayer()
    {
        seed = -1;
    }

    @Override
    public void setSeed(int seed)
    {
        this.seed = seed;
    }

    @Override
    public Action act(GameState gs, int budget)
    {
        List<Action> actions = gs.getPossibleActions();

        if (actions.size() == 0)
            return null;

        Random r;
        if (seed == -1) r = new Random();
        else            r = new Random(seed);

        int idx = r.nextInt(actions.size());
        return actions.get(idx);
    }

    @Override
    public String title()
    {
        return "RandomPlayer";
    }
}
