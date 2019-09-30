import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private boolean[][] grid;
    private int openSites;
    private WeightedQuickUnionUF wquf;
    private WeightedQuickUnionUF wqufFull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();
        this.n = n;
        this.grid = new boolean[n][n];
        this.wquf = new WeightedQuickUnionUF(n * n + 2);
        this.wqufFull = new WeightedQuickUnionUF(n * n + 1);
        this.openSites = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.grid[i][j] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int cRow = row - 1;
        int cCol = col - 1;
        this.checkRange(cRow, cCol);
        if (!this.grid[cRow][cCol]) {
            this.grid[cRow][cCol] = true;
            int currentPos = cRow * this.n + cCol;
            this.openSites++;

            // check ceiling
            if (cRow == 0) {
                this.wquf.union(currentPos, this.n * this.n);
                this.wqufFull.union(currentPos, this.n * this.n);
            }

            // check floor
            if (cRow == this.n - 1) {
                this.wquf.union(currentPos, this.n * this.n + 1);
            }

            // check top
            if (cRow > 0 && this.grid[cRow - 1][cCol]) {
                int topPos = (cRow - 1) * this.n + cCol;
                this.wquf.union(currentPos, topPos);
                this.wqufFull.union(currentPos, topPos);
            }

            // check bottom
            if (cRow + 1 < this.n && this.grid[cRow + 1][cCol]) {
                int bottomPos = (cRow + 1) * this.n + cCol;
                this.wquf.union(currentPos, bottomPos);
                this.wqufFull.union(currentPos, bottomPos);
            }

            // check right
            if (cCol + 1 < this.n && this.grid[cRow][cCol + 1]) {
                this.wquf.union(currentPos, currentPos + 1);
                this.wqufFull.union(currentPos, currentPos + 1);
            }

            // check left
            if (cCol > 0 && this.grid[cRow][cCol - 1]) {
                this.wquf.union(currentPos, currentPos - 1);
                this.wqufFull.union(currentPos, currentPos - 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int cRow = row - 1;
        int cCol = col - 1;
        this.checkRange(cRow, cCol);
        return this.grid[cRow][cCol];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int cRow = row - 1;
        int cCol = col - 1;
        this.checkRange(cRow, cCol);
        int currentPos = cRow * this.n + cCol;

        return this.wqufFull.connected(this.n * this.n, currentPos);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        int qd = this.n * this.n;
        return this.wquf.connected(qd, qd + 1);
    }

    private void checkRange(int row, int col) {
        if (row >= this.n || col >= this.n || row < 0 || col < 0) {
            throw new java.lang.IllegalArgumentException(
                    row + " or " + col + " is not fit into a range");
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int base = 3;
        Percolation perc = new Percolation(base);
        while (!perc.percolates()) {
            int rand = StdRandom.uniform(0, base * base);
            int col = rand % base;
            int row = rand / base;
            perc.open(row + 1, col + 1);
        }
        System.out.println("Percolates: " + perc.percolates());
        System.out.println("Total Open Sites: " + perc.numberOfOpenSites());
    }
}
