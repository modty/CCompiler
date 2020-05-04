package top.modty.ccompiler.lex;

import top.modty.ccompiler.error.ErrorType;
import top.modty.ccompiler.error.Errors;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @author 点木
 * @date 2020-05-03 17:48
 * @mes 词法解析器
 */
public class Lexer {
    public Errors errors=Errors.getInstance();
    public int lookAhead = CTokenType.UNKNOWN_TOKEN.ordinal();
    public int position=0;
    public String yytext = "";
    public int yyleng = 0;

    private String input_buffer = "";
    private String current = "";
    private HashMap<String, Integer> keywordMap = new HashMap<String, Integer>();

    public Lexer(String current) {
        this.current=current;
        initKeyWordMap();
    }

    /**
     * @author 点木
     * @date 2020/5/3
     * @return null
     * @params null
     * @mes 初始化关键字字典
    */
    private void initKeyWordMap() {
        keywordMap.put("auto", CTokenType.CLASS.ordinal());
        keywordMap.put("static", CTokenType.CLASS.ordinal());
        keywordMap.put("register", CTokenType.CLASS.ordinal());
        keywordMap.put("char", CTokenType.TYPE.ordinal());
        keywordMap.put("float", CTokenType.TYPE.ordinal());
        keywordMap.put("double", CTokenType.TYPE.ordinal());
        keywordMap.put("int", CTokenType.TYPE.ordinal());
        keywordMap.put("enum", CTokenType.ENUM.ordinal());
        keywordMap.put("long", CTokenType.TYPE.ordinal());
        keywordMap.put("short", CTokenType.TYPE.ordinal());
        keywordMap.put("void", CTokenType.TYPE.ordinal());
        keywordMap.put("struct", CTokenType.STRUCT.ordinal());
        keywordMap.put("return", CTokenType.RETURN.ordinal());
        keywordMap.put("if", CTokenType.IF.ordinal());
        keywordMap.put("else", CTokenType.ELSE.ordinal());
        keywordMap.put("switch", CTokenType.SWITCH.ordinal());
        keywordMap.put("case", CTokenType.CASE.ordinal());
        keywordMap.put("default", CTokenType.DEFAULT.ordinal());
        keywordMap.put("break", CTokenType.BREAK.ordinal());
        keywordMap.put("for", CTokenType.FOR.ordinal());
        keywordMap.put("while", CTokenType.WHILE.ordinal());
        keywordMap.put("do", CTokenType.DO.ordinal());
        keywordMap.put("goto", CTokenType.GOTO.ordinal());
    }

    /**
     * @author 点木
     * @date 2020/5/3
     * @return char:字符
     * @params boolean
     * @mes 判断输入字符是否为字母或者数字
    */
    private boolean isAlnum(char c) {
        if (Character.isAlphabetic(c) == true ||
                Character.isDigit(c) == true) {
            return true;
        }

        return false;
    }


