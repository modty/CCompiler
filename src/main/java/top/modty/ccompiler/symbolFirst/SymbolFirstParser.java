package top.modty.ccompiler.symbolFirst;

import top.modty.ccompiler.LL1.LL1Productions;
import top.modty.ccompiler.LL1.LProduction;
import top.modty.ccompiler.commons.Word;
import top.modty.ccompiler.commons.constants.CTokenType;
import top.modty.ccompiler.commons.constants.Grammar;
import top.modty.ccompiler.grammar.Production;
import top.modty.ccompiler.grammar.ProductionManager;
import top.modty.ccompiler.semantic.Symbol;

import java.util.*;

/**
 * @author 点木
 * @date 2020-05-16 09:59
 * @mes
 */
public class SymbolFirstParser {
    public HashMap<String, HashSet<String>> firstVT;
    public HashMap<String,HashSet<String>> lastVT;
    public HashMap<String,HashMap<String,String>> selectTable;
    public ArrayList<String> terminals;
    public ArrayList<String> nonterminals;
    public ArrayList<LProduction> productions;
    public ArrayList<SymbolNode> response;

    public SymbolFirstParser() {
        response=new ArrayList<>();
        firstVT=new HashMap<>();
        lastVT=new HashMap<>();
        selectTable=new HashMap<>();
        terminals=new ArrayList<>();
        nonterminals=new ArrayList<>();
        productions=new ArrayList<>();
        initialTest();
        initialFirstVT();
        initialLastVT();
        initialPriorityTable();
    }

    public SymbolFirstParser(ArrayList<LProduction> productions){
        response=new ArrayList<>();
        firstVT=new HashMap<>();
        lastVT=new HashMap<>();
        selectTable=new HashMap<>();
        terminals=new ArrayList<>();
        nonterminals=new ArrayList<>();
        this.productions=productions;
        initialTerminals();
        initialFirstVT();
        initialLastVT();
        initialPriorityTable();
        clearEnd();
//        for(String key:selectTable.keySet()){
//            for(String key1:selectTable.get(key).keySet()){
//                System.out.print("\t"+(selectTable.get(key).get(key1)==null?"0":selectTable.get(key).get(key1)));
//            }
//            System.out.println();
//        }
    }
    public HashSet<String> createFirstVTHelper(String id){
        for(LProduction production:productions){
            if(!production.left.equals(id)) continue;;
            String[] right=production.right;
            // P-->a
            if(terminals.contains(right[0])){
                firstVT.get(id).add(right[0]);
            }else {
                // P-->Qa
                if(right.length>1&&terminals.contains(right[1])){
                    firstVT.get(id).add(right[1]);
                }
                // P-->Q
                if(right[0]!=id){
                    HashSet<String> sonFirst=createFirstVTHelper(right[0]);
                    for(String s:sonFirst){
                        firstVT.get(id).add(s);
                    }
                }
            }

        }
        return firstVT.get(id);
    }
    public void initialFirstVT(){
        for(String ter:nonterminals){
            firstVT.put(ter,new HashSet<>());
        }
        for(LProduction production:productions){

            createFirstVTHelper(production.left);
        }
    }
    /**
     * @author 点木
     * @date 2020/5/16
     * @return
     * @params
     * @mes 得到算符优先关系表
     */
    public void initialPriorityTable(){
        for(int i=0;i<terminals.size();i++){
            HashMap<String,String> temp=new HashMap<>();
            for(int j=0;j<terminals.size();j++){
                temp.put(terminals.get(j),"0");
            }
            selectTable.put(terminals.get(i),temp);
        }
        boolean isFlag=true;
        // 寻找=关系
        for(LProduction production:productions){
            String[] right=production.right;
            // 寻找=关系
            // aQb
            for(int idIndex=0;idIndex<right.length-2;idIndex++){
                if(terminals.contains(right[idIndex])&&!terminals.contains(right[idIndex+1])&&terminals.contains(right[idIndex+2])){
                    if(selectTable.get(right[idIndex]).get(right[idIndex+2]).equals("0")) isFlag=false;
                    selectTable.get(right[idIndex]).put(right[idIndex+2],"=");
                }
            }
            for(int idIndex=0;idIndex<right.length-1;idIndex++){
                // ab
                if(terminals.contains(right[idIndex])&&terminals.contains(right[idIndex+1])){
                    String x=right[idIndex];
                    String y=right[idIndex+1];
                    if(selectTable.get(x).get(y).equals("0")) isFlag=false;
                    selectTable.get(x).put(y,"=");
                }
                // 寻找<关系
                // aQ
                if(terminals.contains(right[idIndex])&&!terminals.contains(right[idIndex+1])){
                    String x=right[idIndex];
                    String y=right[idIndex+1];
                    HashSet<String> tempFisrt=firstVT.get(y);
                    for(String s:tempFisrt){
                        if(selectTable.get(x).get(s).equals("0")) isFlag=false;
                        selectTable.get(x).put(s,"<");
                    }
                }
                // 寻找<关系
                // Qa
                if(!terminals.contains(right[idIndex])&&terminals.contains(right[idIndex+1])){
                    String x=right[idIndex];
                    String y=right[idIndex+1];
                    HashSet<String> tempFisrt=lastVT.get(x);
                    for(String s:tempFisrt){
                        if(selectTable.get(s).get(y)==null) isFlag=false;
                        selectTable.get(s).put(y,">");
                    }
                }
            }


        }
    }
    public void initialTest(){
        productions.clear();
        productions.add(new LProduction("A",new String[]{"#","E","#"}));
        productions.add(new LProduction("E",new String[]{"E","+","T"}));
        productions.add(new LProduction("E",new String[]{"T"}));
        productions.add(new LProduction("T",new String[]{"T","*","F"}));
        productions.add(new LProduction("T",new String[]{"F"}));
        productions.add(new LProduction("F",new String[]{"P","|","F"}));
        productions.add(new LProduction("F",new String[]{"P"}));
        productions.add(new LProduction("P",new String[]{"(","E",")"}));
        productions.add(new LProduction("P",new String[]{"a"}));
        terminals.clear();
        nonterminals.clear();
        terminals.add("+");
        terminals.add("*");
        terminals.add("(");
        terminals.add(")");
        terminals.add("a");
        terminals.add("#");
        terminals.add("|");
        nonterminals.add("E");
        nonterminals.add("F");
        nonterminals.add("T");
        nonterminals.add("P");
        nonterminals.add("A");
    }
    public HashSet<String> createLastVThelper(String id){
        for(LProduction production:productions){
            if(!production.left.equals(id)) continue;;
            String[] right=production.right;
            // P-->a
            if(terminals.contains(right[right.length-1])){
                lastVT.get(id).add(right[right.length-1]);
            }else {
                // P-->Q

                if(!right[right.length-1].equals(id)){
                    HashSet<String> sonLast=createLastVThelper(right[right.length-1]);
                    for(String s:sonLast){
                        lastVT.get(id).add(s);
                    }
                }
                // P-->aQ
                if(right.length>1&&terminals.contains(right[right.length-2])){
                    lastVT.get(id).add(right[right.length-2]);
                }

            }
        }
        return lastVT.get(id);
    }
    public void initialLastVT(){
        for(String ter:nonterminals){
            lastVT.put(ter,new HashSet<>());
        }
        for(LProduction production:productions){

            createLastVThelper(production.left);
        }
    }

