package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class TestExecutor extends BaseExecutor {

	@Override
	public Object Execute(ICodeNode root) {
		executeChildren(root);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
