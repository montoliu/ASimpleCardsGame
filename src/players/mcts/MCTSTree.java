package players.mcts;

import actions.Action;
import game.GameState;
import heuristics.SimpleHeuristic;
import rules.SimpleForwardModel;

import java.util.List;

public class MCTSTree
{
    MCTSNode root;
    int      seed = -1;

    public MCTSTree() {  }

    public void setSeed (int seed) { this.seed = seed; }

    public List<Action> getBestTurn(GameState gs, int budget)
    {
        root = new MCTSNode(gs, null,null);
        List<Action> actions = gs.getPossibleActions();
        SimpleForwardModel fm = new SimpleForwardModel();
        SimpleHeuristic    h  = new SimpleHeuristic();

        // First level: we measure the score in all possibilities on the first action points
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

        // main loop
        MCTSNode actual_node = root;
        int i=0;
        while (i<1000)  // AQUI tendria que ser por budget
        {
            MCTSNode best_child = actual_node.get_best_child_ucb();

            if (best_child.unvisited() || best_child.isTerminal())
            {
                // not yet visited (or terminal) -> rollout
                double score = best_child.rollout();
                best_child.backpropagate(score);
                actual_node = root;

            }
            else if (!best_child.hasChildren())
            {
                // visited without children
                best_child.extend();
                best_child.rollout_one_random_child(seed);
            }
            else
            {
                //it has children, continue traversing
                actual_node = best_child;
            }
            i += 1;
        }

        return null; //recommendTurn();
    }


}
