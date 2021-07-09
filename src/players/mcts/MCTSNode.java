package players.mcts;

import actions.Action;
import game.GameState;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode
{
    double    score;
    int       visits;
    GameState gs;
    Action action;
    MCTSNode  parent;
    List<MCTSNode> children;

    public MCTSNode(GameState gs, Action action, MCTSNode parent)
    {
        score  = 0.0;
        visits = 0;

        this.gs       = gs;
        this.action   = action;
        this.parent   = parent;
        this.children = new ArrayList<>();
    }

    public double getMean() { return score / (double) visits; }

    public void incrementVisits()         { visits += 1;        }
    public void updateScore(double score) { this.score += score;}

    public void addChild(MCTSNode child)  { children.add(child); }
    public Action getAction() {return action; }

    public MCTSNode getBestChild()
    {
        double  best_mean   = -10000000;
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
}
