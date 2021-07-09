package players.mcts;

import actions.Action;
import game.GameState;
import heuristics.SimpleHeuristic;
import rules.SimpleForwardModel;

import java.util.List;

public class MCTSTree
{
    MCTSNode root;

    public MCTSTree() {  }

    public Action getBestAction(GameState gs, int budget)
    {
        root = new MCTSNode(gs, null,null);
        List<Action> actions = gs.getPossibleActions();
        SimpleForwardModel fm = new SimpleForwardModel();
        SimpleHeuristic    h  = new SimpleHeuristic();

        // First level
        for (Action a: actions)
        {
            GameState temp = gs.deepCopy();
            fm.step(temp, a);
            double score = h.getScore(temp);

            MCTSNode node = new MCTSNode(temp, a, root);
            node.updateScore(score);
            node.incrementVisits();

            root.incrementVisits();
            root.addChild(node);
        }

        return root.getBestChild().getAction();
    }


}
