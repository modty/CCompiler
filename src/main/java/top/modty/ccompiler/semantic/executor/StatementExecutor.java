package top.modty.ccompiler.semantic.executor;


import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.commons.constants.Instruction;
import top.modty.ccompiler.grammar.CGrammarInitializer;
import top.modty.ccompiler.semantic.code.ProgramGenerator;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class StatementExecutor extends BaseExecutor {
	private ProgramGenerator generator = ProgramGenerator.getInstance();
	
	private enum LoopType {
		FOR,
		WHILE,
		DO_WHILE
	};
	
		
	 @Override 
	 public Object Execute(ICodeNode root) {
		 int production = (int)root.getAttribute(ICodeKey.PRODUCTION);
		 ICodeNode node;
		 
		 switch (production) {
		 case CGrammarInitializer.LocalDefs_TO_Statement:
			 executeChild(root, 0);
			 break;
			 
		 case CGrammarInitializer.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
			 //解析结点 OptExpr ，即初始化变量
			 executeChild(root, 0);
			 
			 if (BaseExecutor.isCompileMode) {
				 generator.emitLoopBranch();
				 String branch = generator.getCurrentBranch();
				 isLoopContinute(root, LoopType.FOR);
				 generator.emitComparingCommand();
				 String loop = generator.getLoopBranch();
				 generator.increaseLoopCount();
				 generator.increaseBranch();
				 
				 
				 
				 executeChild(root, 3);
				 executeChild(root, 2); 
				 
				 generator.emitString(Instruction.GOTO + " " + loop);
				 
				 generator.emitString("\n" + branch + ":\n");
				 
			 }
			 /* 执行的是执行树中第二个节点，也就是Test节点，
			  * 对应的是for 语句中的中间循环判断语句，如果返回的结果不等于0，也就是循环条件满足，
			  * 那么执行循环体内部的语句代码，也就是通过调用executeChild(root, 3);
			  * 从而执行执最下面的Statement节点
			  * 最后通过调用executeChild(root, 2); 执行EndOptExpr节点，对应于for循环，就是语句i++;
			  */
			 while(BaseExecutor.isCompileMode == false && isLoopContinute(root, LoopType.FOR)) {
				 //execute statment in for body
				 executeChild(root, 3);
				 
				 //execute EndOptExpr
				 executeChild(root, 2); 
			 }
			 
			 break;
			 
		 case CGrammarInitializer.While_LP_Test_Rp_TO_Statement:
			 //change here
			 if (BaseExecutor.isCompileMode) {
				 generator.emitLoopBranch();
				 String branch = generator.getCurrentBranch();
				 /*
				  * 先判断循环条件
				  */
				 executeChild(root, 0);
				 generator.emitComparingCommand();
				 /*   change here increase branch and loop in order to ensure that 
				  *   if/else or loop contained in the loop body have correct branch count
				  */
				 
				 String loop = generator.getLoopBranch();
				 generator.increaseLoopCount();
				 generator.increaseBranch();
				 
				 executeChild(root, 1); 
				 
				 
				 generator.emitString(Instruction.GOTO + " " + loop);
				 
				 generator.emitString("\n" + branch + ":\n");
				 
			 }

			 while (BaseExecutor.isCompileMode == false && isLoopContinute(root, LoopType.WHILE)) {
				 executeChild(root, 1);
			 }
			 break;
			 
		 case CGrammarInitializer.Do_Statement_While_Test_To_Statement:
			 do {
				 executeChild(root, 0);
			 } while(isLoopContinute(root, LoopType.DO_WHILE));
			 
			 break;
			 
		 case  CGrammarInitializer.Return_Semi_TO_Statement:
		 	// 函数return 告诉父结点不再执行return之后的代码
			 isContinueExecution(false);
			 
			 break;
			 
		 case  CGrammarInitializer.Return_Expr_Semi_TO_Statement:
		 	// 如果return后面有返回值，先执行返回值表达式
			 node = executeChild(root, 0);
			 Object obj = node.getAttribute(ICodeKey.VALUE);
			 setReturnObj(obj);
			 isContinueExecution(false);
			 
			 break;
			 
		 default:
			 executeChildren(root);
			
			 break;
		 }
		 
	     return root;
	 }
	 /**
	  * @author 点木
	  * @date 2020/5/17 
	  * @return 
	  * @params 
	  * @mes
	 */
	 private boolean isLoopContinute(ICodeNode root, LoopType type) {
		 ICodeNode res = null;
		 if (type == LoopType.FOR || type == LoopType.DO_WHILE) {
			 res = executeChild(root, 1);
		 }
		 else if (type == LoopType.WHILE) {
			 res = executeChild(root, 0);
		 }
		 
		 int result = (Integer)res.getAttribute(ICodeKey.VALUE);
		 return res != null && result != 0;
		 
	 }
}
