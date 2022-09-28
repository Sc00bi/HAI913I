package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationVisitor extends ASTVisitor {
    List<TypeDeclaration> typeDeclarationList = new ArrayList<>();

    public boolean visit(TypeDeclaration node) {
        typeDeclarationList.add(node);
        return super.visit(node);
    }

    public List<TypeDeclaration> getTypeDeclarationList() {
        return typeDeclarationList;
    }
}