    public void clearEnd(){
        List<LProduction> temp=new ArrayList<>();
        for(LProduction production:productions){
            if(production.right.length==1&&production.right[0].equals("$")){
                temp.add(production);
            }
        }
        productions.removeAll(temp);

    }
    public void initialTerminals(){
        for(LProduction production:productions){
            if(!nonterminals.contains(production.left)){
                nonterminals.add(production.left);
            }
        }
        for(LProduction production:productions){
            String[] right=production.right;
            for(int i=0;i<right.length;i++){
                if(nonterminals.contains(right[i])){
                    continue;
                }else if(!terminals.contains(right[i])){
                    terminals.add(right[i]);
                }
            }
        }
    }
    public Object analy_input_string(List<Word> input){
        // 初始化分析栈
        LinkedList<String> ana_head=new LinkedList<>();
        ana_head.push("#");
        String action;
        String actionMes;
        String stackValue;
        // 充当指针，指向当前栈内参与比较的元素，默认是栈顶
        int indicator = ana_head.size() - 1;
        int inputIndex=0;
        String mark="@";
        while (true){
            indicator = ana_head.size() - 1;
            stackValue=getStack(ana_head);
            if(!terminals.contains(ana_head.get(indicator))){
                for(int i=indicator;i>=0;i--){
                    if(terminals.contains(ana_head.get(i))&&!mark.equals(ana_head.get(i))){
                        indicator=i;
                        break;
                    }
                }
            }
            // 对栈顶符号进行比较,获得两者之间的关系
            action=selectTable.get(ana_head.get(indicator)).get(input.get(inputIndex).getWord());
            switch (action){
                case "<":
                    actionMes="移入";
                    break;
                case ">":
                    actionMes="归约";
                    break;
                default:
                    actionMes="接受";
                    break;
            }
            if(ana_head.size()==2&&inputIndex==input.size()-1) {
                response.add(new SymbolNode(stackValue,action,actionMes));
                break;
            }
            switch (action){
                case "<":
                case "=":
                    ana_head.add(input.get(inputIndex).getWord());
                    inputIndex++;
                    break;
                case ">":
                    ArrayList<String> temp=new ArrayList<>();
                    if(indicator==ana_head.size()-1){
                        temp.add(ana_head.get(indicator));
                        if(statute(temp,ana_head.get(indicator))){
                            ana_head.set(indicator,mark);
                        }
                    }else {
                        for(int i=indicator-1;i<indicator+2;i++){
                            temp.add(ana_head.get(i));
                        }
                        if(statute(temp,ana_head.get(indicator))){
                            ana_head.removeLast();
                            ana_head.removeLast();
                            ana_head.removeLast();
                            ana_head.add(mark);
                        }
                    }
                    break;
            }
            response.add(new SymbolNode(stackValue,action,actionMes));
        }
        return response;
    }
    private boolean statute(ArrayList<String> input,String symbol){
        int count=0;
        for(LProduction production:productions){
            ArrayList<String> rights=production.returnRights();
            if(rights.size()==input.size()&&rights.contains(symbol)){
                count=0;
                for(int i=0;i<rights.size();i++){
                    if(rights.get(i).equals(symbol)) count+=1;
                    else if(!terminals.contains(rights.get(i))&&!terminals.contains(input.get(i))) count+=1;
                }
                if(count==rights.size()) return true;
            }
        }
        return false;
    }
    public String getStack(LinkedList<String> stack){
        String result="";
        for(String s:stack){
            result+=s;
        }
        return result;
    }
    public static void main(String[] args) {
        ArrayList<Word> strings=new ArrayList<>();
        strings.add(new Word("a"));
        strings.add(new Word("+"));
        strings.add(new Word("a"));
        strings.add(new Word("#"));
        SymbolFirstParser symbolFirstParser=new SymbolFirstParser();
        symbolFirstParser.analy_input_string(strings);
    }

}
