# 📐 Polynomial Solver using Matrix Method (Java)

This program reconstructs a polynomial of degree **k-1** from `k` given points using **matrix (Vandermonde) method** and prints the **constant term (c = a₀)**.  
It supports numbers in different bases and uses **BigInteger** for arbitrary large precision.

---

## 🧩 Features

- Accepts inputs in any base (binary, octal, decimal, hex, etc.)
- Solves the polynomial using **Gaussian Elimination**
- Works with large values using `BigInteger`
- Reads input from a JSON file using Google's **Gson** library
