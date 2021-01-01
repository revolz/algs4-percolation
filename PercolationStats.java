import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_CONSTANT = 1.96;

    private final double[] openSiteCountRecord;
    private final int myT;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n < 1 || trials < 1)
            throw new IllegalArgumentException();

        myT = trials;
        openSiteCountRecord = new double[trials];

        for (int run = 0; run < trials; run++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(n)+1, StdRandom.uniform(n)+1);
            }

            openSiteCountRecord[run] = (double) percolation.numberOfOpenSites() / (n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSiteCountRecord);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSiteCountRecord);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_CONSTANT * stddev() / Math.sqrt(myT);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_CONSTANT * stddev() / Math.sqrt(myT);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats st = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean = " + st.mean());
        StdOut.println("stddev = " + st.stddev());
        StdOut.println("95% confidence interval = [" + st.confidenceLo() + ", " + st.confidenceHi() + "]");
    }
}