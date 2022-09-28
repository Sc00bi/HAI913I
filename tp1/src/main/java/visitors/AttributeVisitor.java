package visitors;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class AttributeVisitor extends ASTVisitor {
    List<FieldDeclaration> fields = new ArrayList<>();

    public boolean visit(FieldDeclaration node) {
        fields.add(node);
        return super.visit(node);
    }

    public List<FieldDeclaration> getFields() {
        return fields;
    }
}
