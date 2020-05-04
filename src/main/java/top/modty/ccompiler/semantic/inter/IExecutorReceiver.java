package top.modty.ccompiler.semantic.inter;


import top.modty.ccompiler.semantic.inter.ICodeNode;

public interface IExecutorReceiver {
    public void handleExecutorMessage(ICodeNode code);
}
