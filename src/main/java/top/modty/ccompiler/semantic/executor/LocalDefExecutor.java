package top.modty.ccompiler.semantic.executor;


import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class LocalDefExecutor extends BaseExecutor {

	@Override
	public Object Execute(ICodeNode root) {
		executeChild(root, 0);
		
		return root;
	}

}
