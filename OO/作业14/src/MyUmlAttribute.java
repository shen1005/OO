import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.elements.UmlAttribute;

public class MyUmlAttribute {
    private final UmlAttribute umlAttribute;

    public MyUmlAttribute(UmlAttribute umlAttribute) {
        this.umlAttribute = umlAttribute;
    }

    public UmlAttribute getUmlAttribute() {
        return umlAttribute;
    }

    public String getId() {
        return umlAttribute.getId();
    }

    public String getParentId() {
        return umlAttribute.getParentId();
    }

    public NameableType getType() {
        return umlAttribute.getType();
    }
}
