import java.util.*;

// Product class
abstract class Product {
    private String name;
    private double price;
    private String category;

    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public abstract void displayDetails();
}

// Electronics class
class Electronics extends Product {
    public Electronics(String name, double price) {
        super(name, price, "Electronics");
    }

    @Override
    public void displayDetails() {
        System.out.println("Electronics: " + getName() + ", Price: $" + getPrice());
    }
}

// Clothing class
class Clothing extends Product {
    public Clothing(String name, double price) {
        super(name, price, "Clothing");
    }

    @Override
    public void displayDetails() {
        System.out.println("Clothing: " + getName() + ", Price: $" + getPrice());
    }
}

// ShoppingCart class
class ShoppingCart {
    private List<Product> items = new ArrayList<>();

    public void addProduct(Product product) {
        items.add(product);
    }

    public void removeProduct(Product product) {
        items.remove(product);
    }

    public double calculateTotal() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();
        }
        return total * 1.10; // Adding 10% tax
    }

    public List<Product> getItems() {
        return items;
    }

    public void displayCart() {
        for (Product item : items) {
            item.displayDetails();
        }
    }
}

// Customer class
class Customer {
    private String name;
    private String email;
    private ShoppingCart cart = new ShoppingCart();

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addToCart(Product product) {
        cart.addProduct(product);
    }

    public void removeFromCart(Product product) {
        cart.removeProduct(product);
    }

    public void checkout() {
        System.out.println("Total amount: $" + cart.calculateTotal());
        // Here you would add logic to place the order and generate an invoice
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void displayCart() {
        cart.displayCart();
    }
}

// Order class
class Order {
    private Customer customer;
    private List<Product> products;
    private String status;

    public Order(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
        this.status = "Pending";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayOrder() {
        System.out.println("Order for: " + customer.getName());
        for (Product product : products) {
            product.displayDetails();
        }
        System.out.println("Status: " + status);
    }
}

// Main class to run the program
public class OnlineShoppingPlatform {
    private static List<Customer> customers = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Register Customer");
            System.out.println("2. Add Product");
            System.out.println("3. View Products");
            System.out.println("4. Add Product to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Checkout");
            System.out.println("7. View Orders");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    viewProducts();
                    break;
                case 4:
                    addProductToCart();
                    break;
                case 5:
                    viewCart();
                    break;
                case 6:
                    checkout();
                    break;
                case 7:
                    viewOrders();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        customers.add(new Customer(name, email));
        System.out.println("Customer registered successfully.");
    }

    private static void addProduct() {
        System.out.print("Enter product type (1 for Electronics, 2 for Clothing): ");
        int type = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Product product;
        if (type == 1) {
            product = new Electronics(name, price);
        } else if (type == 2) {
            product = new Clothing(name, price);
        } else {
            System.out.println("Invalid product type.");
            return;
        }

        products.add(product);
        System.out.println("Product added successfully.");
    }

    private static void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        for (Product product : products) {
            product.displayDetails();
        }
    }

    private static void addProductToCart() {
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        Customer customer = findCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        viewProducts();
        System.out.print("Enter product name to add to cart: ");
        String name = scanner.nextLine();
        Product product = findProductByName(name);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        customer.addToCart(product);
        System.out.println("Product added to cart.");
    }

    private static void viewCart() {
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        Customer customer = findCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        customer.displayCart();
    }

    private static void checkout() {
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        Customer customer = findCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        customer.checkout();
        orders.add(new Order(customer, new ArrayList<>(customer.getCart().getItems())));
        customer.getCart().getItems().clear();
        System.out.println("Order placed successfully.");
    }

    private static void viewOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }
        for (Order order : orders) {
            order.displayOrder();
        }
    }

    private static Customer findCustomerByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    private static Product findProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}
