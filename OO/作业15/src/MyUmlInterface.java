import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class MyUmlInterface implements MyUmlObject {
    private final UmlInterface umlInterface;
    private String name;
    private final HashMap<String, MyUmlObject> son;
    private final HashMap<String, MyUmlOperation> operation;
    private final HashMap<String, MyUmlAttribute> attribute;
    private final HashMap<String, MyUmlInterface> interfaceFather;
    private final HashMap<String, MyUmlObject> associationHashMap;
    private final HashMap<String, UmlAssociationEnd> associationEndHashMap;
    private boolean r004;
    private boolean r005;

    public MyUmlInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
        son = new HashMap<>();
        operation = new HashMap<>();
        attribute = new HashMap<>();
        interfaceFather = new HashMap<>();
        name = umlInterface.getName();
        associationHashMap = new HashMap<>();
        associationEndHashMap = new HashMap<>();
        r004 = false;
        r005 = false;
    }

    public void setFather(MyUmlInterface myUmlInterface) {
        if (interfaceFather.containsKey(myUmlInterface.getId())) {
            r004 = true;
        }
        interfaceFather.put(myUmlInterface.getId(), myUmlInterface);
    }

    public int get() {
        return umlInterface.hashCode();
    }

    public HashMap<String, MyUmlInterface> getInterfaceFather() {
        return interfaceFather;
    }

    public String getName() {
        return name;
    }

    public UmlInterface getUmlInterface() {
        return umlInterface;
    }

    @Override
    public String getId() {
        return umlInterface.getId();
    }

    @Override
    public void addSon(MyUmlObject myUmlObject) {
        son.put(myUmlObject.getId(), myUmlObject);
    }

    @Override
    public HashMap<String, MyUmlObject> getSon() {
        return son;
    }

    @Override
    public void addOperation(MyUmlOperation myUmlOperation) {
        operation.put(myUmlOperation.getId(), myUmlOperation);
    }

    @Override
    public void addAttribute(MyUmlAttribute myUmlAttribute) {
        attribute.put(myUmlAttribute.getId(), myUmlAttribute);
        if (myUmlAttribute.getUmlAttribute().getVisibility() != Visibility.PUBLIC) {
            r005 = true;
        }
    }

    @Override
    public HashMap<String, MyUmlOperation> getOperation() {
        return operation;
    }

    @Override
    public void addAssociation(MyUmlObject myUmlObject) {
        associationHashMap.put(myUmlObject.getId(), myUmlObject);
    }

    @Override
    public void addAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        associationEndHashMap.put(umlAssociationEnd.getId(), umlAssociationEnd);
    }

    @Override
    public Set<UmlClassOrInterface> checkR003() {
        Set<UmlClassOrInterface> set = new HashSet<>();
        HashSet<UmlInterface> done = new HashSet<>();
        done.add(this.umlInterface);
        if (this.subCheckR003(done, this)) {
            set.addAll(done);
        }
        return set;
    }

    public boolean subCheckR003(HashSet<UmlInterface> done, MyUmlInterface origin) {
        for (MyUmlInterface temp : interfaceFather.values()) {
            if (temp != origin) {
                if (done.contains(temp.umlInterface)) {
                    continue;
                }
                done.add(temp.umlInterface);
                if (temp.subCheckR003(done, origin)) {
                    return true;
                }
                done.remove(temp.umlInterface);
            }
            else {
                return true;
            }
        }
        return false;
    }

    public boolean checkR004() {
        if (r004) {
            return true;
        }
        MyUmlInterface tempClass = this;
        HashMap<String, MyUmlInterface> temp2 = new HashMap<>();
        temp2.put(tempClass.getId(), tempClass);
        Queue<MyUmlInterface> queue = new LinkedList<>();
        queue.add(tempClass);
        while (!queue.isEmpty()) {
            MyUmlInterface myUmlInterface = queue.poll();
            for (MyUmlInterface myUmlInterface1 : myUmlInterface.getInterfaceFather().values()) {
                if (!temp2.containsValue(myUmlInterface1)) {
                    temp2.put(myUmlInterface1.getId(), myUmlInterface1);
                    queue.add(myUmlInterface1);
                    if (myUmlInterface1.r004) {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkR005() {
        return r005;
    }
}
