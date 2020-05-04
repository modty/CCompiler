package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class InitializerExecutor extends BaseExecutor {

	@Override
	public Object Execute(ICodeNode root) {
		//第0个孩子节点是Expr节点
		executeChild(root, 0);
		copyChild(root, root.getChildren().get(0));
		
		return root;
	}

}
