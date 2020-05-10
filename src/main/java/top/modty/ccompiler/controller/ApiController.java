package top.modty.ccompiler.controller;

import org.springframework.web.bind.annotation.*;
import top.modty.ccompiler.BottomUpParser;
import top.modty.ccompiler.commons.constants.Grammar;
import top.modty.ccompiler.dfaNfa.ThompsonConstruction;
import top.modty.ccompiler.lex.Lexer;
import top.modty.ccompiler.semantic.LRStateTableParser;

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
    BottomUpParser bottomUpParser=new BottomUpParser();
    Grammar grammar=new Grammar();
    String code;
    HashMap<String,String> macro;
    ArrayList<String> regularExpr;
    String strings;
    @PostMapping("/setCode")
    public void initial(HttpServletRequest request){
        this.code=request.getParameterMap().get("code")[0];
        bottomUpParser.setLexer(new Lexer(code));
    }
    @GetMapping("/lexMap")
    public Object lexMap(){
        Map<String,Object> response=new HashMap<>();
        response.put("right",bottomUpParser.getLexer().getRecognizedMap());
        System.out.println(response);
        return response;
    }
    @GetMapping("/grammer")
    public List<String> grammer(String key){
        return grammar.grammers.get(key);
    }
    @GetMapping("/tree")
    public HashMap<String, List<HashMap<String, Object>>> tree(){
        code="void main() {\n" +
                " int a;\n" +
                " int i;\n" +
                " if (i < 1){\n" +
                "   a = 1;\n" +
                " }\n" +
                " else if (i < 2){\n" +
                "   a = 2;\n" +
                " }\n" +
                " else {\n" +
                "   a = 3;\n" +
                "\n" +
                " }\n" +
                "}";
        LRStateTableParser parser=null;
        HashMap<String, List<HashMap<String, Object>>> response=null;
        try{
            Lexer lexer=new Lexer(code);
            lexer.getRecognizedMap().forEach(System.out::println);
            parser = new LRStateTableParser(lexer);
            System.out.println(code);
            response= parser.parse();
//            HashMap<String, List<HashMap<String, Object>>> s=parser.parse();
//            response=new HashMap<>();
//            response.put("width",parser.times*10);
//            response.put("height",parser.times*6);
//            response.put("data",s);
        }catch (Exception e){
         e.printStackTrace();
        }finally {
            parser.clear();
            return response;
        }
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
}
