package players.oe;

import actions.Action;
import game.GameState;
import heuristics.Heuristic;
import players.Player;
import players.shared.Genome;
import players.shared.GenomeUseIllegal;
import rules.ForwardModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OnlineEvolutionPlayerUseIllegal implements Player {

    public int popSize;
    public int budget;
    public double killRate;
    public double mutRate;

    public Heuristic heuristicEvaluator;
    public ForwardModel forwardModel;

    public List<Action> actions;
    public final List<GenomeUseIllegal> population;

    private Random random;

    public OnlineEvolutionPlayerUseIllegal(int popSize, double mutRate, double killRate, int budget, Heuristic heuristicEvaluator) {
        super();
        this.popSize = popSize;
        this.mutRate = mutRate;
        this.budget = budget;
        this.heuristicEvaluator = heuristicEvaluator;
        this.killRate = killRate;
        actions = new ArrayList<>();
        population = new ArrayList<>();
        random = new Random();
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

        long startTime = System.currentTimeMillis();

        final List<GenomeUseIllegal> killed = new ArrayList<>();
        GameState stateClone;

        population.clear();

        //Generate a random starting population
        for (int i = 0; i < popSize; i++) {
            stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
            final GenomeUseIllegal genome = new GenomeUseIllegal(state.getNumberActionPoints(), state.getMainDeck().getCards().size() +
                    state.getDiscardDeck().getCards().size() + state.getP1Hand().getNumberOfCards() + state.getP2Hand().getNumberOfCards() + state.getBoard().getNumberOfCards());
            genome.randomLegal(stateClone, forwardModel);
            population.add(genome);
        }

        // Evaluate the initial population
        for (final GenomeUseIllegal genome : population) {
            stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
            for (Action action : genome.actions)
                if (action != null)
                    forwardModel.step(stateClone, action);
            genome.fitness = heuristicEvaluator.getScore(stateClone);
        }

        int generationsCount = 0;

        while (System.currentTimeMillis() < startTime + budget) {
            generationsCount++;

            // Evaluate population
            // Evaluate the newly introduced genomes
            for (final GenomeUseIllegal genome : killed) {
                stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
                for (Action action : genome.actions)
                    if (action != null)
                        forwardModel.step(stateClone, action);
                genome.fitness = heuristicEvaluator.getScore(stateClone);
            }

            // Kill worst genomes
            Collections.sort(population);
            killed.clear();
            final int idx = (int) Math.floor(population.size() * killRate);
            for (int i = idx; i < population.size(); i++)
                killed.add(population.get(i));

            // Crossover new ones
            for (GenomeUseIllegal killedGenome : killed) {
                final int a = random.nextInt(idx);
                int b = random.nextInt(idx);
                while (b == a)
                    b = random.nextInt(idx);

                killedGenome.crossover(population.get(a), population.get(b));

                // Mutation
                if (random.nextDouble() < mutRate) {
                    killedGenome.mutate();
                }
            }
        }

        //System.out.println(title() + "-> Generations: " + generationsCount + ", best fitness: " + population.get(0).fitness);

        actions = population.get(0).actions;
    }

    @Override
    public String title() {
        return "Online Evolution Use Illegal" + killRate;
    }

    @Override
    public void setSeed(int seed) {
        random = new Random(seed);
        Genome.rnd = new Random(random.nextLong());
    }

    @Override
    public void setForwardModel(ForwardModel fm) {
        forwardModel = fm;
    }
}
