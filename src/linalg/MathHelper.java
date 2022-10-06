package linalg;

/**
 * Collection of mathematical calculations
 */
public class MathHelper {

    /**
     * Function to calculate the greatest common divisor
     * @param a First integer to calculate from
     * @param b Second integer to calculate from
     * @return Greatest common divisor
     */
    public static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

}
