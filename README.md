# Noisy multi-qubit circuits — simulation code

Exact density-matrix simulation of small (2–8 qubit) circuits under
depolarizing, phase-flip and amplitude-damping noise.

Single-author hobby project. Written in Java, deterministic
(no Monte-Carlo sampling, no random seeds).

## Repository layout

```
src/main/java/org/example/
├── ExperimentFidelitySigmoid.java   # RQ1 — fidelity vs noise rate
├── ExperimentCircuitDepth.java      # RQ2 — threshold vs depth
├── ExperimentTopology.java          # RQ3 — linear / star / all-to-all
├── ExperimentEntropySpike.java      # RQ4 — entropy vs fidelity
├── Factory.java                     # ground state factory
├── Component/DensityMatrix.java
├── Interface/QuantumState.java      # gates, channels, observables
├── Gates/                           # H, CNOT, Pauli X/Y/Z
├── Noise/                           # Kraus operators per channel
└── Math/                            # Complex, MathUtils (fidelity, entropy)
```

Dependencies: [ojAlgo](https://www.ojalgo.org/) for complex
eigendecomposition (`C128` backend).

## Build and run

```bash
# build (replace with your actual build command, e.g. `mvn package`)
<your build command here>

# run any experiment — each one writes its own CSVs to the working dir
java -cp <classpath> org.example.ExperimentFidelitySigmoid
java -cp <classpath> org.example.ExperimentCircuitDepth
java -cp <classpath> org.example.ExperimentTopology
java -cp <classpath> org.example.ExperimentEntropySpike
```

Each driver loops over a hard-coded list of qubit counts and
conditions at the top of `main(...)`. Edit those arrays to change the
sweep.

## Output

Every run writes one CSV per `(n, condition)` with columns:

| column            | meaning                          |
|-------------------|----------------------------------|
| `noise_rate`      | $p \in \{0.00, 0.01, \dots, 1.00\}$ |
| `fidelity`        | Uhlmann fidelity vs noiseless target |
| `entropy`         | full-state von Neumann entropy (bits) |
| `purity`          | $\mathrm{Tr}(\rho^2)$            |
| `entropy_derivative` | finite-difference $dS/dp$ (RQ4 only) |


