package top.modty.ccompiler.semantic;

import top.modty.ccompiler.commons.Node;
import top.modty.ccompiler.commons.Nodes;
import top.modty.ccompiler.commons.Word;
import top.modty.ccompiler.commons.constants.CTokenType;
import top.modty.ccompiler.grammar.Production;
import top.modty.ccompiler.grammar.ProductionManager;
import top.modty.ccompiler.grammar.Symbols;
import top.modty.ccompiler.lex.Lexer;

import java.util.*;

/**
 * @author 点木
 * @date 2020-05-14 19:52
 * @mes
 */
public class LL1StateTableParser {
    private LinkedList<Word> recognizedWords;
    private LinkedList<Integer> deduceStack;
    private int[][] parseTable;
    ArrayList<Symbols> symbolArray;
    public  LL1StateTableParser(ArrayList<Word> recognizedWords) {
        this.recognizedWords=new LinkedList<>();
        for(Word word:recognizedWords){
            this.recognizedWords.add(word);
        }
        deduceStack=new LinkedList<>();
        parseTable=ProductionManager.getProductionManager().firstFollowSelectSetBuilder.parseTable;
        this.symbolArray = ProductionManager.getProductionManager().firstFollowSelectSetBuilder.symbolArray;

    }

    public HashMap<String, List<HashMap<String, Object>>> parse(){
        deduceStack.add(CTokenType.EXT_DEF.ordinal());
        // 防止出现左递归
        int pre=-1;
        Nodes treeList=null;
        for(int i=0;i<recognizedWords.size();i++){
            int preType=deduceStack.get(0);
            int laskType=recognizedWords.get(i).getType();
            if(pre!=preType){
                if(!CTokenType.isTerminal(preType)){
                    pre=preType;
                    if(addGrammer(preType,laskType-CTokenType.FIRST_TERMINAL_INDEX)){
                        i--;
                    }else {
                        deduceStack.remove(0);
                    }
                }else{
                    System.out.println(CTokenType.getSymbolStr(preType));
                    deduceStack.remove(0);
                }
            }else {
                pre=-1;
                i--;
                deduceStack.remove(0);
            }
        }
        return null;
    }


    private boolean addGrammer(int preType,int nextType){
        int action=parseTable[preType][nextType];
        if(action!=-1){
            int[] actions=symbolArray.get(action/100-1).productions.get(action%100);

            deduceStack.remove(0);
            for(int j=actions.length-1;j>=0;j--){
                deduceStack.add(0,actions[j]);
            }
            return true;
        }
        return false;
    }
}
