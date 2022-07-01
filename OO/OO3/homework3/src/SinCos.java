import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinCos {
    private Expr expr = new Expr();
    private boolean isSin;
    private BigInteger outIndex = new BigInteger("0");
    private HashMap<String, Func> hashMap;

    public void setSin(boolean sin) {
        isSin = sin;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public boolean getIsSin() {
        return isSin;
    }

    public BigInteger getOutIndex() {
        return outIndex;
    }

    public void setOutIndex(BigInteger outIndex) {
        this.outIndex = outIndex;
    }

    public int forCom() {
        if (!outIndex.equals(new BigInteger("2"))) {
            return -1;
        }
        else if (isSin) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public boolean isSame(SinCos other) { //是否是可乘同类项
        return this.isSin == other.isSin && this.expr.equals(other.expr);
    }

    public void setAll(String expr,HashMap<String, Func> hashMap) {
        this.hashMap = hashMap;
        Pattern p = Pattern.compile("^sin\\((.+)\\)\\*?\\*?\\+?([0-9]+)?$");
        Matcher m = p.matcher(expr);
        if (m.matches()) {
            this.isSin = true;
            if (m.group(2) == null) {
                this.outIndex = new BigInteger("1");
            }
            else {
                this.outIndex = new BigInteger(m.group(2));
            }
            Sentence sentence = new Sentence();
            sentence.setHashMap(hashMap);
            this.expr.addFactors(sentence.setInformation(m.group(1)).getFactors());

        }

        p = Pattern.compile(
                "^cos\\((.+)\\)\\*?\\*?\\+?([0-9]+)?$");
        m = p.matcher(expr);
        if (m.matches()) {
            this.isSin = false;
            if (m.group(2) == null) {
                this.outIndex = new BigInteger("1");
            }
            else {
                this.outIndex = new BigInteger(m.group(2));
            }
            Sentence sentence = new Sentence();
            sentence.setHashMap(hashMap);
            this.expr.addFactors(sentence.setInformation(m.group(1)).getFactors());

        }
    }

    public void mul(SinCos sinCos) {
        this.outIndex = this.outIndex.add(sinCos.outIndex);
    }

    public boolean isEqual(SinCos other) { //是否是可加同类相
        return this.isSin == other.isSin && this.expr.equals(other.expr)
                && this.outIndex.equals(other.outIndex);
    }

    public String getDes() {
        String temp = "";
        String behind = ")";
        if (this.isSin) {
            temp = "sin(";
        } else {
            temp = "cos(";
        }
        if (!this.outIndex.equals(new BigInteger("1"))) {
            behind += "**" + this.outIndex;
        }
        if (expr.getFactors().size() == 0) {
            return temp + "0" + behind;
        }
        Factor i = this.expr.getFactors().get(0);
        if (this.expr.getFactors().size() == 1) {
            if ((i.getRatio().equals(new BigInteger("1")) ||
                    i.getRatio().equals(new BigInteger("0")))
                    && i.getSinCos().size() == 0) {
                return temp + this.expr.des() + behind;
            } else if (i.getIndex().equals(new BigInteger("0")) && i.getSinCos().size() == 0) {
                return temp + this.expr.des() + behind;
            }
            else if ((i.getRatio().equals(new BigInteger("1")) ||
                    i.getRatio().equals(new BigInteger("0")))
                    && i.getSinCos().size() == 1 && i.getIndex().equals(new BigInteger("0"))) {
                return temp + this.expr.des() + behind;
            }
        }
        return temp + "(" + this.expr.des() + ")" +  behind;
    }
}