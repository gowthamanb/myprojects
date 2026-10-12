import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 325 - Maximum Size Subarray Sum Equals k.
 * Same idea as 560, but the map stores the EARLIEST index of each prefix
 * value instead of a count, so the subarray found is the longest.
 */
public class MaxSubarrayLenSumK {
    public static int maxSubArrayLen(int[] nums, int k) {
        Map<Integer, Integer> firstIdx = new HashMap<>();
        firstIdx.put(0, -1);                 // empty prefix "ends" at index -1
        int sum = 0, best = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (firstIdx.containsKey(sum - k)) {
                best = Math.max(best, i - firstIdx.get(sum - k));
            }
            firstIdx.putIfAbsent(sum, i);    // keep the earliest index -> longest length
        }
        return best;
    }

    public static void main(String[] args) {
        check(maxSubArrayLen(new int[] {1, -1, 5, -2, 3}, 3), 4);   // [1,-1,5,-2]
        check(maxSubArrayLen(new int[] {-2, -1, 2, 1}, 1), 2);      // [-1,2]
        check(maxSubArrayLen(new int[] {1, 2, 3}, 100), 0);
        System.out.println("MaxSubarrayLenSumK: all checks passed");
    }

    static void check(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
