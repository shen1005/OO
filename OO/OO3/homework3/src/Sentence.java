import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sentence {
    private Node head;
    private Expr senExpr;
    private HashMap<String, Func> hashMap = new HashMap<>();

    public String dealString(String expr) {
        String tempExpr = expr.replace(" ", "").replace("\t", "");
        Pattern p = Pattern.compile("[+-]{2}");
        Matcher m = p.matcher(tempExpr);
        while (m.find()) {
            tempExpr = tempExpr.replace("+-", "-");
            tempExpr = tempExpr.replace("-+", "-");
            tempExpr = tempExpr.replace("++", "+");
            tempExpr = tempExpr.replace("--", "+");
            m = p.matcher(tempExpr);
        }
        return  tempExpr;
    }

    public ExprNode createSinNode(String expr) {
        Factor factor = new Factor();
        SinCos sinCos = new SinCos();
        sinCos.setAll(expr, this.hashMap);
        if (Pattern.compile("^cos\\(0\\)").matcher(sinCos.getDes()).find()) {
            factor.setRatio(new BigInteger("1"));
            factor.setIndex(new BigInteger("0"));
        }
        else if (Pattern.compile("^sin\\(0\\)").matcher(sinCos.getDes()).find() &&
                !sinCos.getOutIndex().equals(new BigInteger("0"))) {
            factor.setRatio(new BigInteger("0"));
            factor.setIndex(new BigInteger("0"));
        }
        else {
            if (!sinCos.getOutIndex().equals(new BigInteger("0"))) {
                factor.addSinCos(sinCos);
            }
            factor.setRatio(new BigInteger("1"));
            factor.setIndex(new BigInteger("0"));
        }
        Expr e = new Expr();
        ExprNode j = new ExprNode();
        e.addFactor(factor);
        j.setExpr(e);
        return j;
    }

    public ExprNode setExprNode(String tempExpr) {
        ExprNode j = new ExprNode();
        Matcher m = Pattern.compile("^(\\(.+\\))\\*\\*\\+?([0-9]+)$").matcher(tempExpr);
        Expr e = new Expr();
        Factor factor = new Factor();
        if (m.find()) {
            String temp = m.group(1);
            for (int i = Integer.parseInt(m.group(2)); i > 1; i--) {
                temp += "*" + m.group(1);
            }
            if (new BigInteger(m.group(2)).equals(new BigInteger("0"))) {
                temp = "1";
            }
            Sentence sentence = new Sentence();
            sentence.hashMap = this.hashMap;
            e.addFactors(sentence.setInformation(temp).getFactors());
            j.setExpr(e);
            return j;
        }
        m = Pattern.compile("^[sc][io][ns]").matcher(tempExpr);
        if (m.find()) {
            return createSinNode(tempExpr);
        }
        m = Pattern.compile("^[fgh]").matcher(tempExpr);
        if (m.find()) {
            e.addFactors(hashMap.get(tempExpr.charAt(0) + "").
                    getExpr(tempExpr, hashMap).getFactors());
            j.setExpr(e);
            return j;
        }

        m = Pattern.compile("^sum").matcher(tempExpr);
        if (m.find()) {
            Sum sum = new Sum();
            sum.setInformation(tempExpr);
            e.addFactors(sum.getExpr().getFactors());
            j.setExpr(e);
            return j;
        }
        m = Pattern.compile("[0-9]+").matcher(tempExpr);
        if (m.matches()) {
            factor.setIndex(new BigInteger("0"));
            factor.setRatio(new BigInteger(tempExpr));
            e.addFactor(factor);
            j.setExpr(e);
        }
        else if (m.find()) {
            factor.setIndex(new BigInteger(m.group(0)));
            factor.setRatio(new BigInteger("1"));
            e.addFactor(factor);
            j.setExpr(e);
        }
        else {
            factor.setRatio(new BigInteger("1"));
            factor.setIndex(new BigInteger("1"));
            e.addFactor(factor);
            j.setExpr(e);
        }
        return j;
    }

    public String dealSub(String expr) {
        int pos;
        String tempExpr;
        tempExpr = expr;
        for (pos = 0; pos < tempExpr.length(); pos++) {
            if (tempExpr.charAt(pos) == '+' && tempExpr.charAt(pos - 1) != '*') {
                tempExpr = tempExpr.substring(0, pos) + "-" + tempExpr.substring(pos + 1);
            }
            else if (tempExpr.charAt(pos) == '-' && tempExpr.charAt(pos - 1) != '*') {
                tempExpr = tempExpr.substring(0, pos) + "+" + tempExpr.substring(pos + 1);
            }
            else if (tempExpr.charAt(pos) == '(') {
                pos = getPos(tempExpr, pos);
            }
        }
        return tempExpr;
    }

    public Node setTree(String expr) {

        String tempExpr = expr.trim();
        int pos;

        while (tempExpr.startsWith("(") && getPos(tempExpr, 0) == tempExpr.length() - 1) {
            tempExpr = tempExpr.substring(1, tempExpr.length() - 1);
        }
        if (tempExpr.startsWith("+") || tempExpr.startsWith("-")) {
            tempExpr = "0" + tempExpr;
        }
        OpNode i = new OpNode();
        for (pos = 0; pos < tempExpr.length(); pos++) {
            if ((tempExpr.charAt(pos) == '+' || tempExpr.charAt(pos) == '-') &&
                    tempExpr.charAt(pos - 1) != '*') {
                i.setLeft(setTree(tempExpr.substring(0, pos)));
                if (tempExpr.charAt(pos) == '-') {
                    i.setRight(setTree(dealSub(tempExpr.substring(pos + 1))));
                }
                else {
                    i.setRight(setTree(tempExpr.substring(pos + 1)));
                }
                i.setOp(tempExpr.charAt(pos));
                return i;
            }
            else if (tempExpr.charAt(pos) == '(') {
                pos = getPos(tempExpr, pos);
            }
        }
        for (pos = 0; pos < tempExpr.length(); pos++) {
            if (tempExpr.charAt(pos) == '*' && tempExpr.charAt(pos + 1) != '*' &&
                    tempExpr.charAt(pos - 1) != '*') {
                i.setLeft(setTree(tempExpr.substring(0, pos)));
                i.setRight(setTree(tempExpr.substring(pos + 1)));
                i.setOp('*');
                return i;
            }
            else if (tempExpr.charAt(pos) == '(') {
                pos = getPos(tempExpr, pos);
            }
        }
        return setExprNode(tempExpr);

    }

    public int getPos(String expr, int i) {
        int num = 1;
        int pos = i;
        while (num > 0) {
            pos++;
            if (expr.charAt(pos) == '(') {
                num++;
            }
            else if (expr.charAt(pos) == ')') {
                num--;
            }
        }
        return pos;
    }

    public Expr setInformation(String expr) {
        String tempExpr = dealString(expr);
        this.head = setTree(tempExpr);
        this.senExpr = this.head.getValue();
        this.senExpr.simplify();
        return senExpr;
    }

    public Expr getSenExpr() {
        return senExpr;
    }

    public void setHashMap(HashMap<String, Func> hashMap) {
        this.hashMap = hashMap;
    }
}
