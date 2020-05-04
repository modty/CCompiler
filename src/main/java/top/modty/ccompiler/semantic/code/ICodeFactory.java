package top.modty.ccompiler.semantic.code;


import top.modty.ccompiler.commons.constants.CTokenType;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class ICodeFactory {
    
    public static ICodeNode createICodeNode(CTokenType type) {
    	return new ICodeNodeImpl(type);
    }
}
