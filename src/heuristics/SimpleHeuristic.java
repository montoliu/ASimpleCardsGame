package heuristics;

import game.GameState;
import game.Observation;

public class SimpleHeuristic implements Heuristic
{
    @Override
    public double getScore(GameState gs)
    {
        return gs.getP1Score() - gs.getP2Score();
    }
}
