import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 525 - Contiguous Array.
 * Transform trick: treat 0 as -1. "Equal number of 0s and 1s" becomes
 * "subarray sum == 0", then reuse the longest-subarray template with k = 0.
 */
public class ContiguousArray {
    public static int findMaxLength(int[] nums) {
        Map<Integer, Integer> firstIdx = new HashMap<>();
        firstIdx.put(0, -1);
        int sum = 0, best = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i] == 0 ? -1 : 1;
            if (firstIdx.containsKey(sum)) {
                best = Math.max(best, i - firstIdx.get(sum));
            } else {
                firstIdx.put(sum, i);
            }
        }
        return best;
    }

    public static void main(String[] args) {
        check(findMaxLength(new int[] {0, 1}), 2);
        check(findMaxLength(new int[] {0, 1, 0}), 2);
        check(findMaxLength(new int[] {0, 0, 1, 0, 0, 0, 1, 1}), 6);
        System.out.println("ContiguousArray: all checks passed");
    }

    static void check(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
