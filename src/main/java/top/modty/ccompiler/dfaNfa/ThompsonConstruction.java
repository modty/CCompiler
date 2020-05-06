package top.modty.ccompiler.dfaNfa;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThompsonConstruction {
    private MacroHandler macroHandler = null;
    RegularExpressionHandler regularExpr = null;
    private Lexer lexer = null;
    
    private NfaMachineConstructor nfaMachineConstructor = null;
    private NfaPrinter nfaPrinter = new NfaPrinter();
    
    NfaPair pair = new NfaPair();
    
    NfaIntepretor nfaIntepretor = null;
    
    DfaConstructor dfaConstructor = null;
    
    MinimizeDFA    miniDfa = null;
    
    public void runMacroExample(HashMap<String,String> macro) throws Exception {
    	System.out.println("Please enter macro definition");
    	macroHandler = new MacroHandler(macro);
    	macroHandler.printMacs();

    }
    
    public void runMacroExpandExample(ArrayList<String> list) throws Exception {
    	System.out.println("Enter regular expression");
        regularExpr = new RegularExpressionHandler(list, macroHandler);
    	System.out.println("regular expression after expanded: ");
    	for (int i = 0; i < regularExpr.getRegularExpressionCount(); i++) {
    		System.out.println(regularExpr.getRegularExpression(i));	
    	}
    	
    }

    public void runLexerExample() {
       lexer = new Lexer(regularExpr);
       int exprCount = 0;
       System.out.println("当前正则解析的正则表达式: " + regularExpr.getRegularExpression(exprCount));
       lexer.advance();
       
       while (lexer.MatchToken(Lexer.Token.END_OF_INPUT) == false) {
    	   
    	   if (lexer.MatchToken(Lexer.Token.EOS) == true) {
    		   System.out.println("解析下一个正则表达式");
    		   exprCount++;
    		   System.out.println("当前正则解析的正则表达式: " + regularExpr.getRegularExpression(exprCount));
    		   lexer.advance();
    	   }
    	   else {
    		   printLexResult();
    	   }
    	   
       } 
    }
    
    private void printLexResult() {
    	while (lexer.MatchToken(Lexer.Token.EOS) == false) {
    		System.out.println("当前识别字符是: " + (char)lexer.getLexeme());
    		
    		if (lexer.MatchToken(Lexer.Token.L) != true) {
    			System.out.println("当前字符具有特殊含义");
    			
    			printMetaCharMeaning(lexer);
    		}
    		else {
    			System.out.println("当前字符是普通字符常量");
    		}
    		
    		lexer.advance();
    	}
    }
    
    private void printMetaCharMeaning(Lexer lexer) {
    	String s = "";
    	if (lexer.MatchToken(Lexer.Token.ANY)) {
    		s = "当前字符是点通配符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.AT_BOL)) {
    		s = "当前字符是开头匹配符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.AT_EOL)) {
    		s = "当前字符是末尾匹配符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CCL_END)) {
    		s = "当前字符是字符集类结尾括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CCL_START)) {
    		s = "当前字符是字符集类的开始括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CLOSE_CURLY)) {
    		s = "当前字符是结尾大括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CLOSE_PAREN)) {
    		s = "当前字符是结尾圆括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.DASH)) {
    		s = "当前字符是横杆";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OPEN_CURLY)) {
    		s = "当前字符是起始大括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OPEN_PAREN)) {
    		s = "当前字符是起始圆括号";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OPTIONAL)) {
    		s = "当前字符是单字符匹配符?";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.OR)) {
    		s = "当前字符是或操作符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.PLUS_CLOSE)) {
    		s = "当前字符是正闭包操作符";
    	}
    	
    	if (lexer.MatchToken(Lexer.Token.CLOSURE)) {
    		s = "当前字符是闭包操作符";
    	}
    	
    	System.out.println(s);
    }
    
    public void runNfaIntepretorExample(String input) {
    	nfaIntepretor = new NfaIntepretor(pair.startNode);
    	nfaIntepretor.intepretNfa(input);
    }
 
   public void runDfaConstructorExample() {
	   dfaConstructor = new DfaConstructor(pair, nfaIntepretor);
	   dfaConstructor.convertNfaToDfa();
	   dfaConstructor.printDFA();
   }
    
    public void runNfaMachineConstructorExample() throws Exception {
    	lexer = new Lexer(regularExpr);
    	nfaMachineConstructor = new NfaMachineConstructor(lexer);

    	nfaMachineConstructor.constructNfaForSingleCharacter(pair);
    	nfaMachineConstructor.constructNfaForDot(pair);
    	nfaMachineConstructor.constructNfaForCharacterSetWithoutNegative(pair);
    	nfaMachineConstructor.constructNfaForCharacterSet(pair);
    	nfaMachineConstructor.constructStarClosure(pair);
    	nfaMachineConstructor.constructPlusClosure(pair);
    	nfaMachineConstructor.constructOptionsClosure(pair);
    	nfaMachineConstructor.cat_expr(pair);
    	nfaMachineConstructor.expr(pair);
    	nfaPrinter.printNfa(pair.startNode);
    }
    
    public void runMinimizeDFAExample() {
    	miniDfa = new MinimizeDFA(dfaConstructor);
    	miniDfa.minimize();
    }
    
   public HashMap<String, List<HashMap<String, Object>>> getNFAResponse(){
	   HashMap<Integer, HashMap<String,Object>> nfaTable=nfaPrinter.nfaTable;
	   HashMap<String, List<HashMap<String, Object>>> response=new HashMap<>();
	   response.put("state",new ArrayList<>());
	   response.put("edg",new ArrayList<>());
	   for(Integer key:nfaTable.keySet()){
	   	HashMap<String,Object> temp=new HashMap<>();
	   	temp.put("id",key);
	   	temp.put("label",key);
	   	temp.put("shape","circle");
	   	temp.put("class","type-suss");
	   	response.get("state").add(temp);
	   	HashMap<String,Object> data=nfaTable.get(key);
	   	List<Integer> next=(List<Integer>)data.get("next");
	   	String condition= String.valueOf(data.get("conditions"));
	   for(int i=0;i<next.size();i++){
		temp=new HashMap<>();
		temp.put("start",key);
		temp.put("end",next.get(i));
		temp.put("label",condition);
		response.get("edg").add(temp);
		}
	   }
	   return response;
   }
    
    
    public static void main(String[] args) throws Exception {
    	HashMap<String,String> macro=new HashMap<>();
    	macro.put("D","[0-9]");
    	macro.put("A","[a-zA-Z]");
    	ArrayList<String> regularExpr=new ArrayList<>();
    	regularExpr.add("{D}[+]{D}|{D}\\-{D}|void");
    	String inputString="void";
		ThompsonConstruction construction = new ThompsonConstruction();
		construction.runMacroExample(macro);
		construction.runMacroExpandExample(regularExpr);
		construction.runLexerExample();
		construction.runNfaMachineConstructorExample();
		construction.runNfaIntepretorExample(inputString);
		construction.runDfaConstructorExample();
		construction.runMinimizeDFAExample();
    }
}
