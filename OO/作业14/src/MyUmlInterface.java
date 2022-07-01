import com.oocourse.uml2.models.elements.UmlInterface;

import java.util.HashMap;

public class MyUmlInterface implements MyUmlObject {
    private final UmlInterface umlInterface;
    private String name;
    private final HashMap<String, MyUmlObject> son;
    private final HashMap<String, MyUmlOperation> operation;
    private final HashMap<String, MyUmlAttribute> attribute;
    private final HashMap<String, MyUmlInterface> interfaceFather;
    private final HashMap<String, MyUmlObject> associationHashMap;

    public MyUmlInterface(UmlInterface umlInterface) {
        this.umlInterface = umlInterface;
        son = new HashMap<>();
        operation = new HashMap<>();
        attribute = new HashMap<>();
        interfaceFather = new HashMap<>();
        name = umlInterface.getName();
        associationHashMap = new HashMap<>();
    }

    public void setFather(MyUmlInterface myUmlInterface) {
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
    }

    @Override
    public HashMap<String, MyUmlOperation> getOperation() {
        return operation;
    }

    @Override
    public void addAssociation(MyUmlObject myUmlObject) {
        associationHashMap.put(myUmlObject.getId(), myUmlObject);
    }
}

