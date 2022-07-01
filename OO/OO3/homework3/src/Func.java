import java.util.HashMap;

public class Func {
    private String function;
    private int numOfx;
    private String[] var = new String[3];

    public void setInformation(String originFunc) {
        String temp = originFunc.replace(" ", "");
        temp = temp.replace("\t", "");
        temp = temp.replace('x', 'u');
        temp = temp.replace('y', 'v');
        temp = temp.replace('z', 'w');
        this.numOfx = temp.split(",").length;
        int pos = temp.indexOf("=");
        this.function = temp.substring(pos + 1);
        String temp2 = temp.substring(2, pos - 1);
        this.var = temp2.split(",");
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

    public String change(String funcCall) {
        String temp = function;
        String[] m = new String[3];
        int i = 0;
        String tempFuncCall = funcCall.substring(2, funcCall.length() - 1);
        for (int pos = 0; pos < tempFuncCall.length(); pos++) {
            if (tempFuncCall.charAt(pos) == '(') {
                pos = getPos(tempFuncCall, pos);
            }
            else if (tempFuncCall.charAt(pos) == ',') {
                m[i++] = tempFuncCall.substring(0, pos);
                tempFuncCall = tempFuncCall.substring(pos + 1);
                pos = -1;
            }
        }
        m[i] = tempFuncCall;
        if (numOfx == 1) {
            if (m[0].length() == 1) {
                temp = temp.replace(var[0], m[0]);
            }
            else {
                temp = temp.replace(var[0], "(" + m[0] + ")");
            }
        }
        else if (numOfx == 2) {
            if (m[0].length() == 1) {
                temp = temp.replace(var[0], m[0]);
            }
            else {
                temp = temp.replace(var[0], "(" + m[0] + ")");
            }
            if (m[1].length() == 1) {
                temp = temp.replace(var[1], m[1]);
            }
            else {
                temp = temp.replace(var[1], "(" + m[1] + ")");
            }
        }
        else {
            if (m[0].length() == 1) {
                temp = temp.replace(var[0], m[0]);
            }
            else {
                temp = temp.replace(var[0], "(" + m[0] + ")");
            }
            if (m[1].length() == 1) {
                temp = temp.replace(var[1], m[1]);
            }
            else {
                temp = temp.replace(var[1], "(" + m[1] + ")");
            }
            if (m[2].length() == 1) {
                temp = temp.replace(var[2], m[2]);
            }
            else {
                temp = temp.replace(var[2], "(" + m[2] + ")");
            }
        }
        return temp;
    }

    public Expr getExpr(String funCall, HashMap<String, Func> hashMap) {
        String temp = this.change(funCall);
        Sentence sentence = new Sentence();
        sentence.setHashMap(hashMap);
        sentence.setInformation(temp);
        return sentence.getSenExpr();
    }
}
