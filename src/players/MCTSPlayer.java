package players;

import actions.Action;
import game.GameState;
import players.mcts.MCTSTree;

import java.util.ArrayList;
import java.util.List;

public class MCTSPlayer implements Player
{
    int seed;
    List<Action> actions;

    public MCTSPlayer()
    {
        seed = -1;
        actions = new ArrayList<>();
    }

    @Override
    public Action act(GameState gs, int budget)
    {
        Action action;
        if (gs.getActionPointsLeft() == gs.getNumberActionPoints())
        {
            MCTSTree tree = new MCTSTree();
            actions = tree.getBestTurn(gs, budget);
        }
        action = actions.get(0);
        actions.remove(0);

        return action;
    }

    @Override
    public String title()
    {
        return "MCTSPlayer";
    }

    @Override
    public void setSeed(int seed)
    {
        this.seed = seed;
    }
}
