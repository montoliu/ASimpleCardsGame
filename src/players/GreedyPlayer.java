package players;

import actions.Action;
import game.GameState;
import rules.SimpleForwardModel;
import heuristics.Heuristic;
import heuristics.SimpleHeuristic;

import java.util.List;

public class GreedyPlayer implements Player
{
    @Override
    public Action act(GameState gs, int budget)
    {
        List<Action> actions = gs.getPossibleActions();
        if (actions.size() == 0) return null;

        Heuristic           h  = new SimpleHeuristic();
        SimpleForwardModel fm = new SimpleForwardModel();

        double best_score = Integer.MIN_VALUE;
        double score;
        Action best_action = null;

        for (Action a : actions)
        {
            GameState temp = gs.deepCopy();
            fm.step(temp, a);

            score = h.getScore(temp);
            if (score > best_score)
            {
                best_score = score;
                best_action = a;
            }
        }

        return best_action;
    }

    @Override
    public String title()
    {
        return "GreedyPlayer";
    }

    @Override
    public void setSeed(int seed)
    {  }
}
