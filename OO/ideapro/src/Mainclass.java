import java.util.Scanner;

public class Mainclass {

    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        double b = scanner.nextDouble();
        String c = scanner.next();
        System.out.println("Hello world!");
        System.out.println(c + b + a);
    }
}
