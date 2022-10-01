package visitors;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageVisitor extends ASTVisitor{
	private HashSet<String> packageDeclarations = new HashSet<>();
	
	public boolean visit(PackageDeclaration node)
	{
		for (String s : node.getName().getFullyQualifiedName().split("\\."))
		{
			packageDeclarations.add(s);
		}
		return super.visit(node);
		
	}
	
	public int getSize()
	{
		return packageDeclarations.size();
	}
	
	/* Unused in this project */
	public HashSet<String> getPackageDeclarations()
	{
		return packageDeclarations;
	}
}
