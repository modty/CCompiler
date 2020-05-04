package top.modty.ccompiler.semantic.executor;


import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.commons.constants.Instruction;
import top.modty.ccompiler.semantic.ProgramGenerator;
import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class IfStatementExecutor extends BaseExecutor {

	 @Override 
	 public Object Execute(ICodeNode root) {
		 ProgramGenerator generator = ProgramGenerator.getInstance();
    	 ICodeNode res = executeChild(root, 0); 
    	 
    	 String curBranch = generator.getCurrentBranch();
    	 generator.emitComparingCommand();
    	 
    	 
    	 Integer val = (Integer)res.getAttribute(ICodeKey.VALUE);
    	 copyChild(root, res);  
    	 if ((val != null && val != 0) || BaseExecutor.isCompileMode) {
    		 generator.incraseIfElseEmbed();
    		 boolean b = BaseExecutor.inIfElseStatement;
    		 BaseExecutor.inIfElseStatement = false;
    		 executeChild(root, 1);
    		 BaseExecutor.inIfElseStatement = b;
    		 generator.decraseIfElseEmbed();
    	 }
    	 
    	 if (BaseExecutor.inIfElseStatement == true) {
    		 String branchOut = generator.getBranchOut();
    		 generator.emitString(Instruction.GOTO.toString() + " " + branchOut);
    	 } else {
    		 generator.emitString(curBranch + ":\n");
    		 generator.increaseBranch();
    	 }
    	
    	 
	    	
	    return root;
	}

}
