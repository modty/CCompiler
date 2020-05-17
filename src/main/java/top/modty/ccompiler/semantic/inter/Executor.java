package top.modty.ccompiler.semantic.inter;


/**
 * @author 点木
 * @date 2020/5/17
 * @return
 * @params
 * @mes 解释器接口，每个节点都会有一个接口，比如对于NO_COMMA_EXPR将有一个NoCommaExprExecutor实现类
*/
public interface Executor {
    public Object Execute(ICodeNode root);
}
