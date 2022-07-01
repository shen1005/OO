import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int count = 0;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private int id1;
    private int id2;

    public MyEqualRelationException(int id1, int id2) {
        count++;
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        if (map.containsKey(id1)) {
            int num = map.get(id1);
            map.put(id1, num + 1);
        }
        else {
            map.put(id1, 1);
        }
        if (id1 != id2) {
            if (map.containsKey(id2)) {
                int num = map.get(id2);
                map.put(id2, num + 1);
            }
            else {
                map.put(id2, 1);
            }
        }
    }

    @Override
    public void print() {
        String temp = "er-" + count + ", " + id1 + "-" + map.get(id1) + ", " +
                id2 + "-" + map.get(id2);
        System.out.println(temp);
    }
}
