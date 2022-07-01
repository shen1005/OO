import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private int id;

    public MyEmojiIdNotFoundException(int id) {
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
        String temp = "einf-" + count + ", " + id + "-" + map.get(id);
        System.out.println(temp);
    }

}
