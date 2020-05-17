package top.modty.ccompiler.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import top.modty.ccompiler.LL1.LL1Productions;
import top.modty.ccompiler.LL1.LProduction;
import top.modty.ccompiler.LL1.TextLex;
import top.modty.ccompiler.LL1.TextParse;
import top.modty.ccompiler.Parser;
import top.modty.ccompiler.commons.Word;
import top.modty.ccompiler.commons.constants.Grammar;
import top.modty.ccompiler.dfaNfa.ThompsonConstruction;
import top.modty.ccompiler.grammar.ProductionManager;
import top.modty.ccompiler.grammar.Symbols;
import top.modty.ccompiler.lex.Lexer;
import top.modty.ccompiler.semantic.LRStateTableParser;
import top.modty.ccompiler.symbolFirst.SymbolFirstParser;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 点木
 * @date 2020-05-04 20:16
 * @mes
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ApiController {
    Parser parser =new Parser();
    Grammar grammar=new Grammar();
    String code;
    HashMap<String,String> macro;
    ArrayList<String> regularExpr;
    String strings;
    SymbolFirstParser symbolFirstParser;
    @PostMapping("/setCode")
    public void initial(HttpServletRequest request){
        this.code=request.getParameterMap().get("code")[0];
        parser.setLexer(new Lexer(code));
    }
    @GetMapping("/lexMap")
    public Object lexMap(){
        Map<String,Object> response=new HashMap<>();
        response.put("right", parser.getLexer().getRecognizedMap());
        return response;
    }
    @GetMapping("/grammer")
    public HashMap<String, List<String>> grammer(String key){
        if("ll1".equals(key)){
            return LL1Productions.getInstance().grammers;
        }else {
            return grammar.grammers;
        }
    }
    @GetMapping("/LRtree")
    public JSONObject LRtree(){
        LRStateTableParser parser=null;
        HashMap<String, List<HashMap<String, Object>>> response=null;
        try{
            Lexer lexer=new Lexer(code);
            lexer.getRecognizedMap().forEach(System.out::println);
            parser = new LRStateTableParser(lexer);
            System.out.println(code);
            response= parser.parse();
        }catch (Exception e){
         e.printStackTrace();
        }finally {
            parser.clear();
            JSONObject object=new JSONObject();
            object.put("tree",response);
            object.put("actions",parser.actions);
            return object;
        }
    }
    @GetMapping("/LL1tree")
    public JSONObject LL1tree(){
        code="int main(){\n" +
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
        // 若是存在词法分析错误
        if(lex_error_stack.size()!=0){
            return new JSONObject();
        }
        else {
            // 句法分析
            TextParse textParse = new TextParse(lex_result_stack);
            JSONObject res=new JSONObject();
            res.put("tree",textParse.Parsing());
            res.put("actions",textParse.getActions());
            return res;
        }
    }
    @GetMapping("/sympolTree")
    public HashMap<String,List<HashMap<String,Object>>> sympolTree(){
        return null;
    }
    @GetMapping("/firstSympol")
    public Object firstSympol(String key){
        if("ll1".equals(key)){
            return LL1Productions.getInstance().firsts;
        }else if("symbol".equals(key)){
            return symbolFirstParser.firstVT;
        }
        return ProductionManager.getProductionManager().firstFollowSelectSetBuilder.getFirstSympol();
    }
    @PostMapping("/setSymbolGrammer")
    public void setSymbolGrammer(HttpServletRequest request){
        ArrayList<LProduction> symbolGrammer=new ArrayList<>();
        String[] grammers=request.getParameterMap().get("code")[0].split("\n");
        for(int i=0;i<grammers.length;i++){

            String grammer=grammers[i];
            String[] ss=grammer.split("-->");
            String[] ss1=ss[1].trim().split(" ");
            for(int j=0;j<ss1.length;j++) ss1[j]=ss1[j].trim();
            if(i==0){
                symbolGrammer.add(new LProduction("START",new String[]{"#",ss[0].trim(),"#"}));
            }
            symbolGrammer.add(new LProduction(ss[0].trim(),ss1));
        }
        symbolFirstParser=new SymbolFirstParser(symbolGrammer);
    }
    @GetMapping("/followSympol")
    public Object followSympol(String key){
        if("ll1".equals(key)){
            return LL1Productions.getInstance().follows;
        }else if("symbol".equals(key)){
            return symbolFirstParser.lastVT;
        }
        return ProductionManager.getProductionManager().firstFollowSelectSetBuilder.getFollowSympol();
    }
    @GetMapping("/selectSympol")
    public Object selectSympol(String key){
        if("ll1".equals(key)){
            return LL1Productions.getInstance().predict;
        }else if("symbol".equals(key)){
            return symbolFirstParser.selectTable;
        }
        return ProductionManager.getProductionManager().firstFollowSelectSetBuilder.getSelectSympol();
    }
    @PostMapping("/symbolAnalysis")
    public Object symbolAnalysis(HttpServletRequest request){
        List<Word> words=new ArrayList<>();
        String[] ss=request.getParameterMap().get("code")[0].trim().split(" ");
        for(int i=0;i<ss.length;i++){
            words.add(new Word(ss[i].trim()));
        }
        words.add(new Word("#"));
        return symbolFirstParser.analy_input_string(words);
    }
    @RequestMapping("/nfaDfaMacro")
    public boolean nfadfa(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        macro=new HashMap<>();
        for(String key:parameterMap.keySet()){
            macro.put(key,parameterMap.get(key)[0]);
        }
        ThompsonConstruction construction = new ThompsonConstruction();
        construction.runMacroExample(macro);
        return true;
    }

    @RequestMapping("/nfaDfaExpr")
    public HashMap<String, List<HashMap<String, Object>>> nfaDfaExpr(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] arr=parameterMap.get("expr");
        regularExpr=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            regularExpr.add(arr[i]);
        }
        ThompsonConstruction construction = new ThompsonConstruction();
        construction.runMacroExample(macro);
        construction.runMacroExpandExample(regularExpr);
        return construction.runNfaMachineConstructorExample();
    }
    @RequestMapping("/nfaDfaToDfa")
    public HashMap<String, List<HashMap<String, Object>>> nfaDfaToDfa(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] arr=parameterMap.get("expr");
        regularExpr=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            regularExpr.add(arr[i]);
        }
        ThompsonConstruction construction = new ThompsonConstruction();
        construction.runMacroExample(macro);
        construction.runMacroExpandExample(regularExpr);
        construction.runNfaMachineConstructorExample();
        construction.runNfaIntepretorExample(strings);
        return construction.runDfaConstructorExample();
    }
    @RequestMapping("/nfaDfaMinDfa")
    public HashMap<String, List<HashMap<String, Object>>> nfaDfaMinDfa(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] arr=parameterMap.get("expr");
        regularExpr=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            regularExpr.add(arr[i]);
        }
        ThompsonConstruction construction = new ThompsonConstruction();
        construction.runMacroExample(macro);
        construction.runMacroExpandExample(regularExpr);
        construction.runNfaMachineConstructorExample();
        construction.runNfaIntepretorExample(strings);
        construction.runDfaConstructorExample();
        return construction.runMinimizeDFAExample();
    }

    @RequestMapping("/firstFollowSelect")
    public HashMap<String, Symbols> firstFollowSelect(){
        return null;
    }
}
