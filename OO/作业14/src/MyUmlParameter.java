import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.NameableType;
import com.oocourse.uml2.models.elements.UmlParameter;

public class MyUmlParameter {
    private final UmlParameter umlParameter;
    private final NameableType type;

    public MyUmlParameter(UmlParameter umlParameter) {
        this.umlParameter = umlParameter;
        this.type = umlParameter.getType();
    }

    public String getId() {
        return umlParameter.getId();
    }

    public NameableType getType() {
        return type;
    }

    public Direction getDirection() {
        return umlParameter.getDirection();
    }

    public String getParentId() {
        return umlParameter.getParentId();
    }
}
