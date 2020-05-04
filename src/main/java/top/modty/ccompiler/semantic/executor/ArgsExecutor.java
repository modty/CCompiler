package top.modty.ccompiler.semantic.executor;

import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.grammar.CGrammarInitializer;
import top.modty.ccompiler.semantic.inter.ICodeNode;

import java.util.ArrayList;

public class ArgsExecutor extends BaseExecutor {
	
	@Override
	public Object Execute(ICodeNode root) {
		int production = (Integer)root.getAttribute(ICodeKey.PRODUCTION);
		ArrayList<Object> argList = new ArrayList<Object>();
		ArrayList<Object> symList = new ArrayList<Object>();
		ICodeNode child ;
				
		switch (production) {
		case CGrammarInitializer.NoCommaExpr_TO_Args:
			child = (ICodeNode)executeChild(root, 0);
			Object objVal = child.getAttribute(ICodeKey.VALUE);
			argList.add(objVal);
			objVal = child.getAttribute(ICodeKey.SYMBOL);
			symList.add(objVal);
			
			break;
			
		case CGrammarInitializer.NoCommaExpr_Comma_Args_TO_Args:
			child = executeChild(root, 0);
			objVal = child.getAttribute(ICodeKey.VALUE);
			argList.add(objVal);
			objVal = child.getAttribute(ICodeKey.SYMBOL);
			symList.add(objVal);
			
			child = (ICodeNode)executeChild(root, 1);
			ArrayList<Object> list = (ArrayList<Object>)child.getAttribute(ICodeKey.VALUE);
			argList.addAll(list);
			list = (ArrayList<Object>)child.getAttribute(ICodeKey.SYMBOL);
			for (int i = 0; i < list.size(); i++) {
			    symList.add(list.get(i));
			}
			break;
		}
		
		root.setAttribute(ICodeKey.VALUE, argList);
		root.setAttribute(ICodeKey.SYMBOL, symList);
		
		return root;
	}
	
}
