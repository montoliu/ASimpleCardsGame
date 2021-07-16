package players.shared;

import actions.Action;
import game.GameState;
import rules.ForwardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GenomeUseIllegal implements Comparable<GenomeUseIllegal>
{
    public static Random rnd = new Random(); //TODO: is this recreating for every new genome? will it override the value I set it to with setRandom?
    public List<Action> actions;
    public double fitness;

    public int actionsLength;
    public int numberOfCards;

    public GenomeUseIllegal(int actionsLength, int numberOfCards)
    {
        super();
        actions = new ArrayList<>();
        this.actionsLength = actionsLength;
        this.numberOfCards = numberOfCards;
    }

    public void randomLegal(GameState gs, ForwardModel fm)
    {
        List<Action> possible;
        actions.clear();
        fitness = 0;
        final boolean p1Turn = gs.isP1Turn();
        while (!gs.isTerminal() && p1Turn == gs.isP1Turn() && gs.getActionPointsLeft()>0)
        {
            possible = gs.getPossibleActions();  //TODO: change to not regenerate list when implemented
            if (possible.isEmpty())
                break;

            final int idx = rnd.nextInt(possible.size());
            actions.add(possible.get(idx));
            fm.step(gs, possible.get(idx));
        }
    }

    public void crossover(GenomeUseIllegal a, GenomeUseIllegal b)
    {
        actions.clear();
        fitness = 0;
        for (int i = 0; i < Math.max(a.actions.size(), b.actions.size()); i++)
            if (rnd.nextBoolean())
                actions.add(a.actions.get(i));
            else
                actions.add(b.actions.get(i));
    }

    public void mutate() {
        if (actions.isEmpty())
            return;

        final int mutIdx = rnd.nextInt(actions.size());
        actions.set(mutIdx, new Action(rnd.nextInt(numberOfCards), rnd.nextInt(numberOfCards)));
    }

    @Override
    public int compareTo(GenomeUseIllegal other) {
        return Double.compare(other.fitness, fitness);
    }

    @Override
    public String toString() {
        return "GenomeUseIllegal [fitness=" + fitness + "]";
    }

    public void copyFrom(GenomeUseIllegal other) {
        if (actions.size()==other.actions.size())
            for (int i = 0; i<actions.size(); i++)
                actions.set(i, other.actions.get(i));
        else
            actions = new ArrayList<>(other.actions);
        fitness = other.fitness;
    }

}
