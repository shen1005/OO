import com.oocourse.spec1.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private final int id;

    public MyGroupIdNotFoundException(int groupId) {
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
        String temp = "ginf-" + count + ", " + id + "-" + map.get(id);
        System.out.println(temp);
    }
}
