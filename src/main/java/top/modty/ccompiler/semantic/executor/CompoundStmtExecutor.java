package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class CompoundStmtExecutor extends BaseExecutor {

	@Override
	public Object Execute(ICodeNode root) {
		
		return executeChild(root, 0);
	}

}
