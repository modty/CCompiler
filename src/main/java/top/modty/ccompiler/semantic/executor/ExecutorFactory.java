package top.modty.ccompiler.semantic.executor;


import top.modty.ccompiler.commons.constants.CTokenType;
import top.modty.ccompiler.commons.constants.ICodeKey;
import top.modty.ccompiler.semantic.inter.Executor;
import top.modty.ccompiler.semantic.inter.ICodeNode;

public class ExecutorFactory {
    private static ExecutorFactory executorFactory = null;
    private ExecutorFactory() {
    	
    }
    
    public static ExecutorFactory getExecutorFactory() {
    	if (executorFactory == null) {
    		executorFactory = new ExecutorFactory();
    	}
    	
    	return executorFactory;
    }
    
    public Executor getExecutor(ICodeNode node) {
    	
    	if (node == null) {
    		return null;
    	}
    	
    	CTokenType type = (CTokenType)node.getAttribute(ICodeKey.TokenType);
    	switch (type) {
    	//change here
    	case DEF:
    		return new DefExecutor();
    	case DEF_LIST:
    		return new DefListExecutor();
    	case LOCAL_DEFS:
    	    return new LocalDefExecutor();
    	
    	case UNARY:
    		return new UnaryNodeExecutor();
    	case BINARY:
    		return new BinaryExecutor();
    	case NO_COMMA_EXPR:
    		return new NoCommaExprExecutor();
    	case EXPR:
    		return new ExprExecutor();
    	case STATEMENT:
    		return new StatementExecutor();
    	case STMT_LIST:
    		return new StatementListExecutor();
    	case TEST:
    		return new TestExecutor();
    	case IF_STATEMENT:
    		return new IfStatementExecutor();
    	case IF_ELSE_STATEMENT:
    		return new ElseStatementExecutor();
    		
    	case OPT_EXPR:
    		return new OptExprExecutor();
    		
    	case END_OPT_EXPR:
    		return new EndOptExecutor();
    		
    	case INITIALIZER:
    		return new InitializerExecutor();
    		
    	case COMPOUND_STMT:
    		return new CompoundStmtExecutor();
    		
    	case FUNCT_DECL:
    		return new FunctDeclExecutor();
    		
    	case EXT_DEF:
    		return new ExtDefExecutor();
    		
    	case ARGS:
    		return new ArgsExecutor();
    		
    	}
    	
    	return null;
    }
}
