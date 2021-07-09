package players;

import actions.Action;
import game.GameState;
import players.mcts.MCTSTree;

public class MCTSPlayer implements Player
{
    int seed;

    public MCTSPlayer()
    {
        seed = -1;
    }

    @Override
    public Action act(GameState gs, int budget)
    {
        MCTSTree tree = new MCTSTree();
        return tree.getBestAction(gs, budget);
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
