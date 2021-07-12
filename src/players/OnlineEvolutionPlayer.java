package players;

import actions.Action;
import game.GameState;
import heuristics.Heuristic;
import players.shared.Genome;
import rules.SimpleForwardModel;
import java.util.*;

public class OnlineEvolutionPlayer implements Player {

    public int popSize;
    public int budget;
    public double killRate;
    public double mutRate;

    public Heuristic heuristicEvaluator;
    public SimpleForwardModel forwardModel; //TODO: change to interface when it's working

    public List<Action> actions;
    public final List<Genome> pop;

    private Random random;

    public OnlineEvolutionPlayer(int popSize, double mutRate, double killRate, int budget, Heuristic heuristicEvaluator) {
        super();
        this.popSize = popSize;
        this.mutRate = mutRate;
        this.budget = budget;
        this.heuristicEvaluator = heuristicEvaluator;
        this.killRate = killRate;
        actions = new ArrayList<>();
        pop = new ArrayList<>();
        random = new Random();

        forwardModel = new SimpleForwardModel(); //TODO: addSetFowardModel to Player and set it from game?
    }

    @Override
    public Action act(GameState state, int budget) {
        if (actions.isEmpty())
            search(state);

        final Action next = actions.get(0);
        actions.remove(0);
        return next;
    }

    public void search(GameState state) {

        long start = System.currentTimeMillis();

        final List<Genome> killed = new ArrayList<>();
        GameState stateClone;

        pop.clear();

        for (int i = 0; i < popSize; i++) {
            stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
            final Genome genome = new Genome(forwardModel);
            genome.random(stateClone);
            pop.add(genome);
        }

        int g = 0;

        while (System.currentTimeMillis() < start + budget) {
            g++;

            // Test pop
            for (final Genome genome : pop) {
                stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
                //System.out.println("Gen " + g + ": " + genome.actions  + stateClone + "\n\n");
                for (Action action : genome.actions)
                    if (action != null)
                        forwardModel.step(stateClone, action);
                genome.fitness = heuristicEvaluator.getScore(stateClone);
            }

            // Kill worst genomes
            Collections.sort(pop);
            killed.clear();
            final int idx = (int) Math.floor(pop.size() * killRate);
            for (int i = idx; i < pop.size(); i++)
                killed.add(pop.get(i));

            // Crossover new ones
            for (Genome killedGenome : killed) {
                final int a = random.nextInt(idx);
                int b = random.nextInt(idx);
                while (b == a)
                    b = random.nextInt(idx);

                stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
                killedGenome.crossover(pop.get(a), pop.get(b), stateClone);

                // Mutation
                if (Math.random() < mutRate) {
                    stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
                    killedGenome.mutate(stateClone);
                }
            }
        }

        //System.out.println(title() + "-> Generations: " + g + ", best fitness: " + pop.get(0).fitness);

        actions = pop.get(0).actions;
    }

    @Override
    public String title() {
        return "Online Evolution";
    }

    @Override
    public void setSeed(int seed) {
        random = new Random(seed);
        Genome.random = new Random(random.nextLong());
    }
}
