import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    public static String dealString(String expr) {
        String tempExpr = expr.replace(" ", "");
        tempExpr = tempExpr.replace("\t", "");
        int i;
        String temp;

        Pattern p = Pattern.compile("[+-]{2}");
        Matcher m = p.matcher(tempExpr);
        while (m.find()) {
            tempExpr = tempExpr.replace("+-", "-");
            tempExpr = tempExpr.replace("-+", "-");
            tempExpr = tempExpr.replace("++", "+");
            tempExpr = tempExpr.replace("--", "+");
            m = p.matcher(tempExpr);
        }

        p = Pattern.compile("(\\([^)]+\\))\\*\\*(\\+)?([0-9]+)");
        m = p.matcher(tempExpr);
        while (m.find()) {
            i = Integer.parseInt(m.group(4));
            temp = m.group(1);
            while (i > 1) {
                temp = String.format("%s*%s", temp, m.group(1));
                i--;
            }
            if (i == 0) {
                temp = "1";
            }
            tempExpr = tempExpr.replace(m.group(0), temp);
            m = p.matcher(tempExpr);
        }

        p = Pattern.compile("\\*\\*(\\+)?([0-9]+)");
        m = p.matcher(tempExpr);
        while (m.find()) {
            temp = "**" + m.group(2);
            tempExpr = tempExpr.replace(m.group(0), temp);
        }

        if (tempExpr.startsWith("+") || tempExpr.startsWith("-")) {
            return "0" + tempExpr;
        }
        return  tempExpr;
    }

    public static ExprNode setExprNode(String tempExpr) {
        ExprNode j = new ExprNode();
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(tempExpr);
        Expr e = new Expr();
        Factor factor = new Factor();
        if (m.matches()) {
            factor.setIndex(new BigInteger("0"));
            factor.setRatio(new BigInteger(tempExpr));
            e.addFactor(factor);
            j.setExpr(e);
            return j;
        }
        else if (m.find()) {
            factor.setIndex(new BigInteger(m.group(0)));
            factor.setRatio(new BigInteger("1"));
            e.addFactor(factor);
            j.setExpr(e);
            return j;
        }
        else {
            factor.setRatio(new BigInteger("1"));
            factor.setIndex(new BigInteger("1"));
            e.addFactor(factor);
            j.setExpr(e);
            return j;
        }

    }

    public static String dealSub(String expr) {
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
                pos = tempExpr.indexOf(')', pos);
            }
        }
        return tempExpr;
    }

    public static Node setTree(String expr) {
        int pos;
        String tempExpr = expr.trim();
        int num = 0;
        for (pos = 0; pos < tempExpr.length(); pos++) {
            if (tempExpr.charAt(pos) == '(') {
                num++;
            }
        }

        if (tempExpr.startsWith("(") && tempExpr.endsWith(")") && num == 1) {
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
                pos = tempExpr.indexOf(')', pos);
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
                pos = tempExpr.indexOf(')', pos);
            }
        }
        return setExprNode(tempExpr);

    }

    public static void main(String[] args) {
        ExprInput sc = new ExprInput(ExprInputMode.NormalMode);
        String expr = sc.readLine();
        expr = dealString(expr);
        Node head = setTree(expr);
        Expr ans = head.getValue();
        ans.simplify();
        System.out.println(ans.des());
    }
}
