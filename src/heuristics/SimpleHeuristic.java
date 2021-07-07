package heuristics;

import game.Observation;

public class SimpleHeuristic implements Heuristic
{
    @Override
    public double getScore(Observation obs)
    {
        return obs.getP1Score() - obs.getP2Score();
    }
}
