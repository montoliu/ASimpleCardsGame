package players.ntbea;

import actions.Action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//TODO: does this need to implement comparable?

public class ActionArrayPattern /*implements Comparable<ActionArrayPattern> */{
    public int[] v;

    public ActionArrayPattern setPattern(Action[] x, int[] ix) {
        v = new int[ix.length];
        for (int i=0; i<ix.length; i++) {
            v[i] = x[ix[i]].hashCode();
        }
        return this;
    }

    public ActionArrayPattern setPattern(List<Action> x, int[] ix) {
        v = new int[ix.length];
        for (int i=0; i<ix.length; i++) {
            v[i] = x.get(ix[i]).hashCode();
        }
        return this;
    }

    public int hashCode() {
        return Arrays.hashCode(v);
    }

    public  boolean equals(Object pattern) {
        try {
            ActionArrayPattern p = (ActionArrayPattern) pattern;
            for (int i = 0; i < v.length; i++) {
                if (v[i] != p.v[i]) return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

/*
    public int compareTo(ActionArrayPattern p) {
        try {
            // now iterate over all the values
            for (int i=0; i<v.length; i++) {
                if (v[i] > p.v[i]) {
                    return 1;
                }
                if (v[i] < p.v[i]) {
                    return -1;
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
*/

    public String toString() {
        return Arrays.toString(v);
    }
}
