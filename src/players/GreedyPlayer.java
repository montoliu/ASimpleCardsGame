package players;

import actions.Action;
import game.ForwardModel;
import game.Observation;
import heuristics.Heuristic;
import heuristics.SimpleHeuristic;

import java.util.List;

public class GreedyPlayer implements Player
{
    @Override
    public Action act(Observation obs, int budget)
    {
        List<Action> actions = obs.getPossibleActions();
        if (actions.size() == 0) return null;

        Heuristic    h  = new SimpleHeuristic();
        ForwardModel fm = new ForwardModel();

        double best_score = -100000;
        double score;
        Action best_action = null;
        for (Action a : actions)
        {
            Observation temp = obs.deepCopy();
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
