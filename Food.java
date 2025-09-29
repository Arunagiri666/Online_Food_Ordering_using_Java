import java.util.*;
import java.util.stream.Collectors;

enum OrderStatus { PLACED, PREPARING, DELIVERED, CANCELLED }

class User {
    int userId;
    String name;
    String email;
    String phoneNumber;
    String address;
    List<Order> orderHistory = new ArrayList<>();

    public User(int userId, String name, String email, String phoneNumber, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    public void displayUser() {
        System.out.println("\n=== User Profile ===");
        System.out.println("ID: " + userId + " | Name: " + name);
        System.out.println("Email: " + email + " | Phone: " + phoneNumber);
        System.out.println("Address: " + address);
    }

    public void displayOrders() {
        System.out.println("\n--- Order History for " + name + " ---");
        if (orderHistory.isEmpty()) {
            System.out.println("No orders yet.");
        } else {
            for (Order o : orderHistory) {
                o.displayBrief();
            }
        }
    }
}

class Admin {
    int adminId;
    String username;
    String password;
    List<String> permissions;

    public Admin(int adminId, String username, String password, List<String> permissions) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public boolean authenticate(Scanner sc) {
        System.out.print("Enter Admin Username: ");
        String uname = Food.sc.nextLine().trim();
        while (uname.isEmpty()) uname = Food.sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String pw = Food.sc.nextLine().trim();
        while (pw.isEmpty()) pw = Food.sc.nextLine().trim();

        return uname.equals(username) && pw.equals(password);
    }


    public void displayAdmin() {
        System.out.println("\n=== Admin Info ===");
        System.out.println("Admin ID: " + adminId + " | Username: " + username);
        System.out.println("Permissions: " + permissions);
    }
}

class FoodItem {
    int foodId;
    String name;
    String description;
    double price;
    String category;
    double discount;

    public FoodItem(int foodId, String name, String description, double price, String category) {
        this(foodId, name, description, price, category, 0);
    }

    public FoodItem(int foodId, String name, String description, double price, String category, double discount) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.discount = discount;
    }

    public double getDiscountedPrice() {
        return price - (price * discount / 100);
    }

    @Override
    public String toString() {
        if (discount > 0) {
            return String.format("%d. %s (%s) - ₹%.2f (Original: ₹%.2f, %.0f%% OFF) — %s",
                    foodId, name, category, getDiscountedPrice(), price, discount, description);
        } else {
            return String.format("%d. %s (%s) - ₹%.2f — %s",
                    foodId, name, category, price, description);
        }
    }
}

class Hotel {
    int hotelId;
    String name;
    String location;
    List<FoodItem> menu = new ArrayList<>();
    double rating;

    public Hotel(int hotelId, String name, String location, double rating) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    public void addFoodItem(FoodItem fi) {
        menu.add(fi);
    }

    public boolean removeFoodById(int foodId) {
        return menu.removeIf(f -> f.foodId == foodId);
    }

    public FoodItem findFoodById(int foodId) {
        for (FoodItem f : menu) {
            if (f.foodId == foodId) return f;
        }
        return null;
    }

    public void displayHotel() {
        System.out.println("\n--- Hotel: " + name + " (ID: " + hotelId + ") ---");
        System.out.println("Location: " + location + " | Rating: " + rating);
        System.out.println("Menu:");
        if (menu.isEmpty()) {
            System.out.println("  (No items currently)");
        } else {
            for (FoodItem f : menu) {
                System.out.println("  " + f);
            }
        }
    }

    public void displayMenuShort() {
        for (FoodItem f : menu) {
            System.out.println("  " + f);
        }
    }
}

class Order {
    int orderId;
    User user;
    Hotel hotel;
    List<FoodItem> items;
    double totalAmount;
    OrderStatus status;
    Date timestamp;

