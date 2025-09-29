# 🍴 Console-Based Online Food Ordering System (Java)

A **console-driven Online Food Ordering System** developed in **Java** that allows users to browse hotels, view menus, place and track orders, and manage profiles. It also includes an **admin module** for managing hotels, food items, and menu updates.  

---

## ✨ Features  

### 👤 User Module
- Register/Login (predefined sample user available)  
- View and update user profile  
- Search hotels or food items by name  
- Browse hotels and their menus  
- Add food items to cart and place orders  
- View order history with receipts  

### 🛠️ Admin Module
- Admin authentication (username & password)  
- View admin details and permissions  
- Add, edit, or remove hotels  
- Manage hotel menus (add/edit/remove food items)  
- Search hotels/menus and view details  

### 📌 System Features
- Menu-driven console interface  
- Order status tracking (`PLACED`, `PREPARING`, `DELIVERED`, `CANCELLED`)  
- Discount and price calculation  
- Uses **OOP concepts** (Classes, Objects, Inheritance, Enums, Collections, Streams)  
- Input validation for safe data entry  

---

## ⚙️ Technologies Used
- **Java (Core Java, Collections, Streams, Enums, OOP)**  
- **IntelliJ IDEA / Eclipse** (recommended IDE)  
- Runs fully on **console (CLI)** — no external libraries needed  

---

## ▶️ How to Run
1. Clone the repository:  
   ```bash
   git clone https://github.com/Arunagiri666/OnlineFoodOrderingSystem.git

After cloning, open the project in your preferred IDE. You can use IntelliJ IDEA, Eclipse, or any other IDE that supports Java development.

Make sure you have the Java Development Kit (JDK) installed on your system. This project requires JDK 8 or above. Also ensure that the JDK is properly configured in your IDE settings.

Inside the project folder, navigate to the src/ directory. This is where all the Java source code files are stored.

In the src/ folder, locate the file named Food.java. This file contains the main() method and serves as the entry point of the application.

To run the program, you have multiple options:

If you are using IntelliJ IDEA, right-click on the Food.java file and select Run 'Food.main()'.

If you are using Eclipse, right-click on the Food.java file and select Run As → Java Application.

If you prefer running from the command line, open a terminal inside the src/ folder and execute:

bash
Copy code
javac Food.java
java Food
Once the program starts running, the console menu will be displayed. You will be asked to choose whether you want to log in as a User or as an Admin.

If you choose the User option, you will be able to:

Browse the list of hotels

View menus for each hotel

Add food items to your cart

Place orders

View your order history with receipts

If you choose the Admin option, you will be able to:

Log in with an admin username and password

View admin details and permissions

Add, edit, or remove hotels

Manage hotel menus by adding, editing, or removing food items

Search hotels or menus and view their details
