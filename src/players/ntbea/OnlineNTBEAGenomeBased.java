package players.ntbea;

import actions.Action;
import game.GameState;
import heuristics.Heuristic;
import players.Player;
import players.shared.Genome;
import rules.ForwardModel;

import java.util.ArrayList;
import java.util.Random;

//Implements the NTBEA algorithm to evolve a turn and play it
//Note that it will only work properly as p2 for now
//Note: plotting the results may worsen performance, not tested yet
//TODO: Make it work on 3-dimensional turns so it works properly on the first turn if its p1
//TODO: try starting with online evolution
//TODO: test what kFactors and eValues are adequate to properly balance exploration & exploitation
public class OnlineNTBEAGenomeBased implements Player {
    private BanditModel model = null;
    private  int modelDimensions = 0;
    private final Heuristic heuristicEvaluator;

    private ArrayList<Action> actions; //todo: maybe change to array or non mutable list for efficiency

    private final int budget;
    private final int nNeighbours;

    private final double kFactor;
    private final double eValue;

    private final boolean use1D;
    private final boolean use2D;
    private final boolean use3D;
    private final boolean use4D;
    private final boolean useND;
    private final boolean useOnlyContiguous;

    ForwardModel forwardModel;
    Random random = new Random();

    public OnlineNTBEAGenomeBased(int budget, int nNeighbours, double kFactor, double eValue, boolean use1D, boolean use2D, boolean use3D, boolean use4D,  boolean useND, boolean useOnlyContiguous, Heuristic heuristicEvaluator){
        actions = new ArrayList<>();
        this.heuristicEvaluator = heuristicEvaluator;
        this.budget = budget;
        this.nNeighbours = nNeighbours;
        this.kFactor = kFactor;
        this.eValue = eValue;
        this.use1D = use1D;
        this.use2D = use2D;
        this.use3D = use3D;
        this.use4D = use4D;
        this.useND = useND;
        this.useOnlyContiguous = useOnlyContiguous;
    }

    @Override
    public Action act(GameState gs, int budget) {
        if (actions.isEmpty())
            runEA(gs);

        final Action next = actions.get(0);
        actions.remove(0);
        return next;
    }

    private void runEA(GameState state) {
        long start = System.currentTimeMillis();
        GameState stateClone;

        //initial turn
        stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
        Genome currentGenome = new Genome(forwardModel);
        currentGenome.random(stateClone);

        //create or reset the model as needed

        if (model == null || modelDimensions != currentGenome.actions.size()-1)
        {
            model = new BanditModel(currentGenome.actions.size()-1, kFactor, eValue, use1D, use2D, use3D, use4D, useND, useOnlyContiguous);
            modelDimensions =  currentGenome.actions.size()-1;
        }
        else
            model.reset();

        int evaluated = 0;
        double bestTurnYetFitness = Float.NEGATIVE_INFINITY;
        ArrayList<Action> bestTurnYet = null;

        double UCB;
        double bestNeighbourUCB;
        Genome bestNeighbourGenome = new Genome(forwardModel);
        Genome currentNeighbour = new Genome(forwardModel);

        while (System.currentTimeMillis() < start + budget) {
            evaluated++;

            stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
            for (Action action : currentGenome.actions)
                if (action != null)
                    forwardModel.step(stateClone, action);

            //evaluate current genome with the game and add the result to the model
            currentGenome.fitness = heuristicEvaluator.getScore(stateClone);
            model.add(currentGenome.actions, currentGenome.fitness);

            //store best
            if (currentGenome.fitness > bestTurnYetFitness) {
                bestTurnYet = new ArrayList<>(currentGenome.actions);
                bestTurnYetFitness = currentGenome.fitness;
            }

            //explore neighbours
            //todo: filter duplicates somehow? use a history similar to the one in OnlineEvolution? prevent crash when setting 0 neighbours?
            //todo: create mutateFrom on genome to prevent recreating the lists
            bestNeighbourUCB = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < nNeighbours; i++) {
                stateClone = state.deepCopy(); //TODO: change to copyFrom when implemented
                currentNeighbour.copyFrom(currentGenome);
                currentNeighbour.mutate(stateClone);
                UCB = model.ucb(currentNeighbour.actions);
                if (UCB > bestNeighbourUCB){
                    bestNeighbourGenome.copyFrom(currentNeighbour);
                    bestNeighbourUCB = UCB;
                }
            }
            currentGenome = bestNeighbourGenome;
        }

        //final result
        actions = bestTurnYet;
    }

    @Override
    public String title() {
        return "OnlineNTBEA(G)" + (use1D? "1D" : "") + (use2D? "2D" : "") + (use3D? "3D" : "") + (use4D? "4D" : "") + (useND? "nD" : "") + (useOnlyContiguous? "OC" : "") + "N" + nNeighbours + "K" + kFactor + "E" + eValue;
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