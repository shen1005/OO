import java.util.HashMap;

public interface MyUmlObject {

    public String getId();

    public void addAttribute(MyUmlAttribute myUmlAttribute);

    public void addOperation(MyUmlOperation myUmlOperation);

    public void addSon(MyUmlObject myUmlObject);

    public HashMap<String, MyUmlObject> getSon();

    public HashMap<String, MyUmlOperation> getOperation();

    public void addAssociation(MyUmlObject myUmlObject);
}
