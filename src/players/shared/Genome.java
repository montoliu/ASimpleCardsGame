package players.shared;

import actions.Action;
import game.GameState;
import rules.ForwardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Genome implements Comparable<Genome>
{
    public static Random rnd = new Random(); //TODO: is this recreating for every new genome? will it override the value I set it to with setRandom?
    public List<Action> actions;
    public double fitness;

    public ForwardModel forwardModel;

    public Genome(ForwardModel forwardModel)
    {
        super();
        this.forwardModel = forwardModel;
        actions = new ArrayList<>();
    }

    public void random(GameState state)
    {
        List<Action> possible;
        actions.clear();
        fitness = 0;
        final boolean p1Turn = state.isP1Turn();
        while (!state.isTerminal() && p1Turn == state.isP1Turn() && state.getActionPointsLeft()>0)
        {
            possible = state.getPossibleActions();  //TODO: change to not regenerate list when implemented
            if (possible.isEmpty())
                break;

            final int idx = rnd.nextInt(possible.size());
            actions.add(possible.get(idx));
            forwardModel.step(state, possible.get(idx));
        }
    }

    public void crossover(Genome a, Genome b, GameState state)
    {
        actions.clear();
        fitness = 0;
        List<Action> possible;
        for (int i = 0; i < Math.max(a.actions.size(), b.actions.size()); i++)
        {
            possible = state.getPossibleActions();  //TODO: change to not regenerate list when implemented
            if (possible.isEmpty())
                break;

            boolean useA = rnd.nextBoolean();

            if (useA)
            {
                if (hasAction(a, possible, i))
                    actions.add(a.actions.get(i));
                else if (hasAction(b, possible, i))
                    actions.add(b.actions.get(i));
            }
            else
            {
                if (hasAction(b, possible, i))
                    actions.add(b.actions.get(i));
                else if (hasAction(a, possible, i))
                    actions.add(a.actions.get(i));
            }

            if (actions.size() <= i)
                    actions.add(possible.get(rnd.nextInt(possible.size())));

            forwardModel.step(state, actions.get(i));
        }
    }

    private boolean hasAction(Genome genome, List<Action> possible, int index)
    {
        if (genome.actions.size() <= index)
            return false;

        return possible.contains(genome.actions.get(index));
    }

    public void mutate(GameState state) {
        if (actions.isEmpty())
            return;

        final int mutIdx = rnd.nextInt(actions.size());
        List<Action> possible;
        int i = 0;
        for (final Action action : actions)
        {
            if (i == mutIdx)
                actions.set(mutIdx, newAction(state, action));
            else if (i > mutIdx)
            {
                possible = state.getPossibleActions();  //TODO: change to not regenerate list when implemented
                if (!possible.contains(action))
                    if (actions.size() < i+1 && possible.contains(actions.get(i+1))) // Might never be activated
                        actions.set(i, actions.get(i+1));
                    else
                        actions.set(i, newAction(state, action));
            }
            forwardModel.step(state, actions.get(i));
            i++;
        }
    }

    // Return a random action (excluding the current) from the possible ones.
    private Action newAction(GameState state, Action action)
    {
        final List<Action> possibleActions = state.getPossibleActions();
        int idx;
        if (possibleActions.size() > 1)
        {
            possibleActions.remove(action);
            idx = rnd.nextInt(possibleActions.size());
        }
        else
            idx = 0;

        return possibleActions.get(idx);
    }

    @Override
    public int compareTo(Genome other) {
        return Double.compare(other.fitness, fitness);
    }

    @Override
    public String toString() {
        return "Genome [fitness=" + fitness + "]";
    }

    public void copyFrom(Genome other) {
        if (actions.size()==other.actions.size())
            for (int i = 0; i<actions.size(); i++)
                actions.set(i, other.actions.get(i));
        else
            actions = new ArrayList<>(other.actions);
        fitness = other.fitness;
    }
}
