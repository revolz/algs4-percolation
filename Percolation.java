import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int myN;
    private final boolean[] open;
    private int openSiteCount;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufBackwashFix; // Backwash Fix Attempt

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)    {
        if (n <= 0)
            throw new IllegalArgumentException();

        myN = n;
        open = new boolean[myN * myN];
        openSiteCount = 0;

        for (int row = 1; row <= myN; row++)
            for (int col = 1; col <= myN; col++)
                open[rowCol(row, col)] = false;

        // The additional 2 nodes, one for virtual top node, one for virtual bottom node
        uf = new WeightedQuickUnionUF(myN * myN +2);
        ufBackwashFix = new WeightedQuickUnionUF(myN * myN + 2); // Backwash Fix Attempt
    }

    private int rowCol(int row, int col)    {
        return myN *(row-1)+(col-1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)    {
        if (row < 1 || row > myN)
            throw new IllegalArgumentException();

        if (col < 1 || col > myN)
            throw new IllegalArgumentException();

        if (!isOpen(row, col)) {
            open[rowCol(row, col)] = true;
            openSiteCount++;

            // Top row all connected to the virtual top node
            if (row == 1) {
                uf.union(rowCol(row, col), rowCol(myN, myN)+1);
                ufBackwashFix.union(rowCol(row, col), rowCol(myN, myN)+1);
            }

            // Bottom row all connected to the virtual bottom node, except for Backwash Fix
            if (row == myN) {
                uf.union(rowCol(row, col), rowCol(myN, myN)+2);
            }

            if (row - 1 >= 1)
                if (isOpen(row - 1, col)) {
                    uf.union(rowCol(row, col), rowCol(row - 1, col));
                    ufBackwashFix.union(rowCol(row, col), rowCol(row - 1, col)); // Backwash Fix Attempt
                }

            if (col - 1 >= 1)
                if (isOpen(row, col - 1)) {
                    uf.union(rowCol(row, col), rowCol(row, col - 1));
                    ufBackwashFix.union(rowCol(row, col), rowCol(row, col - 1)); // Backwash Fix Attempt
                }

            if (row + 1 <= myN)
                if (isOpen(row + 1, col)) {
                    uf.union(rowCol(row, col), rowCol(row + 1, col));
                    ufBackwashFix.union(rowCol(row, col), rowCol(row + 1, col)); // Backwash Fix Attempt
                }

            if (col + 1 <= myN)
                if (isOpen(row, col + 1)) {
                    uf.union(rowCol(row, col), rowCol(row, col + 1));
                    ufBackwashFix.union(rowCol(row, col), rowCol(row, col + 1)); // Backwash Fix Attempt
                }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)    {
        if (row < 1 || row > myN)
            throw new IllegalArgumentException();

        if (col < 1 || col > myN)
            throw new IllegalArgumentException();

        return open[rowCol(row, col)];
    }

    // Note, I have only realized/discovered at a much later time that
    // Auto Grader actually uses isFull() to scan for & detect Backwash Error
    // This realization/discovery is the key to figure out & understand the Back Wash Fix Solution
    // is the site (row, col) full?
    public boolean isFull(int row, int col)    {
        if (row < 1 || row > myN)
            throw new IllegalArgumentException();

        if (col < 1 || col > myN)
            throw new IllegalArgumentException();

        if (isOpen(row, col))
            // Now it totally makes sense, after knowing how backwash is being detected & flagged up
            // The Backwash Fix doesn't connect to virtual bottom node, hence there is no backwash
            return ufBackwashFix.find(rowCol(row, col)) == ufBackwashFix.find(rowCol(myN, myN)+1);
        else
            return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites()    {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates()    {
        return uf.find(rowCol(myN, myN)+1) == uf.find(rowCol(myN, myN)+2);
    }

    // test client (optional)
    public static void main(String[] args)    {
        // empty main function
    }
}