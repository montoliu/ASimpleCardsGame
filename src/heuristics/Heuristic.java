package heuristics;

import game.GameState;

public interface Heuristic
{
    public double getScore(GameState gs);
}
