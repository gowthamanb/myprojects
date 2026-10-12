import java.util.Arrays;

/**
 * LeetCode 1109 - Corporate Flight Bookings (difference array).
 * Prefix sum in reverse: each range update [l, r] += v is O(1)
 * (diff[l] += v, diff[r + 1] -= v); one prefix pass at the end rebuilds the array.
 */
public class DifferenceArray {
    // bookings[i] = {first, last, seats}, flights are 1-indexed
    public static int[] corpFlightBookings(int[][] bookings, int n) {
        int[] diff = new int[n + 1];
        for (int[] b : bookings) {
            diff[b[0] - 1] += b[2];
            diff[b[1]] -= b[2];
        }
        int[] res = new int[n];
        int running = 0;
        for (int i = 0; i < n; i++) {
            running += diff[i];
            res[i] = running;
        }
        return res;
    }

    public static void main(String[] args) {
        check(corpFlightBookings(new int[][] {{1, 2, 10}, {2, 3, 20}, {2, 5, 25}}, 5),
              new int[] {10, 55, 45, 25, 25});
        check(corpFlightBookings(new int[][] {{1, 2, 10}, {2, 2, 15}}, 2),
              new int[] {10, 25});
        System.out.println("DifferenceArray: all checks passed");
    }

    static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected) + " but got " + Arrays.toString(actual));
        }
    }
}
