package heuristics;

import game.Observation;

public interface Heuristic
{
    public double getScore(Observation obs);
}
