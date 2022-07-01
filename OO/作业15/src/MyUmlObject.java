import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;

import java.util.HashMap;
import java.util.Set;

public interface MyUmlObject {

    public String getId();

    public void addAttribute(MyUmlAttribute myUmlAttribute);

    public void addOperation(MyUmlOperation myUmlOperation);

    public void addSon(MyUmlObject myUmlObject);

    public HashMap<String, MyUmlObject> getSon();

    public HashMap<String, MyUmlOperation> getOperation();

    public void addAssociation(MyUmlObject myUmlObject);

    public void addAssociationEnd(UmlAssociationEnd umlAssociationEnd);

    public Set<UmlClassOrInterface> checkR003();
}