    public Order(int orderId, User user, Hotel hotel, List<FoodItem> items) {
        this.orderId = orderId;
        this.user = user;
        this.hotel = hotel;
        this.items = new ArrayList<>(items);
        this.totalAmount = calculateTotal();
        this.status = OrderStatus.PLACED;
        this.timestamp = new Date();
    }

    private double calculateTotal() {
        return items.stream().mapToDouble(FoodItem::getDiscountedPrice).sum();
    }

    public void displayBrief() {
        System.out.printf("Order #%d at %s — ₹%.2f — %s — %s\n",
                orderId, hotel.name, totalAmount, status, timestamp);
    }

    public void displayFull() {
        System.out.println("\n=== Order Receipt ===");
        System.out.println("Order ID: #" + orderId + " | Status: " + status);
        System.out.println("Hotel: " + hotel.name);
        System.out.println("Items Ordered:");
        for (FoodItem f : items) {
            System.out.printf("  - %s (₹%.2f)\n", f.name, f.getDiscountedPrice());
        }
        System.out.printf("Total Amount: ₹%.2f\n", totalAmount);
        System.out.println("Ordered on: " + timestamp);
    }
}

class Cart {
    User user;
    List<FoodItem> items = new ArrayList<>();

    public Cart(User user) {
        this.user = user;
    }

    public void addItem(FoodItem f) {
        items.add(f);
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(FoodItem::getDiscountedPrice).sum();
    }

    public void displayCart() {
        System.out.println("\n--- Cart for " + user.name + " ---");
        if (items.isEmpty()) {
            System.out.println("(Cart is empty)");
        } else {
            for (FoodItem f : items) {
                System.out.printf("  - %s (₹%.2f)\n", f.name, f.getDiscountedPrice());
            }
            System.out.printf("Total: ₹%.2f\n", getTotalAmount());
        }
    }
}

public class Food {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Setup sample data
        Admin admin = new Admin(1, "SuperAdmin", "admin123",
                Arrays.asList("ADD_MENU", "REMOVE_MENU", "EDIT_MENU", "VIEW_ORDERS"));
        List<Hotel> hotels = new ArrayList<>();
        initSampleHotels(hotels);

        User sampleUser = new User(101, "Shakthi", "shakthi@gmail.com", "9876543210", "Thirumoolar Street");

