import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private final int id;

    public MyMessageIdNotFoundException(int groupId) {
        count++;
        id = groupId;
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
        String temp = "minf-" + count + ", " + id + "-" + map.get(id);
        System.out.println(temp);
    }
}
