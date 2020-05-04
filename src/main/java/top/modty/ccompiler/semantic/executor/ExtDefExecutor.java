package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.grammar.CGrammarInitializer;
import top.modty.ccompiler.semantic.Symbol;
import top.modty.ccompiler.semantic.TypeSystem;
import top.modty.ccompiler.semantic.executor.BaseExecutor;
import top.modty.ccompiler.semantic.inter.ICodeNode;
import top.modty.ccompiler.semantic.inter.IValueSetter;

import java.util.ArrayList;

public class ExtDefExecutor extends BaseExecutor {
    private ArrayList<Object> argsList = new ArrayList<Object>();
    ICodeNode root;
    String funcName;
	@Override
	public Object Execute(ICodeNode root) {
		this.root = root;
		int production = (Integer)root.getAttribute(ICodeKey.PRODUCTION);
		switch (production) {
		case CGrammarInitializer.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
			ICodeNode child = root.getChildren().get(0); 
			funcName = (String)child.getAttribute(ICodeKey.TEXT);
			root.setAttribute(ICodeKey.TEXT, funcName);
			saveArgs();
			executeChild(root, 0);
			//problem here
			executeChild(root, 1);
			Object returnVal = getReturnObj();
			clearReturnObj();
				
			if (returnVal != null) {
				root.setAttribute(ICodeKey.VALUE, returnVal);
			}
				
			isContinueExecution(true);
			
			restoreArgs();
			
			break;
		}
		return root;
	}
	
	private void saveArgs() {
		System.out.println("Save arguments....");
		TypeSystem typeSystem = TypeSystem.getTypeSystem();
		ArrayList<Symbol> args = typeSystem.getSymbolsByScope(funcName);
		int count = 0;
		while (count < args.size()) {
			Symbol arg = args.get(count);
			Object value = arg.getValue();
			argsList.add(value);
			count++;
			}
	}
	
    private void restoreArgs() {
    	System.out.println("Restore arguments....");
    	TypeSystem typeSystem = TypeSystem.getTypeSystem();
		ArrayList<Symbol> args = typeSystem.getSymbolsByScope(funcName);
		int count = 0;
    	
    	while (args != null && count < argsList.size()) {
    		IValueSetter setter = (IValueSetter)args.get(count);
    		try {
    			Object value = argsList.get(count);
    			setter.setValue(value);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		count++;
    	}
    }

}
