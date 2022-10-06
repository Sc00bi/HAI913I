package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class LinesVisitor extends ASTVisitor {
	private int linesCounter = 0;

	/* linesCounter Getter */
	public int getLinesCounter() {
		return linesCounter;
	}

	/*
	 * Implementations of visit and endvisit for every kind of statement node we
	 * could find in a CompilationUnit
	 */
	public boolean visit(BreakStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(AssertStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(DoStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(ContinueStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(EmptyStatement node) {
		// linesCounter++;
		return super.visit(node);
	}

	public boolean visit(ExpressionStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(EnhancedForStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(IfStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(WhileStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(ForStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(TryStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(ThrowStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(LabeledStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(SwitchStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(SynchronizedStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(ReturnStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(TypeDeclarationStatement node) {
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(VariableDeclarationStatement node) {
		linesCounter++;
		return super.visit(node);
	}
	
	public boolean visit(ImportDeclaration node)
	{
		linesCounter++;
		return super.visit(node);
	}

	public boolean visit(PackageDeclaration node)
	{
		linesCounter++;
		return super.visit(node);
	}
	
	public boolean visit(ConstructorInvocation node) {
		linesCounter++;
		return super.visit(node);
	}
	
	public boolean visit(FieldDeclaration node)
	{
		linesCounter++;
		return super.visit(node);
	}
	
	public boolean visit(MethodDeclaration node)
	{
		linesCounter++;
		return super.visit(node);
	}
	
	public boolean visit(SwitchCase node)
	{
		linesCounter++;
		return super.visit(node);
	}	

}
