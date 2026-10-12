/**
 * LeetCode 974 - Subarray Sums Divisible by K.
 * If prefix[i] % k == prefix[j] % k, the subarray between them is divisible by k.
 * The remainder range is known (0..k-1), so an int[] replaces the HashMap.
 */
public class SubarraysDivByK {
    public static int subarraysDivByK(int[] nums, int k) {
        int[] modCount = new int[k];
        modCount[0] = 1;                     // empty prefix
        int sum = 0, count = 0;
        for (int x : nums) {
            sum += x;
            int mod = ((sum % k) + k) % k;   // Java's % can be negative
            count += modCount[mod];
            modCount[mod]++;
        }
        return count;
    }

    public static void main(String[] args) {
        check(subarraysDivByK(new int[] {4, 5, 0, -2, -3, 1}, 5), 7);
        check(subarraysDivByK(new int[] {5}, 9), 0);
        check(subarraysDivByK(new int[] {-1, 2, 9}, 2), 2);   // exercises negative modulo
        System.out.println("SubarraysDivByK: all checks passed");
    }

    static void check(int actual, int expected) {
        if (actual != expected) throw new AssertionError("expected " + expected + " but got " + actual);
    }
}
