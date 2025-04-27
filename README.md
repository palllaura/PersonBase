# PersonBase
PersonBase is a simple web application for managing a list of people with their personal information such as name, birthdate, address, phone number, and home internet speed.

## Features

* View a table of all people
* Add a new person
* Edit an existing person
* Delete a person
* Data validation for fields (e.g., internet speed must be valid)
* Load initial people data from a .json file
* Uses an H2 in-memory database
* Frontend built with React + TailwindCSS
* Backend built with Java Spring Boot

## Prerequisites
* Java 17+
* Node.js + npm
* A package manager like Gradle
* A code editor like IntelliJ IDEA


## Installation
### Backend (Spring Boot)
1. Clone the repository:
   ```bash
   git clone https://github.com/palllaura/PersonBase.git
   cd personbase

2. Open the project in your IDE (e.g., IntelliJ).

3. Build and run the backend:
   ```bash
   ./gradlew bootRun
4. The backend server will start at:
   http://localhost:8080

### Frontend (React)
1. Navigate to the frontend folder:
   ```bash
   cd frontend
2. Install dependencies:
   ```bash
   npm install
3. Start the development server:
   ```bash
   npm run dev
4. The frontend will be available at:
   http://localhost:5173

<img width="1052" alt="Screenshot 2025-04-27 at 18 14 06" src="https://github.com/user-attachments/assets/e9f7cb2b-f74f-483f-bbd2-c12943ef9af5" />


