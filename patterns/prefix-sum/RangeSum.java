/**
 * LeetCode 303 - Range Sum Query (Immutable).
 * Basic template: prefix[i] = sum of the first i elements, so
 * sum(l..r) = prefix[r + 1] - prefix[l].
 */
public class RangeSum {
    private final long[] prefix;   // long to avoid overflow

    public RangeSum(int[] nums) {
        prefix = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
    }

    // inclusive l..r
    public long sum(int l, int r) {
        return prefix[r + 1] - prefix[l];
    }

    public static void main(String[] args) {
        RangeSum rs = new RangeSum(new int[] {3, 1, 4, 1, 5});
        check(rs.sum(1, 3), 6);
        check(rs.sum(0, 4), 14);
        check(rs.sum(2, 2), 4);
        System.out.println("RangeSum: all checks passed");
    }

    static void check(long actual, long expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
