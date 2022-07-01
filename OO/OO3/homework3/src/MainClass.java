import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        ExprInput sc = new ExprInput(ExprInputMode.NormalMode);
        int cnt = sc.getCount();
        HashMap<String, Func> hashMap = new HashMap<>();
        for (int i = 0; i < cnt; i++) {
            String func = sc.readLine().trim();
            Func newFunc = new Func();
            newFunc.setInformation(func);
            hashMap.put(func.charAt(0) + "", newFunc);
        }
        String expr = sc.readLine();
        Sentence sentence = new Sentence();
        sentence.setHashMap(hashMap);
        sentence.setInformation(expr);
        System.out.println(sentence.getSenExpr().des());
    }
}
