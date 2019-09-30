import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] results;
    private final int t;
    private final double DEV_CONST = 1.96;
    private final double stdev;
    private final double mean;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();
        this.t = trials;
        this.results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int rand = StdRandom.uniform(0, n * n);
                int col = rand % n;
                int row = rand / n;
                perc.open(row + 1, col + 1);
            }
            this.results[i] = ((double) perc.numberOfOpenSites()) / (n * n);
        }
        this.mean = StdStats.mean(this.results);
        this.stdev = StdStats.stddev(this.results);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stdev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - ((DEV_CONST * this.stddev() / Math.sqrt(this.t)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + ((DEV_CONST * this.stddev() / Math.sqrt(this.t)));
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percStats = new PercolationStats(2, 100000);
        System.out.println("mean\t\t\t\t\t= " + percStats.mean());
        System.out.println("stddev\t\t\t\t\t= " + percStats.stddev());
        System.out.println(
                "95% confidence interval\t= [" + percStats.confidenceLo() + ", " + percStats
                        .confidenceHi() + "]");
    }
}
