package top.modty.ccompiler;

import top.modty.ccompiler.grammar.GrammarStateManager;
import top.modty.ccompiler.grammar.ProductionManager;
import top.modty.ccompiler.lex.Lexer;
import top.modty.ccompiler.semantic.*;
import top.modty.ccompiler.semantic.code.CodeTreeBuilder;
import top.modty.ccompiler.semantic.executor.BaseExecutor;

/**
 * @author 点木
 * @date 2020-05-04 16:46
 * @mes
 */
public class BottomUpParser {
    private String code;
    private Lexer lexer;
    public BottomUpParser() {
        BaseExecutor.isCompileMode = true;
        /*
         * 把ProductionManager , top.dianmu.ccompiler.day52.top.dianmu.ccompiler.day53.top.dianmu.ccompiler.day54.top.dianmu.ccompiler.day55.top.dianmu.ccompiler.day61.top.dianmu.ccompiler.day65.top.dianmu.ccompiler.day66.top.dianmu.ccompiler.day67.top.dianmu.ccompiler.day68.top.dianmu.ccompiler.day70.BottomUpParser.top.dianmu.ccompiler.day53.top.dianmu.ccompiler.day55.top.dianmu.ccompiler.day61.top.dianmu.ccompiler.day65.top.dianmu.ccompiler.day66.top.dianmu.ccompiler.day67.top.dianmu.ccompiler.day68.top.dianmu.ccompiler.day70.FirstSetBuilder 两个类的初始化移到CGrammarInitializer
         * 将SymbolDefine 修改成CTokenType, 确定表达式的first set集合运算正确
         */
        ProductionManager productionManager = ProductionManager.getProductionManager();
        // 初始化语法推导表达式
        productionManager.initProductions();
        // 打印语法推导表达式
        // productionManager.printAllProductions();
        // 生成并打印fisrt集合
        productionManager.runFirstSetAlgorithm();

        GrammarStateManager stateManager = GrammarStateManager.getGrammarManager();
        stateManager.buildTransitionStateMachine();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Lexer getLexer() {
        return lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public Object LexCodes(String code){
        lexer=new Lexer(code);
        return lexer.getRecognizedMap();
    }
    public static void main(String[] args) {

        String code="\n" +
                "void quicksort(int A[10], int p, int r) {\n" +
                "    int x;\n" +
                "    int i;\n" +
                "    i = p - 1;\n" +
                "    int j;\n" +
                "    int t;\n" +
                "    int v;\n" +
                "    v = r - 1;\n" +
                "    if (p < r) {\n" +
                "       x = A[r];\n" +
                "      \n" +
                "        for (j = p; j <= v; j++) {\n" +
                "            if (A[j] <= x) {  \n" +
                "                 i++;\n" +
                "                 t = A[i];\n" +
                "                 A[i] = A[j];\n" +
                "                 A[j] = t; \n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        v = i + 1;\n" +
                "        t = A[v];\n" +
                "        A[v] = A[r];\n" +
                "        A[r] = t;\n" +
                "         \n" +
                "         t = v - 1;\n" +
                "         quicksort(A, p,  t);\n" +
                "         t = v + 1;\n" +
                "         quicksort(A, t,  r);\n" +
                "    } \n" +
                "    \n" +
                "}\n" +
                "\n" +
                "\n" +
                "void main () {\n" +
                "    int a[10];\n" +
                "    int i;\n" +
                "    int t;\n" +
                "    printf(\"before quick sort:\");\n" +
                "    for(i = 0; i < 10; i++) {\n" +
                "        t = (10 - i);\n" +
                "        a[i] = t;\n" +
                "        printf(\"value of a[%d] is %d\", i, a[i]);\n" +
                "    }   \n" +
                "    \n" +
                "    \n" +
                "    quicksort(a, 0, 9);\n" +
                "    \n" +
                "    printf(\"after quick sort:\");\n" +
                "    for (i = 0; i < 10; i++) {\n" +
                "        printf(\"value of a[%d] is %d\", i, a[i]);\n" +
                "    }\n" +
                "   \n" +
                "}";
//        code="void main(){\n" +
//                "    \t\tint a;\n" +
//                "    \t\tint b;\n" +
//                "    \t\tb=10;\n" +
//                "\t\t}";
        Lexer lexer=new Lexer(code);
        lexer.getRecognizedMap().forEach(System.out::println);
        LRStateTableParser parser = new LRStateTableParser(lexer);
        parser.parse();
        ProgramGenerator generator = ProgramGenerator.getInstance();
        generator.generateHeader();
        CodeTreeBuilder treeBuilder = CodeTreeBuilder.getCodeTreeBuilder();
        Intepretor intepretor = Intepretor.getIntepretor();
        if (intepretor != null) {
            intepretor.Execute(treeBuilder.getCodeTreeRoot());
        }
        generator.finish();
    }
}
