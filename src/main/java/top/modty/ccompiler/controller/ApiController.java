package top.modty.ccompiler.controller;

import org.springframework.web.bind.annotation.*;
import top.modty.ccompiler.BottomUpParser;
import top.modty.ccompiler.commons.constants.Grammar;
import top.modty.ccompiler.lex.Lexer;
import top.modty.ccompiler.semantic.Intepretor;
import top.modty.ccompiler.semantic.LRStateTableParser;
import top.modty.ccompiler.semantic.ProgramGenerator;
import top.modty.ccompiler.semantic.code.CodeTreeBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> tree(){
        LRStateTableParser parser=null;
        Map<String,Object> response=null;
        try{
            Lexer lexer=new Lexer(code);
            lexer.getRecognizedMap().forEach(System.out::println);
            parser = new LRStateTableParser(lexer);
            System.out.println(code);
            List<Object> s=parser.parse();
            response=new HashMap<>();
            response.put("width",parser.times*10);
            response.put("height",parser.times*6);
            response.put("data",s);
        }catch (Exception e){
         e.printStackTrace();
        }finally {
            parser.clear();
            return response;
        }
    }
}
