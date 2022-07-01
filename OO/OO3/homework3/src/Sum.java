import java.math.BigInteger;

public class Sum {
    private String func;
    private BigInteger down;
    private BigInteger up;

    public void setInformation(String originSum) {
        String temp = originSum.replace(" ", "");
        temp = temp.replace("\t", "");
        String[] array = temp.substring(4, temp.length() - 1).split(",");
        this.down = new BigInteger(array[1]);
        this.up = new BigInteger(array[2]);
        this.func = array[3].replace("sin", "son");
    }

    public Expr getExpr() {
        BigInteger i = new BigInteger(down + "");
        String temp = "";
        if (up.compareTo(down) < 0) {
            temp = "0";
        }
        else {
            while (!i.equals(this.up)) {
                temp += this.func.replace("i", "(" + i + ")") + "+";
                i = i.add(new BigInteger("1"));
            }
            temp += this.func.replace("i", "(" + up + ")");
        }
        temp = temp.replace("son", "sin");
        Sentence sentence = new Sentence();
        sentence.setInformation(temp);
        return sentence.getSenExpr();
    }
}
