import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int count;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private int id;

    public MyEqualPersonIdException(int id) {
        count++;
        this.id = id;
        if (map.containsKey(id)) {
            int num = map.get(id);
            map.put(id, num + 1);
        }
        else {
            map.put(id, 1);
        }
    }

    @Override
    public void print() {
        String temp = "epi-" + count + ", " + id + "-" + map.get(id);
        System.out.println(temp);
    }
}
