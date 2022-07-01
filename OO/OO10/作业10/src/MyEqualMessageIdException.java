import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int count = 0;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private int id;

    public MyEqualMessageIdException(int messageId) {
        count++;
        id = messageId;
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
        String temp = "emi-" + count + ", " + id + "-" + map.get(id);
        System.out.println(temp);
    }
}
