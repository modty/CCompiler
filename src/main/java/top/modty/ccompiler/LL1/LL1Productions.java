package top.modty.ccompiler.LL1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.modty.ccompiler.commons.Word;
import top.modty.ccompiler.commons.constants.CTokenType;
import top.modty.ccompiler.grammar.Production;
import top.modty.ccompiler.symbolFirst.SymbolFirstParser;

import javax.swing.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @author 点木
 * @date 2020-05-14 22:43
 * @mes
 */
public class LL1Productions {
    private static LL1Productions instance;
    public ArrayList<LProduction> productions;
    public HashMap<String, List<String>> grammers;
    public ArrayList<String> terminals;
    public ArrayList<String> nonterminals;
    public HashMap<String, ArrayList<String>> firsts;
    public HashMap<String, ArrayList<String>> follows;
    public HashMap<String,HashMap<String, ArrayList<String>>> predict;




    public static LL1Productions getInstance(){
        if(instance==null){
            instance=new LL1Productions();
        }
        return instance;
    }
    private LL1Productions() {

        firsts=new HashMap<>();
        follows=new HashMap<>();
        productions=new ArrayList<>();
        predict=new HashMap<>();
        terminals=new ArrayList<>();
        nonterminals=new ArrayList<>();
        grammers=new HashMap<>();
        grammers.put("LL1语法",new ArrayList<>());
        JSONObject object= JSON.parseObject(string);
        for(String key:object.keySet()){
            if(!nonterminals.contains(key)){
                nonterminals.add(key);
            }
        }
        for(String key:object.keySet()){

            JSONArray array=object.getJSONArray(key);
            for(int i=0;i<array.size();i++){
                String s="";
                s+=key;
                String[] temp=new String[array.getJSONArray(i).size()];
                for(int j=0;j<array.getJSONArray(i).size();j++){
                    temp[j]=array.getJSONArray(i).getString(j);
                    if(!(nonterminals.contains(temp[j])||terminals.contains(temp[j])||temp[j].equals("$"))){
                        terminals.add(temp[j]);
                    }
                    s+="@"+temp[j];
                }
                System.out.println(s);
                grammers.get("LL1语法").add(s);
                productions.add(new LProduction(key,temp));
            }
        }
        getFirst();
        getFollow();
        getSelect();
        getPredict();


    }
    public void getFirst(){
// 终结符全部求出first集
        ArrayList<String> first;
        for (int i = 0; i < terminals.size(); i++) {
            first = new ArrayList<String>();
            first.add(terminals.get(i));
            firsts.put(terminals.get(i), first);
        }
        // 给所有非终结符注册一下
        for (int i = 0; i < nonterminals.size(); i++) {
            first = new ArrayList<String>();
            firsts.put(nonterminals.get(i), first);
        }

        boolean flag;
        while (true) {
            flag = true;
            String left;
            String right;
            String[] rights;
            for (int i = 0; i < productions.size(); i++) {
                left = productions.get(i).returnLeft();
                rights = productions.get(i).right;
                for (int j = 0; j < rights.length; j++) {
                    right = rights[j];
                    // right是否存在，遇到空怎么办
                    if(!right.equals("$")){
                        for (int l = 0; l < firsts.get(right).size(); l++) {
                            if(firsts.get(left).contains(firsts.get(right).get(l))){
                                continue;
                            }
                            else {
                                firsts.get(left).add(firsts.get(right).get(l));
                                flag=false;
                            }
                        }
                    }
                    if (isCanBeNull(right)) {
                        continue;
                    }
                    else {
                        break;
                    }
                }
            }
            if (flag==true) {
                break;
            }

        }
        // 非终结符的first集
    }
    // 判断是否产生$
    public boolean isCanBeNull(String symbol){
        String[] rights;
        for (int i = 0; i < productions.size(); i++) {
            // 找到产生式
            if (productions.get(i).returnLeft().equals(symbol)) {
                rights = productions.get(i).right;
                if (rights[0].equals("$")) {
                    return true;
                }
            }
        }
        return false;
    }
    public void getFollow(){
        // 所有非终结符的follow集初始化一下
        ArrayList<String> follow;
        for (int i = 0; i < nonterminals.size(); i++) {
            follow = new ArrayList<String>();
            follows.put(nonterminals.get(i), follow);
        }
        // 将#加入到follow(S)中
        follows.get("S").add("#");
        boolean flag;
        boolean fab;
        while (true) {
            flag = true;
            // 循环
            for (int i = 0; i < productions.size(); i++) {
                String left;
                String right;
                String[] rights;
                rights = productions.get(i).right;
                for (int j = 0; j < rights.length; j++) {
                    right = rights[j];

                    // 非终结符
                    if (nonterminals.contains(right)) {
                        fab = true;
                        for(int k=j+1;k<rights.length;k++){

                            // 查找first集
                            for(int v=0;v<firsts.get(rights[k]).size();v++){
                                // 将后一个元素的first集加入到前一个元素的follow集中
                                if(follows.get(right).contains(firsts.get(rights[k]).get(v))){
                                    continue;
                                }
                                else {
                                    follows.get(right).add(firsts.get(rights[k]).get(v));
                                    flag=false;
                                }
                            }
                            if (isCanBeNull(rights[k])) {
                                continue;
                            }
                            else {
                                fab = false;
                                break;
                            }
                        }
                        if(fab){
                            left = productions.get(i).returnLeft();
                            for (int p = 0; p < follows.get(left).size(); p++) {
                                if (follows.get(right).contains(follows.get(left).get(p))) {
                                    continue;
                                }
                                else {
                                    follows.get(right).add(follows.get(left).get(p));
                                    flag = false;
                                }
                            }
                        }
                    }

                }
            }
            if(flag==true){
                break;
            }
        }

        // 清除follow集中的#
        String left;
        for (int j = 0; j < nonterminals.size(); j++) {
            left = nonterminals.get(j);
            for (int v=0; v<follows.get(left).size(); v++) {
                if(follows.get(left).get(v).equals("#"))
                    follows.get(left).remove(v);
            }
        }
    }
    public void getSelect(){
        String left;
        String right;
        String[] rights;
        ArrayList<String> follow = null;
        ArrayList<String> first = null;

        for (int i = 0; i < productions.size(); i++) {
            left = productions.get(i).returnLeft();
            rights = productions.get(i).right;
            if(rights[0].equals("$")){
                // select(i) = follow(A)
                follow = follows.get(left);
                for (int j = 0; j < follow.size(); j++) {
                    if(productions.get(i).select.contains(follow.get(j))){
                        continue;
                    }
                    else {
                        productions.get(i).select.add(follow.get(j));
                    }
                }
            }
            else {
                boolean flag = true;
                for (int j = 0; j < rights.length; j++) {
                    right = rights[j];
                    first = firsts.get(right);
                    for (int v = 0; v < first.size(); v++) {
                        if (productions.get(i).select.contains(first.get(v))) {
                            continue;
                        }
                        else {
                            productions.get(i).select.add(first.get(v));
                        }
                    }
                    if(isCanBeNull(right)){
                        continue;
                    }
                    else {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    follow = follows.get(left);
                    for (int j = 0; j < follow.size(); j++) {
                        if (productions.get(i).select.contains(follow.get(j))) {
                            continue;
                        }
                        else {
                            // 刚刚这里出现了一个问题，已经被解决啦
                            productions.get(i).select.add(follow.get(j));
                        }
                    }
                }
            }
        }
    }
    public void getPredict(){
        predict=new HashMap<>();
        LProduction production;
        for (int i = 0; i < productions.size(); i++) {
            production = productions.get(i);
            if(!predict.containsKey(production.left))
                predict.put(production.left,new HashMap<>());
            for (int j = 0; j < production.select.size(); j++) {
                ArrayList<String> temp=new ArrayList<>();
                for(int a=0;a<production.right.length;a++){
                    temp.add(production.right[a]);
                }
                predict.get(production.left).put(production.select.get(j),temp);
            }
        }
    }

    public static void main(String[] args) {
        String code="int main(){\n" +
                "   int a;\n" +
                "   a=10;\n" +
                "   if(a>0){\n" +
                "       a=1;\n" +
                "   }\n" +
                "   else{\n" +
                "       a=2;\n" +
                "   }\n" +
                "   while(a>0){\n" +
                "       a=3;\n" +
                "   }\n" +
                "}";
        TextLex textLex=new TextLex(code);
        textLex.scannerAll();
        // 获得结果的表
        ArrayList<Word> lex_result_stack = textLex.get_Lex_Result();
        ArrayList<HashMap<String, String>> lex_error_stack = textLex.get_Lex_Error();
        SymbolFirstParser parser=new SymbolFirstParser();
        parser.analy_input_string(lex_result_stack);
//        // 若是存在词法分析错误
//        if(lex_error_stack.size()!=0){
//            System.out.println("语法分析出错");
//        }
//        else {
//            // 句法分析
//            TextParse textParse = new TextParse(lex_result_stack);
//            textParse.Parsing();
//
//        }

    }

    String string="{\n" +
            "    \"S\": [\n" +
            "        [\n" +
            "            \"FUNC\",\n" +
            "            \"FUNCS\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"FUNCS\": [\n" +
            "        [\n" +
            "            \"FUNC\",\n" +
            "            \"FUNCS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"FUNC\": [\n" +
            "        [\n" +
            "            \"TYPE\",\n" +
            "            \"IDN\",\n" +
            "            \"(\",\n" +
            "            \"ARGS\",\n" +
            "            \")\",\n" +
            "            \"FUNC_BODY\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"TYPE\": [\n" +
            "        [\n" +
            "            \"INT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"SHORT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"LONG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"CHAR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"FLOAT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"DOUBLE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"VOID\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"UNSIGNED\",\n" +
            "            \"TYPE\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ARGS\": [\n" +
            "        [\n" +
            "            \"TYPE\",\n" +
            "            \"IDN\",\n" +
            "            \"ARG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ARG\": [\n" +
            "        [\n" +
            "            \",\",\n" +
            "            \"TYPE\",\n" +
            "            \"IDN\",\n" +
            "            \"ARG\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"FUNC_BODY\": [\n" +
            "        [\n" +
            "            \";\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"BLOCK\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"BLOCK\": [\n" +
            "        [\n" +
            "            \"{\",\n" +
            "            \"DEFINE_STMTS\",\n" +
            "            \"STMTS\",\n" +
            "            \"}\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"DEFINE_STMTS\": [\n" +
            "        [\n" +
            "            \"DEFINE_STMT\",\n" +
            "            \"DEFINE_STMTS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"DEFINE_STMT\": [\n" +
            "        [\n" +
            "            \"TYPE\",\n" +
            "            \"IDN\",\n" +
            "            \"INIT\",\n" +
            "            \"VARS\",\n" +
            "            \";\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"INIT\": [\n" +
            "        [\n" +
            "            \"=\",\n" +
            "            \"EXPRESSION\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"VARS\": [\n" +
            "        [\n" +
            "            \",\",\n" +
            "            \"IDN\",\n" +
            "            \"INIT\",\n" +
            "            \"VARS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"STMTS\": [\n" +
            "        [\n" +
            "            \"STMT\",\n" +
            "            \"STMTS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"STMT\": [\n" +
            "        [\n" +
            "            \"ASSIGN_STMT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"JUMP_STMT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"ITERATION_STMT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"BRANCH_STMT\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ASSIGN_STMT\": [\n" +
            "        [\n" +
            "            \"EXPRESSION\",\n" +
            "            \";\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"JUMP_STMT\": [\n" +
            "        [\n" +
            "            \"CONTINUE\",\n" +
            "            \";\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"BREAK\",\n" +
            "            \";\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"RETURN\",\n" +
            "            \"ISNULL_EXPR\",\n" +
            "            \";\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ITERATION_STMT\": [\n" +
            "        [\n" +
            "            \"WHILE\",\n" +
            "            \"(\",\n" +
            "            \"LOGICAL_EXPRESSION\",\n" +
            "            \")\",\n" +
            "            \"BLOCK_STMT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"FOR\",\n" +
            "            \"(\",\n" +
            "            \"ISNULL_EXPR\",\n" +
            "            \";\",\n" +
            "            \"ISNULL_EXPR\",\n" +
            "            \";\",\n" +
            "            \"ISNULL_EXPR\",\n" +
            "            \")\",\n" +
            "            \"BLOCK_STMT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"DO\",\n" +
            "            \"BLOCK_STMT\",\n" +
            "            \"WHILE\",\n" +
            "            \"(\",\n" +
            "            \"LOGICAL_EXPRESSION\",\n" +
            "            \")\",\n" +
            "            \";\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"BRANCH_STMT\": [\n" +
            "        [\n" +
            "            \"IF\",\n" +
            "            \"(\",\n" +
            "            \"LOGICAL_EXPRESSION\",\n" +
            "            \")\",\n" +
            "            \"BLOCK_STMT\",\n" +
            "            \"RESULT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"SWITCH\",\n" +
            "            \"(\",\n" +
            "            \"IDN\",\n" +
            "            \")\",\n" +
            "            \"{\",\n" +
            "            \"CASE_STMT\",\n" +
            "            \"CASE_STMTS\",\n" +
            "            \"DEFAULT_STMT\",\n" +
            "            \"}\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"RESULT\": [\n" +
            "        [\n" +
            "            \"ELSE\",\n" +
            "            \"BLOCK_STMT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"LOGICAL_EXPRESSION\": [\n" +
            "        [\n" +
            "            \"!\",\n" +
            "            \"EXPRESSION\",\n" +
            "            \"BOOL_EXPRESSION\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"EXPRESSION\",\n" +
            "            \"BOOL_EXPRESSION\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"BOOL_EXPRESSION\": [\n" +
            "        [\n" +
            "            \"LOP\",\n" +
            "            \"EXPRESSION\",\n" +
            "            \"BOOL_EXPRESSION\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"LOP\": [\n" +
            "        [\n" +
            "            \"&&\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"||\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"CASE_STMTS\": [\n" +
            "        [\n" +
            "            \"CASE_STMT\",\n" +
            "            \"CASE_STMTS\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"CASE_STMT\": [\n" +
            "        [\n" +
            "            \"CASE\",\n" +
            "            \"CONST\",\n" +
            "            \":\",\n" +
            "            \"STMTS\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"DEFAULT_STMT\": [\n" +
            "        [\n" +
            "            \"DEFAULT\",\n" +
            "            \":\",\n" +
            "            \"STMTS\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"BLOCK_STMT\": [\n" +
            "        [\n" +
            "            \"{\",\n" +
            "            \"STMTS\",\n" +
            "            \"}\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ISNULL_EXPR\": [\n" +
            "        [\n" +
            "            \"EXPRESSION\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"EXPRESSION\": [\n" +
            "        [\n" +
            "            \"VALUE\",\n" +
            "            \"OPERATION\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"OPERATION\": [\n" +
            "        [\n" +
            "            \"COMPARE_OP\",\n" +
            "            \"VALUE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"EQUAL_OP\",\n" +
            "            \"VALUE\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"COMPARE_OP\": [\n" +
            "        [\n" +
            "            \">\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \">=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"<\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"<=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"==\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"!=\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"EQUAL_OP\": [\n" +
            "        [\n" +
            "            \"=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"+=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"-=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"*=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"/=\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"%=\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"VALUE\": [\n" +
            "        [\n" +
            "            \"ITEM\",\n" +
            "            \"VALUE'\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"VALUE'\": [\n" +
            "        [\n" +
            "            \"+\",\n" +
            "            \"ITEM\",\n" +
            "            \"VALUE'\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"-\",\n" +
            "            \"ITEM\",\n" +
            "            \"VALUE'\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ITEM\": [\n" +
            "        [\n" +
            "            \"FACTOR\",\n" +
            "            \"ITEM'\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ITEM'\": [\n" +
            "        [\n" +
            "            \"*\",\n" +
            "            \"FACTOR\",\n" +
            "            \"ITEM'\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"/\",\n" +
            "            \"FACTOR\",\n" +
            "            \"ITEM'\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"%\",\n" +
            "            \"FACTOR\",\n" +
            "            \"ITEM'\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"FACTOR\": [\n" +
            "        [\n" +
            "            \"(\",\n" +
            "            \"VALUE\",\n" +
            "            \")\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"IDN\",\n" +
            "            \"CALL_FUNC\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"CONST\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"CALL_FUNC\": [\n" +
            "        [\n" +
            "            \"(\",\n" +
            "            \"ES\",\n" +
            "            \")\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ES\": [\n" +
            "        [\n" +
            "            \"ISNULL_EXPR\",\n" +
            "            \"ISNULL_ES\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"ISNULL_ES\": [\n" +
            "        [\n" +
            "            \",\",\n" +
            "            \"ISNULL_EXPR\",\n" +
            "            \"ISNULL_ES\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"$\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"CONST\": [\n" +
            "        [\n" +
            "            \"NUM_CONST\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"FLOAT\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"CHAR\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"STR\"\n" +
            "        ]\n" +
            "    ],\n" +
            "    \"NUM_CONST\": [\n" +
            "        [\n" +
            "            \"INT10\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"INT8\"\n" +
            "        ],\n" +
            "        [\n" +
            "            \"INT16\"\n" +
            "        ]\n" +
            "    ]\n" +
            "}";
}
