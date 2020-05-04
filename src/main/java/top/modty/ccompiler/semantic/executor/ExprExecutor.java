package top.modty.ccompiler.semantic.executor;


import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.grammar.CGrammarInitializer;
import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class ExprExecutor extends BaseExecutor {
    @Override 
    public Object Execute(ICodeNode root) {
    	executeChildren(root);
    	int production = (int)root.getAttribute(ICodeKey.PRODUCTION);
    	
    	switch (production) {
    	case CGrammarInitializer.NoCommaExpr_TO_Expr:
    		copyChild(root, root.getChildren().get(0));
    		break;
    	}
    	
    	return root;
    }
}
