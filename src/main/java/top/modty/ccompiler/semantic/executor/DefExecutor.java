package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.commons.constants.Instruction;
import top.modty.ccompiler.grammar.CGrammarInitializer;
import top.modty.ccompiler.semantic.Declarator;
import top.modty.ccompiler.semantic.ProgramGenerator;
import top.modty.ccompiler.semantic.Specifier;
import top.modty.ccompiler.semantic.Symbol;
import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class DefExecutor extends BaseExecutor {

	@Override
	public Object Execute(ICodeNode root) {
		int production = (int)root.getAttribute(ICodeKey.PRODUCTION);
		ProgramGenerator generator = ProgramGenerator.getInstance();
		Symbol symbol = (Symbol)root.getAttribute(ICodeKey.SYMBOL);
		switch (production) {
		case CGrammarInitializer.Specifiers_DeclList_Semi_TO_Def:
			 Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
			 if (declarator != null) {
				 if (symbol.getSpecifierByType(Specifier.STRUCTURE) == null) {
						//如果是结构体数组，这里不做处理
					    generator.createArray(symbol);
				 }
			 } else {
				 //change here 遇到变量定义时，自动将其初始化为0
				 int i = generator.getLocalVariableIndex(symbol);
				 generator.emit(Instruction.SIPUSH, ""+0);
				 generator.emit(Instruction.ISTORE, ""+i);
			 }
			 
			 break; 
		}
		
		return root;
	}

}
