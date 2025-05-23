# Clojure Server-Side Counter App

This project is a simple web application built entirely in Clojure. It demonstrates a web page with a counter that is incremented on the server when a button is clicked. No client-side JavaScript is used for the counter logic.

## Features

-   **Server-Side Logic:** All counter operations and HTML rendering are handled by Clojure code on the server.
-   **Ring/Jetty:** Uses the Ring library for web request handling and Jetty as the underlying HTTP server.
-   **Rum:** Uses the Rum library for generating HTML via UI components. Page uses default browser styling.
-   **State Management:** The counter's state is managed on the server using a Clojure `atom`.

## Project Structure

-   `project.clj`: Defines the project, dependencies (Clojure, Ring, Jetty, Rum), and the main class.
-   `src/clojure_counter_app/core.clj`: Contains all the application logic:
    -   Namespace definition and required libraries (including Rum).
    -   A Rum component (`counter-page`) for defining the HTML structure. (Removed `page-styles`)
    -   An `atom` to store the counter value.
    -   A Ring `handler` function that:
        -   Checks for requests to `/increment` (via POST) to increase the counter.
        -   Renders the HTML page using `rum/render-html` and the defined Rum components.
    -   The `app` definition, wrapping the handler with necessary middleware.
    -   A `-main` function to start the Jetty server.

## Prerequisites

-   Java Development Kit (JDK) version 8 or later.
-   Leiningen (Clojure project management tool). You can find installation instructions at [https://leiningen.org/](https://leiningen.org/).

## How to Run

1.  **Clone the repository (if applicable).**
2.  **Navigate to the project directory.**
3.  **Run the application:**
    ```bash
    lein run
    ```
4.  **Open your web browser** and go to `http://localhost:8080` (or the port specified in the console output if `PORT` environment variable is set).

    You should see a page displaying "Clojure Counter App", the current count (initially 0), and a button. Clicking the button will send a request to the server, increment the counter, and reload the page with the updated count.
