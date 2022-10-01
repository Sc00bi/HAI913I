package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class LinesVisitor extends ASTVisitor {
	private int linesCounter = 0;
	
	/* linesCounter Getter */
	public int getLinesCounter() {
		return linesCounter;
	}

	/* Implementations of visit and endvisit for every kind of node we could find in a CompilationUnit */
	/* SimpleName */
	/*public boolean visit(SimpleName node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean endvisite(SimpleName node) {
		linesCounter++;
		return super.visit(node);
	}*/

	/* FieldDeclaration */
	public boolean visit(FieldDeclaration node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean endvisite(FieldDeclaration node) {
		linesCounter++;
		return super.visit(node);
	}

	/* MethodDeclaration */
	public boolean visit(MethodDeclaration node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean endvisite(MethodDeclaration node) {
		linesCounter++;
		return super.visit(node);
	}

	/* MethodInvocation */
	public boolean visit(MethodInvocation node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean endvisite(MethodInvocation node) {
		linesCounter++;
		return super.visit(node);
	}

	/* TypeDeclaration */
	public boolean visit(TypeDeclaration node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean endvisite(TypeDeclaration node) {
		linesCounter++;
		return super.visit(node);
	}

	/* VariableDeclarationFragment */
	public boolean visit(VariableDeclarationFragment node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean endvisite(VariableDeclarationFragment node) {
		linesCounter++;
		return super.visit(node);
	}
	
	//public boolean visit(Package)

}
