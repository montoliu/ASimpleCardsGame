package players;

import actions.Action;
import game.GameState;
import players.mcts.MCTSTree;

import java.util.ArrayList;
import java.util.List;

public class MCTSPlayer implements Player
{
    int seed;
    List<Action> turn;

    public MCTSPlayer()
    {
        seed = -1;
        turn = new ArrayList<>();
    }

    @Override
    public Action act(GameState gs, int budget)
    {
        // In the first action point the player thinks the complete turn
        // In the following turs, just get the first action in the remaining action included in the turn
        Action action;
        if (gs.getActionPointsLeft() == gs.getNumberActionPoints())
        {
            turn.clear();
            MCTSTree tree = new MCTSTree();
            tree.setSeed(seed);

            turn = tree.getBestTurn(gs, budget);
        }
        action = turn.get(0);
        turn.remove(0);

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
