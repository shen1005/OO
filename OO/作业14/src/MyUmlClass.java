import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlClass;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class MyUmlClass implements MyUmlObject {
    private final UmlClass umlClass;
    private final String name;
    private final HashMap<String, MyUmlObject> son;
    private final HashMap<String, MyUmlOperation> operation;
    private final HashMap<String, MyUmlAttribute> attribute;
    private MyUmlClass father;
    private int attributeDegree;
    private final HashSet<ReferenceType> referenceType;
    private final HashMap<String, MyUmlInterface> interfaceHashMap;
    private final HashMap<String, MyUmlObject> associationHashMap;

    public MyUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
        son = new HashMap<>();
        operation = new HashMap<>();
        attribute = new HashMap<>();
        father = null;
        name = umlClass.getName();
        attributeDegree = 0;
        referenceType = new HashSet<>();
        interfaceHashMap = new HashMap<>();
        associationHashMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int get() {
        return umlClass.hashCode();
    }

    public void setFather(MyUmlClass myUmlClass) {
        father = myUmlClass;
    }

    public Map<Visibility, Integer> getOperationVisibility(String methodName) {
        Map<Visibility, Integer> map = new HashMap<>();
        map.put(Visibility.PUBLIC, 0);
        map.put(Visibility.PROTECTED, 0);
        map.put(Visibility.PRIVATE, 0);
        map.put(Visibility.PACKAGE, 0);
        for (MyUmlOperation myUmlOperation : operation.values()) {
            if (myUmlOperation.getName().equals(methodName)) {
                Visibility visibility = myUmlOperation.getVisibility();
                map.put(visibility, map.get(visibility) + 1);
            }
        }
        return map;
    }

    public List<Integer> getOperationDegree(String methodName) throws
            MethodWrongTypeException, MethodDuplicatedException {
        HashMap<String, MyUmlOperation> temp = new HashMap<>();
        boolean flag = true;
        for (MyUmlOperation myUmlOperation : operation.values()) {
            if (myUmlOperation.getName().equals(methodName)) {
                temp.put(myUmlOperation.getId(), myUmlOperation);
                flag = flag && myUmlOperation.isRight();
            }
        }
        if (!flag) {
            throw new MethodWrongTypeException(this.name, methodName);
        }
        List<MyUmlOperation> temp2 = new ArrayList<>();
        for (MyUmlOperation myUmlOperation : temp.values()) {
            if (!temp2.contains(myUmlOperation)) {
                temp2.add(myUmlOperation);
            }
        }
        if (temp.size() != temp2.size()) {
            throw new MethodDuplicatedException(this.name, methodName);
        }
        List<Integer> list = new ArrayList<>();
        for (MyUmlOperation myUmlOperation : temp.values()) {
            list.add(myUmlOperation.getDegree());
        }
        return list;
    }

    @Override
    public String getId() {
        return umlClass.getId();
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
        if (myUmlAttribute.getType() instanceof ReferenceType) {
            ReferenceType type = (ReferenceType) myUmlAttribute.getType();
            referenceType.add(type);
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

    public int getAttributeDegree() {
        MyUmlClass temp = this;
        if (attributeDegree == 0) {
            while (temp.father != null) {
                referenceType.addAll(temp.father.referenceType);
                temp = temp.father;
            }
            for (ReferenceType type : referenceType) {
                if (!type.getReferenceId().equals(this.getId())) {
                    attributeDegree++;
                }
            }
        }
        return attributeDegree;
    }

    public void addInterface(MyUmlInterface myUmlInterface) {
        interfaceHashMap.put(myUmlInterface.getId(), myUmlInterface);
    }

    public HashMap<String, String> getAllInterface() {
        HashMap<String, MyUmlInterface> temp = new HashMap<>();
        MyUmlClass tempClass = this;
        while (tempClass != null) {
            temp.putAll(tempClass.interfaceHashMap);
            tempClass = tempClass.father;
        }
        HashMap<String, MyUmlInterface> temp2 = new HashMap<>(temp);
        Queue<MyUmlInterface> queue = new LinkedList<>(temp.values());
        while (!queue.isEmpty()) {
            MyUmlInterface myUmlInterface = queue.poll();
            for (MyUmlInterface myUmlInterface1 : myUmlInterface.getInterfaceFather().values()) {
                if (!temp2.containsValue(myUmlInterface1)) {
                    temp2.put(myUmlInterface1.getId(), myUmlInterface1);
                    queue.add(myUmlInterface1);
                }
            }
        }
        HashMap<String, String> ans = new HashMap<>();
        for (MyUmlInterface myUmlInterface : temp2.values()) {
            ans.put(myUmlInterface.getName(), myUmlInterface.getId());
        }
        return ans;
    }

    public int getDepth() {
        int depth = 0;
        MyUmlClass temp = father;
        while (temp != null) {
            depth++;
            temp = temp.father;
        }
        return depth;
    }
}
