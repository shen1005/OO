import com.oocourse.spec3.main.Person;

import java.util.HashSet;

public class Graph {
    private Person father;
    private HashSet<Side> sides;
    private HashSet<Integer> children;

    public Graph(Person father) {
        this.father = father;
        this.sides = new HashSet<>();
        this.children = new HashSet<>();
        this.children.add(father.getId());
    }

    public void addChild(int child) {
        children.add(child);
    }

    public HashSet<Integer> getChildren() {
        return children;
    }

    public void addSide(Side side) {
        this.sides.add(side);
        this.children.add(side.getPerson1());
        this.children.add(side.getPerson2());
    }

    public HashSet<Side> getSides() {
        return this.sides;
    }
}
