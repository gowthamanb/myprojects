import java.util.Arrays;

/**
 * LeetCode 1310 - XOR Queries of a Subarray.
 * XOR is its own inverse (a ^ a = 0), so xor(l..r) = px[r + 1] ^ px[l].
 */
public class XorQueries {
    public static int[] xorQueries(int[] arr, int[][] queries) {
        int[] px = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            px[i + 1] = px[i] ^ arr[i];
        }
        int[] res = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            res[q] = px[queries[q][1] + 1] ^ px[queries[q][0]];
        }
        return res;
    }

    public static void main(String[] args) {
        int[] got = xorQueries(new int[] {1, 3, 4, 8}, new int[][] {{0, 1}, {1, 2}, {0, 3}, {3, 3}});
        check(got, new int[] {2, 7, 14, 8});
        System.out.println("XorQueries: all checks passed");
    }

    static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected) + " but got " + Arrays.toString(actual));
        }
    }
}
