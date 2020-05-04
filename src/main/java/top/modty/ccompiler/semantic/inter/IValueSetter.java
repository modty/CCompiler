package top.modty.ccompiler.semantic.inter;


import top.modty.ccompiler.semantic.Symbol;

public interface IValueSetter {
   public void setValue(Object obj) throws Exception;
   public Symbol getSymbol();
}
