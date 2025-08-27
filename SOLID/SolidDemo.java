public class SolidDemo {

    // ===== S — Single Responsibility =====
    static class Invoice {
        private final int id;
        private final double amount;

        Invoice(int id, double amount) {
            this.id = id;
            this.amount = amount;
        }

        int getId() {
            return id;
        }

        double getAmount() {
            return amount;
        }
    }

    static class InvoicePrinter {
        void print(Invoice inv) {
            System.out.println("Invoice #" + inv.getId() + " amount=" + inv.getAmount());
        }
    }

    static class InvoiceSaver {
        void save(Invoice inv) {
            System.out.println("Saved invoice " + inv.getId());
        }
    }

    // ===== O — Open/Closed =====
    interface Shape {
        double area();
    }

    static class Circle implements Shape {
        private final double r;

        Circle(double r) {
            this.r = r;
        }

        public double area() {
            return Math.PI * r * r;
        }
    }

    static class Rectangle implements Shape {
        private final double w, h;

        Rectangle(double w, double h) {
            this.w = w;
            this.h = h;
        }

        public double area() {
            return w * h;
        }
    }

    static class AreaCalculator {
        double totalArea(java.util.List<Shape> shapes) {
            double sum = 0;
            for (Shape s : shapes) sum += s.area();
            return sum;
        }
    }

    // ===== L — Liskov Substitution =====
    interface Bird {
        void eat();
    }

    interface Flyable {
        void fly();
    }

    static class Sparrow implements Bird, Flyable {
        public void eat() {
            System.out.println("Sparrow pecks.");
        }

        public void fly() {
            System.out.println("Sparrow flies.");
        }
    }

    static class Penguin implements Bird {
        public void eat() {
            System.out.println("Penguin eats fish.");
        }
    }

    static void letItFly(Flyable f) {
        f.fly();
    } // only things that can actually fly

    // ===== I — Interface Segregation =====
    interface Printer {
        void print(String doc);
    }

    interface Scanner {
        void scan();
    }

    static class SimplePrinter implements Printer {
        public void print(String doc) {
            System.out.println("Printing: " + doc);
        }
    }

    static class MultiFunctionPrinter implements Printer, Scanner {
        public void print(String doc) {
            System.out.println("MFP print: " + doc);
        }

        public void scan() {
            System.out.println("MFP scanning");
        }
    }

    // ===== D — Dependency Inversion =====
    interface MessageSender {
        void send(String to, String msg);
    }

    static class EmailSender implements MessageSender {
        public void send(String to, String msg) {
            System.out.println("Email to " + to + ": " + msg);
        }
    }

    static class SmsSender implements MessageSender {
        public void send(String to, String msg) {
            System.out.println("SMS to " + to + ": " + msg);
        }
    }

    static class NotificationService {
        private final MessageSender sender;

        NotificationService(MessageSender sender) {
            this.sender = sender;
        } // depend on abstraction

        void notify(String user, String message) {
            sender.send(user, message);
        }
    }

    public static void main(String[] args) {
        // SRP
        Invoice inv = new Invoice(101, 999.0);
        new InvoicePrinter().print(inv);
        new InvoiceSaver().save(inv);

        // OCP
        java.util.List<Shape> shapes =
                java.util.Arrays.asList(new Circle(1), new Rectangle(2, 3));
        System.out.println("Total area = " + new AreaCalculator().totalArea(shapes));

        // LSP
        letItFly(new Sparrow()); // OK
        new Penguin().eat();     // We never try to fly a penguin

        // ISP
        Printer p = new SimplePrinter();
        p.print("Report");
        MultiFunctionPrinter mfp = new MultiFunctionPrinter();
        mfp.scan();

        // DIP
        NotificationService n1 = new NotificationService(new EmailSender());
        n1.notify("alice@example.com", "Welcome!");
        NotificationService n2 = new NotificationService(new SmsSender());
        n2.notify("+911234567890", "OTP: 123456");
    }
}
