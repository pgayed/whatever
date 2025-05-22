# Cool Clojure Code Example

This project demonstrates a simple use of Clojure's `core.async` library for concurrent programming.

## Functionality

The main program (`cool-clojure-code.core/-main`) launches two asynchronous "computations" that simulate long-running tasks. It then waits for both computations to complete and prints their results.

This showcases:
- Basic Clojure project setup (`project.clj`).
- Namespace definition and requiring libraries.
- `go` blocks for asynchronous operations.
- Channels (`chan`, `<!`, `>!`) for communication (though this example primarily uses `<!` for receiving results from go blocks that act like channels).
- Simulating delays with `async/timeout`.

## Prerequisites

- Java Development Kit (JDK) version 8 or later.
- Leiningen (Clojure project management tool). You can find installation instructions at [https://leiningen.org/](https://leiningen.org/).

## How to Run

1.  **Clone the repository (if applicable).**
2.  **Navigate to the project directory.**
3.  **Run the code:**
    ```bash
    lein run
    ```

    You should see output indicating the start and completion of two computations, followed by their results. For example:

    ```
    Launched computations, waiting for results...
    Starting computation A...
    Starting computation B...
    Computation A done! Result: <some-random-number>
    Final result from A: Computation A done! Result: <some-random-number>
    Computation B done! Result: <another-random-number>
    Final result from B: Computation B done! Result: <another-random-number>
    All computations finished.
    ```