        System.out.println("Welcome to the Food Ordering App!");
        while (true) {
            System.out.println("\nLogin as (type):");
            System.out.println("1. user");
            System.out.println("2. admin");
            System.out.println("3. exit");
            System.out.print("Your choice: ");
            String role = sc.nextLine().trim().toLowerCase();
            if (role.equals("user") || role.equals("1")) {
                userMenu(sampleUser, hotels);
            } else if (role.equals("admin") || role.equals("2")) {
                if (admin.authenticate(sc)) {
                    adminMenu(admin, hotels);
                }
            } else if (role.equals("exit") || role.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void initSampleHotels(List<Hotel> hotels) {
        Hotel h1 = new Hotel(1, "Arafa Restaurant", "Vadugapalayam", 4.5);
        h1.addFoodItem(new FoodItem(1, "Chicken Biryani", "Spicy Hyderabadi biryani", 180, "Non-Veg", 10));
        h1.addFoodItem(new FoodItem(2, "Veg Fried Rice", "Chinese style rice", 120, "Veg"));
        h1.addFoodItem(new FoodItem(3, "Paneer Butter Masala", "Creamy gravy with paneer", 150, "Veg", 20));

        Hotel h2 = new Hotel(2, "David Hotel", "Coimbatore", 4.2);
        h2.addFoodItem(new FoodItem(1, "Mutton Biryani", "Rich Mughlai biryani", 220, "Non-Veg", 15));
        h2.addFoodItem(new FoodItem(2, "Veg Biryani", "Aromatic basmati with veggies", 160, "Veg", 5));
        h2.addFoodItem(new FoodItem(3, "Butter Naan", "Soft Indian bread", 40, "Veg"));

        Hotel h3 = new Hotel(3, "Dindigul Thalappakatti", "Erode", 4.7);
        h3.addFoodItem(new FoodItem(1, "Dindigul Biryani", "Authentic seeraga samba biryani", 200, "Non-Veg", 10));
        h3.addFoodItem(new FoodItem(2, "Chicken 65", "Crispy fried chicken", 150, "Non-Veg"));
        h3.addFoodItem(new FoodItem(3, "Parotta", "Soft layered bread", 25, "Veg"));

        Hotel h4 = new Hotel(4, "SS Hyderabad Biryani", "Salem", 4.3);
        h4.addFoodItem(new FoodItem(1, "Hyderabadi Biryani", "Authentic Hyderabad flavor", 210, "Non-Veg", 12));
        h4.addFoodItem(new FoodItem(2, "Egg Biryani", "Biryani with boiled eggs", 150, "Non-Veg", 5));
        h4.addFoodItem(new FoodItem(3, "Paneer Tikka", "Grilled paneer cubes", 180, "Veg"));

        hotels.add(h1);
        hotels.add(h2);
        hotels.add(h3);
        hotels.add(h4);
    }

    private static void userMenu(User user, List<Hotel> hotels) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. View Profile");
            System.out.println("2. Search hotels / food");
            System.out.println("3. Browse all hotels");
            System.out.println("4. View order history");
            System.out.println("5. Logout to main");
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1":
                    user.displayUser();
                    break;
                case "2":
                    userSearchAndOrder(user, hotels);
                    break;
                case "3":
                    browseAndOrder(user, hotels);
                    break;
                case "4":
                    user.displayOrders();
                    break;
                case "5":
                    System.out.println("Logging out user...");
                    return;
                default:
                    System.out.println("Invalid. Try again.");
            }
        }
    }

    private static void adminMenu(Admin admin, List<Hotel> hotels) {
        System.out.println("Logged in as Admin: " + admin.username);
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View Admin Info");
            System.out.println("2. Search hotels / food");
            System.out.println("3. View all hotels & menus");
            System.out.println("4. Add hotel");
            System.out.println("5. Edit hotel");
            System.out.println("6. Remove hotel");
            System.out.println("7. Add menu item to a hotel");
            System.out.println("8. Remove menu item from a hotel");
            System.out.println("9. Edit menu item in a hotel");
            System.out.println("10. Logout to main");
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": admin.displayAdmin(); break;
                case "2": adminSearch(hotels); break;
                case "3": for (Hotel h : hotels) h.displayHotel(); break;
                case "4": adminAddHotel(hotels); break;
                case "5": adminEditHotel(hotels); break;
                case "6": adminRemoveHotel(hotels); break;
                case "7": adminAddMenu(hotels); break;
                case "8": adminRemoveMenu(hotels); break;
                case "9": adminEditMenu(hotels); break;
                case "10": System.out.println("Logging out admin..."); return;
                default: System.out.println("Invalid. Try again.");
            }
        }
    }

    private static void adminAddHotel(List<Hotel> hotels) {
        System.out.print("Enter new Hotel ID: ");
        int hid = safeIntInput();
        System.out.print("Hotel Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Location: ");
        String loc = sc.nextLine().trim();
        System.out.print("Rating: ");
        double rating = safeDoubleInput();
        hotels.add(new Hotel(hid, name, loc, rating));
        System.out.println("Hotel added successfully!");
    }

    private static void adminEditHotel(List<Hotel> hotels) {
        System.out.println("Hotels available:");
        for (Hotel h : hotels) {
            System.out.println(h.hotelId + ". " + h.name);
        }
        System.out.print("Enter hotel ID to edit (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel h = findHotelById(hotels, hid);
        if (h == null) {
            System.out.println("Hotel not found.");
            return;
        }
        System.out.print("New name (blank to keep): ");
        String nm = sc.nextLine().trim();
        if (!nm.isEmpty()) h.name = nm;
        System.out.print("New location (blank to keep): ");
        String loc = sc.nextLine().trim();
        if (!loc.isEmpty()) h.location = loc;
        System.out.print("New rating (-1 to keep): ");
        double r = safeDoubleInput();
        if (r >= 0) h.rating = r;
        System.out.println("Hotel updated!");
    }

    private static void adminRemoveHotel(List<Hotel> hotels) {
        System.out.println("Hotels available:");
        for (Hotel h : hotels) {
            System.out.println(h.hotelId + ". " + h.name);
        }
        System.out.print("Enter hotel ID to remove (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        boolean ok = hotels.removeIf(h -> h.hotelId == hid);
        if (ok) System.out.println("Hotel removed!");
        else System.out.println("Hotel not found.");
    }

    private static void adminAddMenu(List<Hotel> hotels) {
        System.out.println("Hotels available:");
        for (Hotel h : hotels) {
            System.out.println(h.hotelId + ". " + h.name);
        }
        System.out.print("Enter hotel ID to add item to (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel h = findHotelById(hotels, hid);
        if (h == null) {
            System.out.println("Hotel not found.");
            return;
        }
        System.out.print("Enter new Food ID: ");
        int fid = safeIntInput();
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Description: ");
        String desc = sc.nextLine().trim();
        System.out.print("Price: ");
        double price = safeDoubleInput();
        System.out.print("Category: ");
        String cat = sc.nextLine().trim();
        System.out.print("Discount %: ");
        double disc = safeDoubleInput();
        h.addFoodItem(new FoodItem(fid, name, desc, price, cat, disc));
        System.out.println("Item added to " + h.name);
    }

    private static void adminRemoveMenu(List<Hotel> hotels) {
        System.out.println("Hotels available:");
        for (Hotel h : hotels) {
            System.out.println(h.hotelId + ". " + h.name);
        }
        System.out.print("Enter hotel ID to remove item from (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel h = findHotelById(hotels, hid);
        if (h == null) {
            System.out.println("Hotel not found.");
            return;
        }
        h.displayHotel();
        System.out.print("Enter Food ID to remove: ");
        int fid = safeIntInput();
        boolean ok = h.removeFoodById(fid);
        if (ok) System.out.println("Removed item.");
        else System.out.println("No such item.");
    }

    private static void adminEditMenu(List<Hotel> hotels) {
        System.out.println("Hotels available:");
        for (Hotel h : hotels) {
            System.out.println(h.hotelId + ". " + h.name);
        }
        System.out.print("Enter hotel ID to edit (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel h = findHotelById(hotels, hid);
        if (h == null) {
            System.out.println("Hotel not found.");
            return;
        }
        h.displayHotel();
        System.out.print("Enter Food ID to edit (0 to cancel): ");
        int fid = safeIntInput();
        if (fid == 0) return;
        FoodItem f = h.findFoodById(fid);
        if (f == null) {
            System.out.println("Item not found.");
            return;
        }
        System.out.print("New name (blank to keep): ");
        String nm = sc.nextLine().trim();
        if (!nm.isEmpty()) f.name = nm;
        System.out.print("New description (blank to keep): ");
        String ds = sc.nextLine().trim();
        if (!ds.isEmpty()) f.description = ds;
        System.out.print("New price (-1 to keep): ");
        double pr = safeDoubleInput();
        if (pr >= 0) f.price = pr;
        System.out.print("New category (blank to keep): ");
        String ct = sc.nextLine().trim();
        if (!ct.isEmpty()) f.category = ct;
        System.out.print("New discount % (-1 to keep): ");
        double dc = safeDoubleInput();
        if (dc >= 0) f.discount = dc;
        System.out.println("Updated item!");
    }

    private static void userSearchAndOrder(User user, List<Hotel> hotels) {
        System.out.print("Enter hotel name or food name to search: ");
        String query = sc.nextLine().trim().toLowerCase();
        List<Hotel> matchingHotels = hotels.stream()
                .filter(h -> h.name.toLowerCase().contains(query))
                .collect(Collectors.toList());
        List<Hotel> hotelsWithFood = new ArrayList<>();
        for (Hotel h : hotels) {
            for (FoodItem f : h.menu) {
                if (f.name.toLowerCase().contains(query)) {
                    hotelsWithFood.add(h);
                    break;
                }
            }
        }
        Set<Hotel> allMatches = new LinkedHashSet<>();
        allMatches.addAll(matchingHotels);
        allMatches.addAll(hotelsWithFood);
        if (allMatches.isEmpty()) {
            System.out.println("No matching hotels or food found.");
            return;
        }
        for (Hotel h : allMatches) {
            System.out.println(h.hotelId + ". " + h.name + " (" + h.location + ")");
        }
        System.out.print("Enter hotel ID to view menu (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel sel = findHotelById(hotels, hid);
        if (sel == null || !allMatches.contains(sel)) {
            System.out.println("Invalid hotel.");
            return;
        }
        sel.displayHotel();
        takeOrderFromHotel(user, sel);
    }

    private static void browseAndOrder(User user, List<Hotel> hotels) {
        for (Hotel h : hotels) {
            System.out.println(h.hotelId + ". " + h.name + " (" + h.location + ")");
        }
        System.out.print("Enter hotel ID (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel sel = findHotelById(hotels, hid);
        if (sel == null) {
            System.out.println("Invalid hotel.");
            return;
        }
        sel.displayHotel();
        takeOrderFromHotel(user, sel);
    }

    private static void takeOrderFromHotel(User user, Hotel hotel) {
        Cart cart = new Cart(user);
        while (true) {
            System.out.print("Enter Food ID to add (0 to checkout, -1 to cancel): ");
            int fid = safeIntInput();
            if (fid == 0) break;
            if (fid == -1) return;
            FoodItem f = hotel.findFoodById(fid);
            if (f == null) System.out.println("Invalid food ID.");
            else {
                cart.addItem(f);
                System.out.println("Added: " + f.name);
            }
        }
        cart.displayCart();
        if (cart.items.isEmpty()) return;
        System.out.print("Place order? (yes/no): ");
        String yn = sc.nextLine().trim().toLowerCase();
        if (yn.equals("yes")) {
            int orderId = new Random().nextInt(9000) + 1000;
            Order order = new Order(orderId, user, hotel, cart.items);
            user.addOrder(order);
            System.out.println("Order placed!");
            order.displayFull();
        }
    }

    private static void adminSearch(List<Hotel> hotels) {
        System.out.print("Enter hotel/food name: ");
        String q = sc.nextLine().trim().toLowerCase();
        List<Hotel> matchingHotels = hotels.stream()
                .filter(h -> h.name.toLowerCase().contains(q))
                .collect(Collectors.toList());
        Set<Hotel> foodMatches = new LinkedHashSet<>();
        for (Hotel h : hotels) {
            for (FoodItem f : h.menu) {
                if (f.name.toLowerCase().contains(q)) {
                    foodMatches.add(h);
                    break;
                }
            }
        }
        Set<Hotel> allMatches = new LinkedHashSet<>();
        allMatches.addAll(matchingHotels);
        allMatches.addAll(foodMatches);
        if (allMatches.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }
        for (Hotel h : allMatches) {
            System.out.println(h.hotelId + ". " + h.name);
        }
        System.out.print("Enter hotel ID (0 to cancel): ");
        int hid = safeIntInput();
        if (hid == 0) return;
        Hotel sel = findHotelById(hotels, hid);
        if (sel == null || !allMatches.contains(sel)) {
            System.out.println("Invalid.");
            return;
        }
        sel.displayHotel();
    }

    private static Hotel findHotelById(List<Hotel> hotels, int hid) {
        for (Hotel h : hotels) if (h.hotelId == hid) return h;
        return null;
    }

    private static int safeIntInput() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Invalid integer. Try again: ");
            }
        }
    }

    private static double safeDoubleInput() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}