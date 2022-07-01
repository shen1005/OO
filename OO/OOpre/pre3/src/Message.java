import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Message {
    private String information;
    private String text;

    public String getInformation() {
        return this.information;
    }

    public void setInformation(String information) {
        Matcher maText = this.reText.matcher(information);
        if (maText.find()) {
            this.text = maText.group();
            this.text = this.text.substring(1, this.text.length() - 1);
        }

        this.information = information;
    }

    private Pattern reText = Pattern.compile("\".*\"");

    public String getText() {
        return text;
    }

    public boolean isMatch(String type) {
        Pattern reA1 = Pattern.compile("^a{2,3}?b{2,4}?c{2,4}?");
        Pattern reA2 = Pattern.compile("a{2,3}?b{2,4}?c{2,4}?$");
        Pattern reA3 = Pattern.compile("a{2,3}?b{2,4}?c{2,4}?");
        Pattern reA4 = Pattern.compile("a.*?a.*?b.*?b.*?c.*?c");
        Pattern reB1 = Pattern.compile("^a{2,3}?b{2,1000000}?c{2,4}?");
        Pattern reB2 = Pattern.compile("a{2,3}?b{2,1000000}?c{2,4}?$");
        Pattern reB3 = Pattern.compile("a{2,3}?b{2,1000000}?c{2,4}?");
        Pattern reB4 = Pattern.compile("a.*?a.*?b.*?b.*?c.*?c");

        Matcher maA1 = reA1.matcher(this.text);
        Matcher maA2 = reA2.matcher(this.text);
        Matcher maA3 = reA3.matcher(this.text);
        Matcher maA4 = reA4.matcher(this.text);
        Matcher maB1 = reB1.matcher(this.text);
        Matcher maB2 = reB2.matcher(this.text);
        Matcher maB3 = reB3.matcher(this.text);
        Matcher maB4 = reB4.matcher(this.text);

        switch (type) {
            case "A1":
                return maA1.find();
            case "A2":
                return maA2.find();
            case "A3":
                return maA3.find();
            case "A4":
                return maA4.find();
            case "B1":
                return maB1.find();
            case "B2":
                return maB2.find();
            case "B3":
                return maB3.find();
            case "B4":
                return maB4.find();
            default:
                break;
        }
        return false;
    }
}
