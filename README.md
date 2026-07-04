# Welcome to the Library Management System

Welcome! This project is a complete digital assistant for running a small library. It keeps track of books, manages library members, and handles the borrowing and returning process so you don't have to do it on paper!

#  What Can It Do?
Think of this system as the digital brain of your library. With it, you can:
*   **Manage Books:** Add new books to your catalog, update their details, and keep track of how many copies you own versus how many are currently available on the shelf.
*   **Manage Members:** Register new people who want to borrow books and keep their contact information updated.
*   **Check Out & Return:** Easily lend a book to a member. The system will automatically reduce the available copies. When they bring it back, the system updates the inventory again!
*   **Track History:** Wondering who currently has a specific book, or what books a particular person is reading? The system tracks all of that history for you.

# How It Works (Behind the Scenes)
This system is built using modern, reliable technologies:
*   **Java & Spring Boot:** The powerful engine running the logic of the application.
*   **MySQL:** The secure database where all your books, members, and history are permanently stored.
*   **Swagger:** A beautiful, interactive web page that acts as the control panel for your system.

# How to Start the System
Starting your library system is incredibly simple. Just follow these steps:

1. **Start your Database:** Make sure your MySQL server is running on your computer.
2. **Configure the Password:** Open the `src/main/resources/application.properties` file and make sure the `spring.datasource.password` matches your MySQL root password.
3. **Run the Application:** 
   * If you are using a code editor (like VS Code or Eclipse), just click the "Run" button on the project.
   * Alternatively, open a terminal in this folder and run: `.\apache-maven-3.9.6\bin\mvn spring-boot:run` (or just `mvn spring-boot:run` if Maven is installed globally).
4. **Done!** The system will automatically connect to your database and create all the necessary tables for you.

# How to Use the System
You don't need any special software to use this system. Everything is controlled through a simple web interface!

Once the application is running, open your favorite web browser and go to:
** http://localhost:8080/swagger-ui.html **

This will open your **Interactive Control Panel**. Here is how to use it:
1. You will see sections for **Books**, **Members**, and **Borrowing**.
2. Click on any action you want to perform (for example, the green button to add a new book).
3. Click the **"Try it out"** button.
4. Fill in the details (like the book's title and author).
5. Click the big **Execute** button!

The system will instantly process your request and save the information directly into your database. It's that easy!
