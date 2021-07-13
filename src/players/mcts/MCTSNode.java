package players.mcts;

import actions.Action;
import game.GameState;
import heuristics.SimpleHeuristic;
import rules.SimpleForwardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTSNode
{
    double         C = 1.4;
    double         score;
    int            visits;
    GameState      gs;
    Action         action;
    MCTSNode       parent;
    List<MCTSNode> children;
    int            seed;

    public MCTSNode(GameState gs, Action action, MCTSNode parent)
    {
        score  = 0.0;
        visits = 0;

        this.gs       = gs;
        this.action   = action;
        this.parent   = parent;
        this.children = new ArrayList<>();

        seed = -1;
    }

    public void setSeed(int seed) { this.seed = seed; }

    public double getMean()                 { return score / (double) visits; }
    public void   incrementVisits()         { visits += 1;                    }
    public void   updateScore(double score) { this.score += score;            }
    public void   addChild(MCTSNode child)  { children.add(child);            }
    public Action getAction()               { return action;                  }

    public MCTSNode getBestChildMeanScore()
    {
        double  best_mean   = Integer.MIN_VALUE;
        MCTSNode best_child = null;

        for (MCTSNode child : children)
        {
            if (child.getMean() >= best_mean)
            {
                best_mean = child.getMean();
                best_child = child;
            }
        }
        return best_child;
    }

    public MCTSNode getBestChildUcb()
    {
        double   best_ucb   = Integer.MIN_VALUE;
        MCTSNode best_child = null;

        Random r;
        if (seed == -1) r = new Random();
        else            r = new Random(seed);

        for (MCTSNode child : children)
        {
            double ucb;
            double epsilon = r.nextDouble() / 1000;
            if (child.visits != 0)
                ucb = child.getMean() + C * Math.sqrt(Math.log(visits) / child.visits) + epsilon;
            else
                ucb = Integer.MAX_VALUE - epsilon;

            if (ucb >= best_ucb)
            {
                best_ucb = ucb;
                best_child = child;
            }
        }
        return best_child;
    }

    public boolean hasChildren() { return children.size() > 0; }
    public boolean unvisited()   { return visits == 0;         }
    public boolean isTerminal()  { return gs.isTerminal();     }
    public boolean noActionPointsLeft() { return gs.getActionPointsLeft() == 0; }

    public void extend()
    {
        List<Action> actions = gs.getPossibleActions();
        SimpleForwardModel fm = new SimpleForwardModel();

        for (Action a: actions)
        {
            GameState temp = gs.deepCopy();
            fm.step(temp, a);

            MCTSNode node = new MCTSNode(temp, a, this);
            node.setSeed(seed);
            addChild(node);
        }
    }

    public void rolloutOneRandomChild(int seed)
    {
        Random r;
        if (seed == -1) r = new Random();
        else            r = new Random(seed);

        int idx = r.nextInt(children.size());

        SimpleHeuristic h = new SimpleHeuristic();

        double score = h.getScore(children.get(idx).gs);
        children.get(idx).updateScore(score);
        children.get(idx).incrementVisits();

        backpropagation(score);
    }

    public double rollout()
    {
        SimpleHeuristic h = new SimpleHeuristic();
        return h.getScore(gs);
    }

    public void backpropagation(double score)
    {
        updateScore(score);
        incrementVisits();

        MCTSNode parent_node = parent;
        while (parent_node != null)
        {
            parent_node.incrementVisits();
            parent_node.updateScore(score);
            parent_node = parent_node.parent;
        }
    }
}
