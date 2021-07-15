package players.ntbea;

import actions.Action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

//TODO: unit testing
//Defines a bandit as an N-Tuple that samples a set of dimensions. Each of the arms of the bandit uses ActionArrayPatterns as hash keys that index TupleStats.
public class BanditNTuple {

    public int[] tuplePositions;
    public HashMap<ActionArrayPattern, TupleStats> armsMap;

    public int nSamples;
    public double kFactor;
    public double eValue;
    int nEntries;

    public BanditNTuple(int[] tuplePositions, double kFactor, double eValue) {
        this.tuplePositions = tuplePositions;
        this.kFactor = kFactor;
        this.eValue = eValue;
        reset();
    }

    public void reset() {
        nSamples = 0;
        nEntries = 0;
        armsMap = new HashMap<>();
    }

    //Add the value v to the corresponding arm, creating it if it doesn't exist.
    //Note that if the arm doesn't exist it will add it to the bandit, create the states and then add the value.
    public void add(Action[] x, double v) {
        TupleStats stats = getStatsForceCreate(x);
        stats.add(v);
        nSamples++;
    }

    public void add(List<Action> x, double v) {
        TupleStats stats = getStatsForceCreate(x);
        stats.add(v);
        nSamples++;
    }


    //Method for printing the contents of the bandit. WILL REQUIRE COMPARABLE IMPLEMENTATION OR A DIFFERENT APPROACH. UNTESTED.
    public void printArms() {
        TreeSet<ActionArrayPattern> orderedKeys = new TreeSet<>();
        orderedKeys.addAll(armsMap.keySet());
        for (ActionArrayPattern key : orderedKeys) {
            TupleStats ss = armsMap.get(key);
            System.out.println(key + "\t " + ss.n() + "\t " + ss.mean());
        }
    }

    //Try to get the arm corresponding to the given action and the pattern of this bandit.
    public TupleStats getStatsForceCreate(Action[] x) {
        ActionArrayPattern key = new ActionArrayPattern().setPattern(x, tuplePositions);
        TupleStats stats = armsMap.get(key);
        if (stats == null) {
            stats = new TupleStats();
            nEntries++;
            armsMap.put(key, stats);
        }
        return stats;
    }

    public TupleStats getStatsForceCreate(List<Action> x) {
        ActionArrayPattern key = new ActionArrayPattern().setPattern(x, tuplePositions);
        TupleStats stats = armsMap.get(key);
        if (stats == null) {
            stats = new TupleStats();
            nEntries++;
            armsMap.put(key, stats);
        }
        return stats;
    }

    public String toString() {
        return tuplePositions.length + "\t " + Arrays.toString(tuplePositions) + "\t " + nSamples  + "\t " + nEntries;
    }

    //Get the UCB for an arm.
    //Note that it will crash when called before adding anything to the bandit.
    //todo: prevent that crash, not really needed though.
    // TODO: eValue random?
    public double ucb(Action[] x) {
        ActionArrayPattern key = new ActionArrayPattern().setPattern(x, tuplePositions);
        TupleStats armStats = armsMap.get(key);
        if (armStats != null)
            return armStats.mean() + kFactor * Math.sqrt(Math.log(1+nSamples)/(armStats.n+eValue));
        else
            return kFactor * Math.sqrt(Math.log(1+nSamples)/(eValue)); }

    public double ucb(List<Action> x) {
        ActionArrayPattern key = new ActionArrayPattern().setPattern(x, tuplePositions);
        TupleStats armStats = armsMap.get(key);
        if (armStats != null)
            return armStats.mean() + kFactor * Math.sqrt(Math.log(1+nSamples)/(armStats.n+eValue));
        else
            return kFactor * Math.sqrt(Math.log(1+nSamples)/(eValue));
    }
}
