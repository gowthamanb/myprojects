import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 560 - Subarray Sum Equals K.
 * A subarray nums[j..i] sums to k when prefix[j] == prefix[i+1] - k,
 * so count how many earlier prefixes equal (sum - k).
 */
public class SubarraySumEqualsK {
    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> seen = new HashMap<>();
        seen.put(0, 1);              // the empty prefix, so subarrays starting at index 0 are counted
        int sum = 0, count = 0;

        for (int x : nums) {
            sum += x;
            count += seen.getOrDefault(sum - k, 0);   // look up first
            seen.merge(sum, 1, Integer::sum);          // then record
        }
        return count;
    }

    public static void main(String[] args) {
        check(subarraySum(new int[] {1, 2, 3}, 3), 2);       // [1,2], [3]
        check(subarraySum(new int[] {1, -1, 0}, 0), 3);      // [1,-1], [0], [1,-1,0]
        check(subarraySum(new int[] {1, 1, 1}, 2), 2);
        check(subarraySum(new int[] {3}, 3), 1);             // needs the seed seen.put(0, 1)
        System.out.println("SubarraySumEqualsK: all checks passed");
    }

    static void check(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
