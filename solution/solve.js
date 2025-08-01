const fs = require("fs");

function decodeY(value, base) {
  return BigInt(parseInt(value, base));
}

function lagrangeInterpolation(x, y, atX = 0n) {
  let result = 0n;
  const k = x.length;

  for (let i = 0; i < k; i++) {
    let term = y[i];
    for (let j = 0; j < k; j++) {
      if (i !== j) {
        const num = BigInt(atX) - x[j];
        const den = x[i] - x[j];
        term = (term * num) / den;
      }
    }
    result += term;
  }

  return result;
}

function computeSecret(filePath) {
  const raw = fs.readFileSync(filePath);
  const data = JSON.parse(raw);

  const k = data["keys"]["k"];
  let x = [];
  let y = [];

  for (let key in data) {
    if (key === "keys") continue;
    if (x.length === k) break;

    const xi = BigInt(parseInt(key));
    const base = parseInt(data[key]["base"]);
    const yEncoded = data[key]["value"];

    const yi = decodeY(yEncoded, base);
    x.push(xi);
    y.push(yi);
  }

  const secret = lagrangeInterpolation(x, y, 0n);
  console.log(`Secret (c): ${secret}`);
}

// Run both testcases
console.log("Testcase 1:");
computeSecret("testcase1.json");

console.log("\nTestcase 2:");
computeSecret("testcase2.json");
