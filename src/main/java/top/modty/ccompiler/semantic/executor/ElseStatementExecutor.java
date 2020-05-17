package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.semantic.code.ProgramGenerator;
import top.modty.ccompiler.semantic.inter.ICodeNode;
/**
 * @author 点木
 * @date 2020-05-05 16:05
 * @mes 包含 ELSE的执行器
 */
public class ElseStatementExecutor extends BaseExecutor {
    private ProgramGenerator generator = ProgramGenerator.getInstance();
	@Override
	public Object Execute(ICodeNode root) {
		 BaseExecutor.inIfElseStatement = true;
		 
		//先执行if 部分
		 String branch = generator.getCurrentBranch();
    	 ICodeNode res = executeChild(root, 0);
    	 
    	 BaseExecutor.inIfElseStatement = false;
    	 
		 branch = "\n" + branch + ":\n";
		 generator.emitString(branch);
		 
		/*
		 if (generator.getIfElseEmbedCount() == 0) {
			 generator.increaseBranch();
		 }*/
		 generator.increaseBranch();
		 
    	 Object obj = res.getAttribute(ICodeKey.VALUE);
    	 if ((Integer)obj == 0 || BaseExecutor.isCompileMode) {
    		 generator.incraseIfElseEmbed();
    		 //if 部分没有执行，所以执行else部分
    		 res = executeChild(root, 1); 
    		 generator.decraseIfElseEmbed();
    	 }
    	 
    	 copyChild(root, res);
    	 
    	 generator.emitBranchOut();
		 
    	 return root;
	}

}
