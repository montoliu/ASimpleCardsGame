package players.mcts;

import actions.Action;
import game.GameState;
import heuristics.SimpleHeuristic;
import rules.SimpleForwardModel;

import java.util.ArrayList;
import java.util.List;

public class MCTSTree
{
    MCTSNode root;
    int      seed = -1;

    public MCTSTree() {  }

    public void setSeed (int seed) { this.seed = seed; }

    public List<Action> getBestTurn(GameState gs, double C, int budget)
    {
        long startTime = System.currentTimeMillis();

        root = new MCTSNode(gs, null,null);
        root.setSeed(seed);

        List<Action> actions = gs.getPossibleActions();
        SimpleForwardModel fm = new SimpleForwardModel();
        SimpleHeuristic    h  = new SimpleHeuristic();

        // First level: we measure the score in all possibilities on the first action point
        for (Action a: actions)
        {
            GameState temp = gs.deepCopy();
            fm.step(temp, a);
            double score = h.getScore(temp);

            MCTSNode node = new MCTSNode(temp, a, root);
            node.setSeed(seed);
            node.updateScore(score);
            node.incrementVisits();

            root.incrementVisits();
            root.addChild(node);
        }

        // main loop
        MCTSNode actual_node = root;
        int i=0;
        while (System.currentTimeMillis() < startTime + budget)
        {
            MCTSNode best_child = actual_node.getBestChildUcb(C);   // get actual_node child according to ucb

            if (best_child.unvisited() || best_child.isTerminal() || best_child.noActionPointsLeft())
            {
                // not yet visited (or terminal) -> rollout and backpropagate score
                double score = best_child.rollout();
                best_child.backpropagation(score);
                actual_node = root;

            }
            else if (!best_child.hasChildren())
            {
                // visited without children
                best_child.extend();
                best_child.rolloutOneRandomChild(seed);
            }
            else
            {
                //it has children, continue traversing
                actual_node = best_child;
            }
            i += 1;
        }

        return recommendTurn(gs.getGameParameters().number_of_action_points);
    }

    public List<Action> recommendTurn(int n_aps)
    {
        List<Action> turn = new ArrayList<>();

        MCTSNode actual_node = root;
        for (int ap=0; ap<n_aps; ap++)
        {
            MCTSNode best_child = actual_node.getBestChildMeanScore();
            turn.add(best_child.getAction());
            actual_node = best_child;
        }

        return turn;
    }

}
