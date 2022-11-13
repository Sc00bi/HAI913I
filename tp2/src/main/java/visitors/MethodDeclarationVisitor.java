package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methodDeclarationList;

    public MethodDeclarationVisitor() {
        this.methodDeclarationList = new ArrayList<>();
    }

    public boolean visit(MethodDeclaration node) {
        methodDeclarationList.add(node);
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethodDeclarationList() {
        return methodDeclarationList;
    }
}
