/**
 * LeetCode 304 - Range Sum Query 2D (Immutable).
 * P[r][c] = sum of the rectangle (0,0)..(r-1,c-1). Queries use inclusion-exclusion.
 */
public class RangeSum2D {
    private final int[][] P;

    public RangeSum2D(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        P = new int[m + 1][n + 1];
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                P[r + 1][c + 1] = matrix[r][c] + P[r][c + 1] + P[r + 1][c] - P[r][c];
    }

    // rectangle (r1,c1) to (r2,c2) inclusive
    public int sumRegion(int r1, int c1, int r2, int c2) {
        return P[r2 + 1][c2 + 1] - P[r1][c2 + 1] - P[r2 + 1][c1] + P[r1][c1];
    }

    public static void main(String[] args) {
        RangeSum2D rs = new RangeSum2D(new int[][] {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        });
        check(rs.sumRegion(2, 1, 4, 3), 8);
        check(rs.sumRegion(1, 1, 2, 2), 11);
        check(rs.sumRegion(1, 2, 2, 4), 12);
        System.out.println("RangeSum2D: all checks passed");
    }

    static void check(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