    /**
     * @author 点木
     * @date 2020/5/3
     * @return CTokenType枚举默认值
     * @params null
     * @mes 判断下一个可以确认的类型
    */
    private int lex() {

        while (true) {
            /*            从控制台输入
            while (current == "") {
                Scanner s = new Scanner(System.in);
                while (true) {
                    String line = s.nextLine();
                    if (line.equals("end")) {
                        break;
                    }
                    input_buffer += line;
                }
                s.close();

                if (input_buffer.length() == 0) {
                    current = "";
                    return CTokenType.SEMI.ordinal();
                }

                current = input_buffer;
                current.trim();
            }
*/
            if (current.isEmpty()) {
                return CTokenType.SEMI.ordinal();
            }
            for (int i = 0; i < current.length(); i++) {

                yyleng = 0;
                yytext = current.substring(i, i + 1);
                switch (current.charAt(i)) {
                    case ';': current = current.substring(1); position+=1;return CTokenType.SEMI.ordinal();
                    case '+':

                        if (current.charAt(i + 1) == '+') {
                            position+=2;
                            current= current.substring(2);
                            return CTokenType.INCOP.ordinal();
                        }
                        position+=1;
                        current = current.substring(1);
                        return CTokenType.PLUS.ordinal();

                    case '-':
                        if (current.charAt(i+1) == '>') {
                            current = current.substring(2);
                            position+=2;
                            return CTokenType.STRUCTOP.ordinal();
                        } else if (current.charAt(i+1) == '-') {
                            current = current.substring(2);
                            position+=2;
                            return CTokenType.DECOP.ordinal();
                        }

                        current = current.substring(1);
                        position+=1;
                        return CTokenType.MINUS.ordinal();

                    case '[': current = current.substring(1);position+=1;return CTokenType.LB.ordinal();
                    case ']': current = current.substring(1);position+=1;return CTokenType.RB.ordinal();
                    case '*': current = current.substring(1);position+=1;return CTokenType.STAR.ordinal();
                    case '(': current = current.substring(1);position+=1;return CTokenType.LP.ordinal();
                    case ')': current = current.substring(1);position+=1;return CTokenType.RP.ordinal();
                    case ',': current = current.substring(1);position+=1; return CTokenType.COMMA.ordinal();
                    case '{': current = current.substring(1);position+=1; return CTokenType.LC.ordinal();
                    case '}': current = current.substring(1);position+=1; return CTokenType.RC.ordinal();
                    case '=':

                        if (current.charAt(i + 1) == '=') {
                            current = current.substring(2);
                            position+=2;
                            return CTokenType.RELOP.ordinal();
                        }

                        current = current.substring(1); position+=1;return CTokenType.EQUAL.ordinal();

                    case '?': current = current.substring(1); position+=1;return CTokenType.QUEST.ordinal();
                    case ':': current = current.substring(1); position+=1;return CTokenType.COLON.ordinal();
                    case '&': current = current.substring(1); position+=1;return CTokenType.AND.ordinal();
                    case '|': current = current.substring(1); position+=1;return CTokenType.OR.ordinal();
                    case '^': current = current.substring(1); position+=1;return CTokenType.XOR.ordinal();

                    case '/':
                    case '%':
                        current = current.substring(1); position+=1;return CTokenType.DIVOP.ordinal();
                    case '>':
                    case '<':
                        if (current.charAt(i + 1) == '=') {
                            yytext = current.substring(0, 2);
                            current = current.substring(2);
                            position+=2;
                        } else if ((current.charAt(i) == '<' && current.charAt(i+1) == '<')
                                || (current.charAt(i) == '>' && current.charAt(i+1) == '>')) {
                            yytext = current.substring(0, 2);
                            current = current.substring(2);
                            position+=2;
                            return CTokenType.SHIFTOP.ordinal();
                        }
                        else {
                            current = current.substring(1);
                            position+=1;
                        }

                        return CTokenType.RELOP.ordinal();

                    case '\n':
                    case '\t':
                    case ' ':
                        current = current.substring(1);
                        position+=1;
                        return CTokenType.WHITE_SPACE.ordinal();

                    case '.':
                        current = current.substring(1);
                        position+=1;
                        return CTokenType.STRUCTOP.ordinal();

                    case '"':
                        i++;
                        int begin = i;
                        while (i < current.length()) {
                            if (current.charAt(i) != '"') {
                                yyleng++;
                            }else {
                                break;
                            }
                            i++;
                        }
                        if (i >= current.length()) {
                            errors.addError(ErrorType.ERROR_LEX,current.substring(begin-1, yyleng+1),"缺少字符串结束符。",position);
                            return CTokenType.UNKNOWN_TOKEN.ordinal();
                        }

                        yytext = current.substring(begin, yyleng+1);
                        current = current.substring(yyleng + 2);
                        position+=yyleng + 2;
                        return CTokenType.STRING.ordinal();


                    default:
                        if (isAlnum(current.charAt(i)) == false) {
                            current = current.substring(1);
                            errors.addError(ErrorType.ERROR_LEX, current.substring(position, position+1),"不支持字符。",position);
                            position+=1;
                            return CTokenType.UNKNOWN_TOKEN.ordinal();
                        }
                        else {

                            while (i < current.length() && isAlnum(current.charAt(i)) ) {
                                i++;
                                yyleng++;
                            }
                            yytext = current.substring(0, yyleng);
                            current = current.substring(yyleng);
                            position+=yyleng;
                            return translateStringToToken();
                        }

                }
            }

        }
    }

    /**
     * @author 点木
     * @date 2020/5/3
     * @return CTokenType枚举默认值
     * @params null
     * @mes 判断单个字符是不是确认的类型
    */
    private int translateStringToToken() {
        int type = id_keyword_or_number();
        if (type != CTokenType.UNKNOWN_TOKEN.ordinal()) {
            return type;
        }
        return CTokenType.NAME.ordinal();
    }

    private int id_keyword_or_number() {
        int type = CTokenType.UNKNOWN_TOKEN.ordinal();
        if (Character.isAlphabetic(yytext.charAt(0))) {
            type = isKeyWord(yytext);
        } else {
            if (isNum()) {
                type = CTokenType.NUMBER.ordinal();
            }
        }

        return type;
    }

    private boolean isNum() {
        int pos = 0;
        if (yytext.charAt(0) == '-' || yytext.charAt(0) == '+') {
            pos++;
        }

        for (; pos < yytext.length(); pos++) {
            if (Character.isDigit(yytext.charAt(pos)) != true) {
                break;
            }
        }

        return pos == yytext.length();
    }

    private int isKeyWord(String str) {

        if (keywordMap.containsKey(str)) {
            return keywordMap.get(str);
        }

        return CTokenType.UNKNOWN_TOKEN.ordinal();
    }
    /**
     * @author 点木
     * @date 2020/5/3
     * @return CTokenType枚举默认值
     * @params CTokenType
     * @mes 供外部调用，判断下一个类型是否是某个类型
     */
    public boolean match(int token) {
        if (lookAhead == -1) {
            lookAhead = lex();
        }
        return token == lookAhead;
    }

    /**
     * @author 点木
     * @date 2020/5/3
     * @return CTokenType枚举默认值
     * @params null
     * @mes 供外部调用，返回下一个类型
     */
    public void advance() {
        lookAhead = lex();
        while (lookAhead == CTokenType.WHITE_SPACE.ordinal()) {
            lookAhead = lex();
        }
    }
    public static void main(String[] args) {
        // 冒泡排序代码
        String code="int @a=\"123";
        Lexer lexer=new Lexer(code);
        // 词法分析器不用来解析边界
        for(int i=0;i<5;i++){
            lexer.advance();
            System.out.println(lexer.yytext+"--->"+CTokenType.values()[lexer.lookAhead].toString());
        }
        lexer.errors.getErrorsMap().get(ErrorType.getSymbolStr(ErrorType.ERROR_LEX.ordinal())).forEach(System.err::println);
    }
}

