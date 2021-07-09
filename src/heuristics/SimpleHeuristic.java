package heuristics;

import game.GameState;

public class SimpleHeuristic implements Heuristic
{
    @Override
    public double getScore(GameState gs)
    {
        if (gs.getTurn() == 1)  return gs.getP1Score() - gs.getP2Score();
        else                    return gs.getP2Score() - gs.getP1Score();
    }
}
