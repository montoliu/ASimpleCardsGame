
package players.ntbea;

import java.util.List;

public class TupleStats implements Comparable<TupleStats> {


    private double sum;

    private double mean;

    private boolean strict = false;

    int n;
    boolean valid;

    public TupleStats() {
        n = 0;
        sum = 0;
        valid = false;
    }

    public TupleStats setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public final void reset() {
        n = 0;
        sum = 0;
    }

    static String strictMessage = "No values in summary";

    public double mean() {
        if (strict && n < 1) throw new RuntimeException(strictMessage);
        if (!valid)
            computeMean();
        return mean;
    }

    public double sum() {
        if (strict && n < 1) throw new RuntimeException(strictMessage);
        return sum;
    }

    private void computeMean() {
        if (!valid) {
            mean = sum / n;
        }
    }

    public int n() {
        return n;
    }

    public TupleStats add(double d) {
        n++;
        sum += d;
        valid = false;
        return this;
    }

    public TupleStats add(double... xa) {
        for (double x : xa) {
            add(x);
        }
        return this;
    }

    public TupleStats add(List<Double> xa) {
        for (double x : xa) {
            add(x);
        }
        return this;
    }

    public String toString() {
        return " ave = " + mean() + "\n" +
                  " n   = " + n;

    }

    @Override
    public int compareTo(TupleStats other) {
        return Double.compare(mean(), other.mean());
    }
}