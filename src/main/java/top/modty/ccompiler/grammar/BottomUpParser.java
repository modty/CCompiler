package top.modty.ccompiler.grammar;

import top.modty.ccompiler.grammar.initial.ProductionManager;

/**
 * @author 点木
 * @date 2020-05-03 19:32
 * @mes
 */
public class BottomUpParser {
    public static void main(String[] args) {
        /*
         * 把ProductionManager , top.dianmu.ccompiler.day52.top.dianmu.ccompiler.day53.top.dianmu.ccompiler.day54.top.dianmu.ccompiler.day55.top.dianmu.ccompiler.day61.top.dianmu.ccompiler.day65.top.dianmu.ccompiler.day66.top.dianmu.ccompiler.day67.top.dianmu.ccompiler.day68.top.dianmu.ccompiler.day70.BottomUpParser.top.dianmu.ccompiler.day53.top.dianmu.ccompiler.day55.top.dianmu.ccompiler.day61.top.dianmu.ccompiler.day65.top.dianmu.ccompiler.day66.top.dianmu.ccompiler.day67.top.dianmu.ccompiler.day68.top.dianmu.ccompiler.day70.FirstSetBuilder 两个类的初始化移到CGrammarInitializer
         * 将SymbolDefine 修改成CTokenType, 确定表达式的first set集合运算正确
         */
        ProductionManager productionManager = ProductionManager.getProductionManager();
        // 初始化语法推导表达式
        productionManager.initProductions();
        // 打印语法推导表达式
        productionManager.printAllProductions();
        productionManager.runFirstSetAlgorithm();

    }
}
