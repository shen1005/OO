import com.oocourse.spec2.exceptions.RelationNotFoundException;
import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int count = 0;
    private static HashMap<Integer, Integer> map = new HashMap<>();
    private int id1;
    private int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        count++;
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        if (!map.containsKey(this.id1)) {
            map.put(this.id1, 1);
        }
        else {
            map.put(this.id1, map.get(this.id1) + 1);
        }
        if (!map.containsKey(this.id2)) {
            map.put(this.id2, 1);
        }
        else {
            map.put(this.id2, map.get(this.id2) + 1);
        }
    }

    public void print() {
        String temp = "rnf-" + count + ", " +
                id1 + "-" + map.get(id1) + ", " + id2 + "-" + map.get(id2);
        System.out.println(temp);
    }
}
