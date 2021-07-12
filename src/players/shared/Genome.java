package players.shared;

import actions.Action;
import game.GameState;
import rules.ForwardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Genome implements Comparable<Genome> {


    public static Random random;
    public List<Action> actions;
    public double fitness;

    public ForwardModel forwardModel;

    public Genome(ForwardModel forwardModel) {
        super();
        this.forwardModel = forwardModel;
        actions = new ArrayList<>();
        random = new Random();
    }

    public void random(GameState state) {
        List<Action> possible;
        actions.clear();
        fitness = 0;
        final boolean p1Turn = state.isP1Turn();
        while (!state.isTerminal() && p1Turn == state.isP1Turn() && actions.size()<3) { //TODO: remove the size limit after fixing ForwardModel to take action points into account
            possible = state.getPossibleActions();  //TODO: change to not regenerate list when implemented
            if (p1Turn == state.isP1Turn() && possible.isEmpty()) {
                break;
            }

            final int idx = random.nextInt(possible.size());
            actions.add(possible.get(idx));
            forwardModel.step(state, possible.get(idx));
        }
    }

    public void crossover(Genome a, Genome b, GameState state) {
        actions.clear();
        fitness = 0;
        List<Action> possible;
        for (int i = 0; i < Math.max(a.actions.size(), b.actions.size()); i++) {
            possible = state.getPossibleActions();  //TODO: change to not regenerate list when implemented
            if (possible.isEmpty()){
                break;
            }
            if (random.nextBoolean() && hasAction(a, possible, i))
                actions.add(a.actions.get(i));
            else if (hasAction(b, possible, i))
                actions.add(b.actions.get(i));
            else if (hasAction(a, possible, i))
                actions.add(a.actions.get(i));
            else {
                if (hasAction(a, possible, i+1))
                    actions.add(a.actions.get(i+1));
                else if (hasAction(b, possible, i+1))
                    actions.add(b.actions.get(i+1));
                else
                    actions.add(possible.get(random.nextInt(possible.size())));
            }

            forwardModel.step(state, actions.get(i));
        }
    }

    private boolean hasAction(Genome genome, List<Action> possible, int index) {
        if (genome.actions.size() <= index)
            return false;

        if (possible.contains(genome.actions.get(index)))
            return true;

        return false;
    }

    public void mutate(GameState state) {
        if (actions.isEmpty())
            return;

        final int mutIdx = random.nextInt(actions.size());
        List<Action> possible;
        int i = 0;
        for (final Action action : actions) {
            if (i == mutIdx)
                actions.set(mutIdx, newAction(state, action));
            else if (i > mutIdx) {
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

    private Action newAction(GameState state, Action action) {
        final List<Action> possibleActions = state.getPossibleActions();
        possibleActions.remove(action);

        if (possibleActions.isEmpty())
            return null;

        final int idx = random.nextInt(possibleActions.size());

        return possibleActions.get(idx);
    }

    @Override
    public int compareTo(Genome other) {
        if (fitness == other.fitness)
            return 0;
        if (fitness > other.fitness)
            return -1;
        return 1;
    }

    public boolean isLegal(GameState clone) {
        for (final Action action : actions) {
            List<Action> possible = clone.getPossibleActions();
            if (!possible.contains(action))
                return false;
            forwardModel.step(clone, action);
        }
        return true;
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
