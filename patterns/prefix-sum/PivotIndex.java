/**
 * LeetCode 724 - Find Pivot Index.
 * One running prefix plus the total: right sum = total - leftSum - nums[i].
 */
public class PivotIndex {
    public static int pivotIndex(int[] nums) {
        int total = 0;
        for (int x : nums) total += x;
        int leftSum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (leftSum == total - leftSum - nums[i]) return i;
            leftSum += nums[i];
        }
        return -1;
    }

    public static void main(String[] args) {
        check(pivotIndex(new int[] {1, 7, 3, 6, 5, 6}), 3);
        check(pivotIndex(new int[] {1, 2, 3}), -1);
        check(pivotIndex(new int[] {2, 1, -1}), 0);
        System.out.println("PivotIndex: all checks passed");
    }

    static void check(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
