import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line;
        String[] sentences;
        ArrayList<Message> messages = new ArrayList<>();
        while (!(line = sc.nextLine()).equals("END_OF_MESSAGE")) {
            sentences = line.trim().split(";");
            for (String i: sentences) {
                Message message = new Message();
                message.setInformation((i + ";").trim());
                messages.add(message);
            }
        }
        while (sc.hasNext()) {
            sentences = sc.nextLine().split(" ");
            for (Message i: messages) {
                if (i.isMatch(sentences[1] + sentences[2])) {
                    System.out.println(i.getInformation());
                }
            }
        }
    }
}

