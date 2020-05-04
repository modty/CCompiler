package top.modty.ccompiler.controller;

import org.springframework.web.bind.annotation.*;
import top.modty.ccompiler.BottomUpParser;
import top.modty.ccompiler.commons.constants.Grammar;
import top.modty.ccompiler.lex.Lexer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    @PostMapping("/setCode")
    public void initial(HttpServletRequest request){
        String code=request.getParameterMap().get("code")[0];
        bottomUpParser.setLexer(new Lexer(code));
    }
    @GetMapping("/lexMap")
    public Object lexMap(){
        Map<String,Object> response=new HashMap<>();
        response.put("right",bottomUpParser.getLexer().getRecognizedMap());
        System.out.println(response);
        return response;
    }
    @GetMapping("grammer")
    public Object grammer(String key){
        return grammar.grammers.get(key);
    }
}
