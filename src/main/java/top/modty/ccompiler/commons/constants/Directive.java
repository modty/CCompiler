package top.modty.ccompiler.commons.constants;
/**
 * @author 点木
 * @date 2020-05-05 16:05
 * @mes java相关宏指令
 */
public enum Directive {
    // 类
    CLASS_PUBLIC(".class public"),
    END_CLASS(".end class"),
    // 父类
    SUPER(".super"),
    FIELD_PRIVATE_STATIC(".field private static"),
    METHOD_STATIC(".method static"),
    METHOD_PUBLIC(".method public"),
    FIELD_PUBLIC(".field public"),
    METHOD_PUBBLIC_STATIC(".method public static"),
    END_METHOD(".end method"),
    LIMIT_LOCALS(".limit locals"),
    LIMIT_STACK(".limit stack"),
    VAR(".var"),
    LINE(".line");
    
    private String text;
    
    Directive(String text) {
    	this.text = text;
    }
    
    public String toString() {
    	return text;
    }
}
