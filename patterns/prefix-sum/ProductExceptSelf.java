import java.util.Arrays;

/**
 * LeetCode 238 - Product of Array Except Self.
 * Prefix products from the left times suffix products from the right, no division.
 */
public class ProductExceptSelf {
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        res[0] = 1;
        for (int i = 1; i < n; i++) res[i] = res[i - 1] * nums[i - 1];   // left products
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            res[i] *= right;
            right *= nums[i];                                            // running right product
        }
        return res;
    }

    public static void main(String[] args) {
        check(productExceptSelf(new int[] {1, 2, 3, 4}), new int[] {24, 12, 8, 6});
        check(productExceptSelf(new int[] {-1, 1, 0, -3, 3}), new int[] {0, 0, 9, 0, 0});
        System.out.println("ProductExceptSelf: all checks passed");
    }

    static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected) + " but got " + Arrays.toString(actual));
        }
    }
}
