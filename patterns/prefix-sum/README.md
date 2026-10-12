# Prefix Sum Pattern (Java)

Precompute running totals so that the sum of **any range is a single subtraction**.
When a problem asks *how many* or *which* subarrays satisfy a condition, store the
earlier prefixes in a `HashMap` and look up the one you need at each step.

Every `.java` file in this folder is self-contained, with a `main` that runs its examples
as checks. See [Running the examples](#running-the-examples).

## 1. The core idea

Convention: **`prefix[i]` = sum of the first `i` elements** (`nums[0..i-1]`), so `prefix[0] = 0`.

```
nums   =    [ 3,  1,  4,  1,  5 ]
index       0   1   2   3   4

prefix = [ 0, 3,  4,  8,  9, 14 ]
index    0  1   2   3   4   5
```

**Key identity:**

```
sum(nums[l..r]) = prefix[r + 1] - prefix[l]
```

`prefix[r+1]` is `nums[0] + … + nums[r]` and `prefix[l]` is `nums[0] + … + nums[l-1]`.
Subtracting cancels the shared start and leaves `nums[l] + … + nums[r]`.
Example: `sum(1..3) = 1 + 4 + 1 = 6 = prefix[4] - prefix[1] = 9 - 3`.

| Approach | Preprocess | Per query | Q queries |
|---|---|---|---|
| Brute force | none | O(n) | O(n·Q) |
| Prefix sum | O(n) | O(1) | O(n + Q) |

Code: [`RangeSum.java`](RangeSum.java) (LC 303)

## 2. Prefix sum + HashMap: count subarrays with sum k (LC 560)

A subarray `nums[j..i]` sums to `k` when

```
prefix[i+1] - prefix[j] = k   ⟺   prefix[j] = prefix[i+1] - k
```

So at each step ask: **how many earlier prefixes equal `sum - k`?**
A `HashMap<prefixValue, count>` answers that in O(1).

```java
public int subarraySum(int[] nums, int k) {
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
```

- **`seen.put(0, 1)`** is the leading `prefix[0] = 0`. Without it, subarrays starting at index 0 are missed (`[3]`, `k = 3`).
- **Look up before recording.** Otherwise, when `k = 0`, the current prefix matches itself and an *empty* subarray is counted.
- **The map stores counts**, because the same prefix value can occur at several start indices.

Trace for `nums = [1, -1, 0]`, `k = 0`:

| x | sum | sum − k | found | count | seen after |
|---|---|---|---|---|---|
| — | 0 | — | — | 0 | {0:1} |
| 1 | 1 | 1 | 0 | 0 | {0:1, 1:1} |
| −1 | 0 | 0 | 1 → `[1,-1]` | 1 | {0:2, 1:1} |
| 0 | 0 | 0 | 2 → `[0]`, `[1,-1,0]` | 3 | {0:3, 1:1} |

**Why not a sliding window?** A sliding window needs the sum to grow as the window grows.
Negative numbers and zeros break that. This pattern works with any integers.
Complexity: O(n) time and O(n) space, compared with O(n²) for brute force.

Code: [`SubarraySumEqualsK.java`](SubarraySumEqualsK.java)

## 3. Variations

| Variation | What changes | Code |
|---|---|---|
| Longest subarray with sum k (LC 325) | Map stores the **first index**, seeded with `0 → -1`; use `putIfAbsent` | [`MaxSubarrayLenSumK.java`](MaxSubarrayLenSumK.java) |
| Equal 0s and 1s (LC 525) | Transform `0 → -1`, then look for the longest subarray with sum 0 | [`ContiguousArray.java`](ContiguousArray.java) |
| Divisible by k (LC 974) | Key on `((sum % k) + k) % k`; an `int[k]` replaces the map | [`SubarraysDivByK.java`](SubarraysDivByK.java) |
| XOR of a range (LC 1310) | XOR is its own inverse: `px[r+1] ^ px[l]` | [`XorQueries.java`](XorQueries.java) |
| Product except self (LC 238) | Prefix products × suffix products, no division | [`ProductExceptSelf.java`](ProductExceptSelf.java) |
| Pivot index (LC 724) | `leftSum == total - leftSum - nums[i]` | [`PivotIndex.java`](PivotIndex.java) |
| 2D rectangle sums (LC 304) | `P[r+1][c+1] = m[r][c] + P[r][c+1] + P[r+1][c] - P[r][c]`; inclusion–exclusion per query | [`RangeSum2D.java`](RangeSum2D.java) |
| Many range updates (LC 1109) | **Difference array**: `diff[l] += v; diff[r+1] -= v`, then one prefix pass | [`DifferenceArray.java`](DifferenceArray.java) |

2D query, with inclusion–exclusion (big rectangle − strip above − strip to the left + doubly subtracted corner):

```java
int sum = P[r2 + 1][c2 + 1] - P[r1][c2 + 1] - P[r2 + 1][c1] + P[r1][c1];
```

## 4. Real-world uses

- **Databases and analytics**: running totals (`SUM() OVER (ORDER BY …)`), revenue to date.
- **Image processing**: summed-area tables, also called integral images (2D prefix sums). They give O(1) box blur and fast features in the Viola–Jones face detector.
- **Time series and monitoring**: counts over any time window, moving averages.
- **Games and maps**: the total cost or resources in a rectangular region.
- **Scheduling**: difference arrays for calendar overlaps and resource allocation.
- **Parallel computing**: "scan" (parallel prefix sum) is a core GPU primitive.

## 5. When to apply it

Reach for prefix sums when you see:

1. **Sum, count or XOR of a range**, especially with many queries on a static array.
2. **"Number of subarrays with sum = k"** (or divisible by k, or XOR = k) → prefix + map of counts.
3. **"Longest/shortest subarray with property X"**, where X is a difference of prefixes → prefix + map of first index.
4. **Negative numbers or zeros**, where a sliding window fails.
5. **"Equal number of A and B"** → map one to +1 and the other to −1, then find sum 0.
6. **"Divisible by k"** → prefix mod k.
7. **Rectangle sums in a matrix** → 2D prefix.
8. **Many range updates, one final read** → difference array.
9. **"Everything except index i"** → prefix and suffix arrays.

> **Checklist:** can the target be written as `f(prefix up to i) − f(prefix up to j)`?
> If yes, iterate `i` and look up the matching `j` in a map or array.

### When not to use it

| Situation | Better tool |
|---|---|
| Values change between queries | Fenwick tree (BIT) or segment tree: O(log n) update and query |
| All values positive, longest/shortest window with sum ≤/≥ k | Sliding window (O(1) space) |
| Range min/max/GCD (no inverse operation) | Sparse table or segment tree |

Prefix sums only work for operations that have an **inverse** (`+` has `−`, XOR has XOR).

## 6. Java pitfalls

1. **Forgetting the seed**: `prefix[0] = 0`, `seen.put(0, 1)`, `firstIdx.put(0, -1)`.
2. **Off-by-one errors**: use an array of size `n + 1` and `sum(l..r) = prefix[r+1] - prefix[l]`.
3. **Overflow**: use `long[]` or `Map<Long, Integer>` for large sums.
4. **Negative modulo**: in Java, `-7 % 3 == -1`. Normalise with `((x % k) + k) % k`.
5. **Recording before looking up**: this counts empty subarrays.
6. **Overwriting the first index** in "longest" problems: use `putIfAbsent`.
7. **Autoboxing cost**: when keys fall in a small known range (as with mod k), use an `int[]` instead of a `HashMap`.

## 7. Practice path: beginner to advanced

Work through the problems in order. Each one adds a single new idea on top of the ones before it.
Solve it yourself first, then compare with the solution file in this folder where there is one.

### Level 1: Build and use a prefix array

| # | Problem | Difficulty | New idea | Solution |
|---|---|---|---|---|
| 1 | [LC 1480 Running Sum of 1d Array](https://leetcode.com/problems/running-sum-of-1d-array/) | Easy | Build a running total | — |
| 2 | [LC 303 Range Sum Query – Immutable](https://leetcode.com/problems/range-sum-query-immutable/) | Easy | `sum(l..r) = prefix[r+1] - prefix[l]` and the leading 0 | [`RangeSum.java`](RangeSum.java) |
| 3 | [LC 724 Find Pivot Index](https://leetcode.com/problems/find-pivot-index/) | Easy | Running left sum + total gives the right sum | [`PivotIndex.java`](PivotIndex.java) |
| 4 | [LC 238 Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) | Medium | Prefix from the left and suffix from the right; works for products too | [`ProductExceptSelf.java`](ProductExceptSelf.java) |
| 5 | [LC 1310 XOR Queries of a Subarray](https://leetcode.com/problems/xor-queries-of-a-subarray/) | Medium | Any operation with an inverse works (XOR) | [`XorQueries.java`](XorQueries.java) |

### Level 2: Prefix sum + HashMap

| # | Problem | Difficulty | New idea | Solution |
|---|---|---|---|---|
| 6 | [LC 560 Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) | Medium | Look up `sum - k` in a map of **counts**; seed `0 → 1` | [`SubarraySumEqualsK.java`](SubarraySumEqualsK.java) |
| 7 | [LC 325 Maximum Size Subarray Sum Equals k](https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/) *(Premium)* | Medium | Map of **first index**; seed `0 → -1`; `putIfAbsent` | [`MaxSubarrayLenSumK.java`](MaxSubarrayLenSumK.java) |
| 8 | [LC 525 Contiguous Array](https://leetcode.com/problems/contiguous-array/) | Medium | Transform the input (`0 → -1`) so the condition becomes sum = 0 | [`ContiguousArray.java`](ContiguousArray.java) |
| 9 | [LC 974 Subarray Sums Divisible by K](https://leetcode.com/problems/subarray-sums-divisible-by-k/) | Medium | Key on the remainder; fix Java's negative `%` | [`SubarraysDivByK.java`](SubarraysDivByK.java) |
| 10 | [LC 523 Continuous Subarray Sum](https://leetcode.com/problems/continuous-subarray-sum/) | Medium | Remainder + first index, with a minimum-length constraint | — |

### Level 3: Advanced

| # | Problem | Difficulty | New idea | Solution |
|---|---|---|---|---|
| 11 | [LC 1109 Corporate Flight Bookings](https://leetcode.com/problems/corporate-flight-bookings/) | Medium | Difference array: O(1) range updates, then one prefix pass | [`DifferenceArray.java`](DifferenceArray.java) |
| 12 | [LC 304 Range Sum Query 2D – Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/) | Medium | 2D prefix sums and inclusion–exclusion | [`RangeSum2D.java`](RangeSum2D.java) |
| 13 | [LC 1442 Count Triplets That Can Form Two Arrays of Equal XOR](https://leetcode.com/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/) | Medium | Prefix XOR + map that tracks both counts and sums of indices | — |
| 14 | [LC 1074 Number of Submatrices That Sum to Target](https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/) | Hard | Fix a pair of rows, collapse to 1D, then apply the LC 560 technique | — |

**Tip:** If LC 325 is locked, practise the same technique with LC 525 and come back to 325 later.

## Running the examples

Each file has no package and a single public class, so Java 11+ can run it directly:

```bash
cd patterns/prefix-sum
java SubarraySumEqualsK.java
```

Or compile all of them and run every check:

```bash
cd patterns/prefix-sum
javac -d out *.java
for f in *.java; do java -cp out "${f%.java}"; done
```
