// File: PolynomialSolver.java

import com.google.gson.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class PolynomialSolver {

    public static void main(String[] args) throws Exception {
        // Step 1: Read and parse JSON file
        JsonObject json = JsonParser.parseReader(new FileReader("input.json")).getAsJsonObject();
        JsonObject keys = json.getAsJsonObject("keys");
        int n = keys.get("n").getAsInt();
        int k = keys.get("k").getAsInt();

        List<BigInteger> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (!entry.getKey().equals("keys")) {
                int x = Integer.parseInt(entry.getKey());
                JsonObject pair = entry.getValue().getAsJsonObject();
                int base = pair.get("base").getAsInt();
                String value = pair.get("value").getAsString();
                BigInteger y = new BigInteger(value, base);

                xList.add(BigInteger.valueOf(x));
                yList.add(y);
                if (xList.size() == k) break; // Use only k points
            }
        }

        // Step 2: Build matrix A and vector Y
        BigInteger[][] A = new BigInteger[k][k];
        BigInteger[] Y = new BigInteger[k];

        for (int i = 0; i < k; i++) {
            BigInteger xi = xList.get(i);
            BigInteger power = BigInteger.ONE;
            for (int j = 0; j < k; j++) {
                A[i][j] = power;
                power = power.multiply(xi);
            }
            Y[i] = yList.get(i);
        }

        // Step 3: Solve A * coeff = Y
        BigInteger[] coeffs = solveGaussian(A, Y);

        // ✅ Print only the constant term (a0 = c)
        System.out.println("c = " + coeffs[0]);
    }

    // Gaussian Elimination with BigInteger
    static BigInteger[] solveGaussian(BigInteger[][] A, BigInteger[] Y) {
        int n = Y.length;

        for (int i = 0; i < n; i++) {
            // Partial pivot
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j][i].abs().compareTo(A[maxRow][i].abs()) > 0)
                    maxRow = j;
            }

            // Swap rows
            BigInteger[] temp = A[i];
            A[i] = A[maxRow];
            A[maxRow] = temp;

            BigInteger tmpY = Y[i];
            Y[i] = Y[maxRow];
            Y[maxRow] = tmpY;

            // Eliminate
            for (int j = i + 1; j < n; j++) {
                if (A[i][i].equals(BigInteger.ZERO)) continue;
                BigInteger factor = A[j][i].divide(A[i][i]);

                for (int k = i; k < n; k++) {
                    A[j][k] = A[j][k].subtract(factor.multiply(A[i][k]));
                }

                Y[j] = Y[j].subtract(factor.multiply(Y[i]));
            }
        }

        // Back substitution
        BigInteger[] x = new BigInteger[n];
        for (int i = n - 1; i >= 0; i--) {
            BigInteger sum = Y[i];
            for (int j = i + 1; j < n; j++) {
                sum = sum.subtract(A[i][j].multiply(x[j]));
            }

            if (A[i][i].equals(BigInteger.ZERO)) {
                x[i] = BigInteger.ZERO;
            } else {
                x[i] = sum.divide(A[i][i]);
            }
        }

        return x;
    }
}
