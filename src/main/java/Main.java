import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) {
        int[] primes = new int[] {2,3,5,7};

        String num = "277777788888899";
        BigDecimal bigDec;
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);

        final int[] digits = findDigits(num);
        String number;
        ArrayList primesHad = new ArrayList<String>();

        ArrayList<String> permutations = getPermutations(digits);
        permutations.add(num);
        // This all works good and as intended. It's very good.
        while (permutations.size() > 0) {
            number = permutations.get(0);
            if (divisible(number)) {
                bigDec = new BigDecimal(number);
                int startingPrime = 0;
                boolean firstTrip = false;
                for (int i = 0; i < primes.length; i++) {
                    if(i == startingPrime && startingPrime != primes.length - 1) {
                        if (firstTrip) {
                            startingPrime++;
                            primesHad.add(bigDec.toString());
                            primesHad.add("|");
                            bigDec = new BigDecimal(permutations.get(0));
                            number = permutations.get(0);
                            i = startingPrime;
                        }
                        firstTrip = true;
                    }
                    bigDec = bigDec.divide(BigDecimal.valueOf(primes[i]), mc);
                    while(bigDec.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                        number = bigDec.toString();
                        primesHad.add(primes[i]);
                        bigDec = bigDec.divide(BigDecimal.valueOf(primes[i]), mc);
                    }
                    bigDec = new BigDecimal(number);
                    if (bigDec.toString().length() == 1) {
                        primesHad.add(bigDec.toString());
                        for (Object o : primesHad) {
                            System.out.print(o.toString() + " ");
                        }
                        System.out.println();
                        break;
                    }
                    else if (i == primes.length - 1) {
                        // If we started at 7, stop.
                        if (startingPrime == primes.length - 1) {
                            primesHad.add(bigDec.toString());
                            firstTrip = false;
                            break;
                        }
                        else {
                            i = -1;
                        }
                    }
                }
            }
            primesHad.clear();
            permutations.remove(0);
        }
    }

    // Stolen mostly from https://www.nayuki.io/page/next-lexicographical-permutation-algorithm.
    private static ArrayList<String> getPermutations(int[] digitsGiven) {
        ArrayList<String> permutations = new ArrayList<>();

        // Builds the lowest possible permutation of a number given its digits.
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            while (digitsGiven[i] > 0) {
                stringBuilder.append(i);
                digitsGiven[i]--;
            }
        }

        String number = stringBuilder.toString();
        // Starting the algorithm to find the next permutation.
        int k = 0;
        int l;
        while (k >= 0) {

            // Getting the largest index of k such that a[k] < a[k + 1]. If k < 0, it's the last permutation.
            k = number.length() - 2;
            while (number.charAt(k) >= number.charAt(k + 1)) {
                k--;
                // Checking if we've reached the last permutation.
                if (k < 0)
                    return permutations;
            }

            // Find the rightmost element greater than the pivot.
            l = number.length() - 1;
            while (number.charAt(l) <= number.charAt(k) && l > k)
                l--;

            // Assertion: charAt(k) < charAt(i).
            number = swap(number, k, l);

            permutations.add(number);
        }
        return permutations;
    }

    // Assertion: charAt(i) < charAt(k).
    private static String swap(String number, int k, int l) {
        char[] array = number.toCharArray();
        // Swapping i and k positions.
        char temp = array[l];
        array[l] = array[k];
        array[k] = temp;
        String reversed;
        if (++k == array.length)
            return String.valueOf(array);
        else {
            reversed = new StringBuilder(String.valueOf(array).substring(k)).reverse().toString();
        }
        return String.valueOf(array).substring(0, k) + reversed;
    }

    // Determines if the number is divisible by 2, 3, 5
    private static boolean divisible(String number) {
                              // 2                                         3                                     5
        return ((Double.parseDouble(number) / 2) % 1 == 0) || (sum(number) % 3) == 0 || (number.charAt(number.length() - 1) == '0') || (number.charAt(number.length() - 1) == '5');
    }

    // Sums up the digits of a number.
    private static double sum(String num) {
        double sum = 0;
        for (int i = 0; i < num.length(); i++) {
            sum += num.charAt(i) - '0';
        }
        return sum;
    }

    // Returns the number of each digit in its corresponding index in an array of length 10.
    public static int[] findDigits (String number) {
        int[] digits = new int[10];
        for(int i = 0; i < number.length(); i++) {
            digits[number.charAt(i) - '0']++;
        }
        return digits;
    }
}
