package top.modty.ccompiler.semantic.executor;


import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class StatementListExecutor extends BaseExecutor {
	 @Override 
	 public Object Execute(ICodeNode root) {
	    	executeChildren(root);
	    	Object child = root.getChildren().get(0);
	        copyChild(root, root.getChildren().get(0));
	    	return root;
	    }
}
