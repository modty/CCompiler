package top.modty.ccompiler.commons.constants;

import java.util.*;

/**
 * @author 点木
 * @date 2020@05@04 21:20
 * @mes
 */
public class Grammar {
    public HashMap<String, List<String>> grammers;
    public Grammar(){
        grammers=new HashMap<>();
        grammers.put("变量定义",initVariableDecalationProductions());
        grammers.put("函数声明",initFunctionProductions());
        grammers.put("结构体",initStructureProductions());
        grammers.put("枚举",initEmunProductions());
        grammers.put("函数定义",initFunctionDefinition());
        grammers.put("IF@ELSE",initFunctionDefinitionWithIfElse());
        grammers.put("SWITCH@CASE",initFunctionDefinitionWithSwitchCase());
        grammers.put("循环",initFunctionDefinitionWithLoop());
        grammers.put("算术表达式",initComputingOperation());
        grammers.put("其他",initRemaindingProduction());
    }

    private List<String> initVariableDecalationProductions() {
        List<String> result=new ArrayList<>();
        //PROGRAM @> EXT_DEF_LIST
        result.add("PROGRAM@EXT_DEF_LIST");
        //EXT_DEF_LIST @> EXT_DEF_LIST EXT_DEF
        result.add("EXT_DEF_LIST@EXT_DEF_LIST@EXT_DEF");
        //EXT_DEF @> OPT_SPECIFIERS EXT_DECL_LIST  SEMI
        result.add("EXT_DEF@OPT_SPECIFIERS@EXT_DECL_LIST@SEMI");
        //EXT_DEF @> OPT_SPECIFIERS  SEMI
        result.add("EXT_DEF@OPT_SPECIFIERS@SEMI");
        //EXT_DECL_LIST @>   EXT_DECL
        result.add("EXT_DECL_LIST@EXT_DECL");
        ///EXT_DECL_LIST @>EXT_DECL_LIST COMMA EXT_DECL
        result.add("EXT_DECL_LIST@EXT_DECL_LIST@COMMA@EXT_DECL");
        //EXT_DECL @> VAR_DECL
        result.add("EXT_DECL@VAR_DECL");
        //OPT_SPECIFIERS @> SPECIFIERS
        result.add("OPT_SPECIFIERS@SPECIFIERS");
        //SPECIFIERS @> TYPE_OR_CLASS
        result.add("SPECIFIERS@TYPE_OR_CLASS");
        //SPECIFIERS @> SPECIFIERS TYPE_OR_CLASS
        result.add("SPECIFIERS@SPECIFIERS@TYPE_OR_CLASS");
        //TYPE_OR_CLASS @> TYPE_SPECIFIER
        result.add("TYPE_OR_CLASS@TYPE_SPECIFIER");
        //TYPE_SPECIFIER @>  TYPE
        result.add("TYPE_SPECIFIER@TYPE");
        //NEW_NAME @> NAME
        result.add("NEW_NAME@NAME");
        //VAR_DECL @>  NEW_NAME(13)
        result.add("VAR_DECL@NEW_NAME");
        ///VAR_DECL @>START VAR_DECL
        result.add("VAR_DECL@START@VAR_DECL");
        return result;
    }

    private List<String> initFunctionProductions() {
        List<String> result=new ArrayList<>();
        //EXT_DEF @> OPT_SPECIFIERS FUNCT_DECL SEMI(15)
        result.add("EXT_DEF@OPT_SPECIFIERS@FUNCT_DECL@SEMI");
        //FUNCT_DECL @> NEW_NAME LP VAR_LIST RP(16)
        result.add("FUNCT_DECL@NEW_NAME@LP@VAR_LIST@RP");
        //FUNCT_DECL @>  NEW_NAME LP RP(17)
        result.add("FUNCT_DECL@NEW_NAME@LP@RP");
        //VAR_LIST @>  PARAM_DECLARATION
        result.add("VAR_LIST@PARAM_DECLARATION");
        //VAR_LIST @> VAR_LIST COMMA PARAM_DECLARATION
        result.add("VAR_LIST@VAR_LIST@COMMA@PARAM_DECLARATION");
        //PARAM_DECLARATION @> TYPE_NT VAR_DECL
        result.add("PARAM_DECLARATION@TYPE_NT@VAR_DECL");
        //TYPE_NT @> TYPE_SPECIFIER
        result.add("TYPE_NT@TYPE_SPECIFIER");
        //TYPE_NT @> TYPE TYPE_SPECIFIER
        result.add("TYPE_NT@TYPE@TYPE_SPECIFIER");
        return result;

    }

    private List<String> initStructureProductions() {
        List<String> result=new ArrayList<>();
        //TYPE_SPECIFIER @> STRUCT_SPECIFIER  (23)
        result.add("TYPE_SPECIFIER@STRUCT_SPECIFIER");
        //STTUCT_SPECIFIER @> STRUCT OPT_TAG LC DEF_LIST RC (24)
        result.add("STTUCT_SPECIFIER@STRUCT@OPT_TAG@LC@DEF_LIST@RC");
        //STRUCT_SPECIFIER @> STRUCT TAG (25)
        result.add("STRUCT_SPECIFIER@STRUCT@TAG");
        //OPT_TAG @> TAG (26)
        result.add("OPT_TAG@TAG");
        //TAG @> NAME (27)
        result.add("TAG@NAME");
        //DEF_LIST @>  DEF (28)
        result.add("DEF_LIST@DEF");
        //DEF_LIST @> DEF_LIST DEF (29)
        result.add("DEF_LIST@DEF_LIST@DEF");
        //DEF @> SPECIFIERS DECL_LIST SEMI (30)
        result.add("DEF@SPECIFIERS@DECL_LIST@SEMI");
        //DEF @> SPECIFIERS SEMI (31)
        result.add("DEF@SPECIFIERS@SEMI");
        //DECL_LIST @> DECL (32)
        result.add("DECL_LIST@DECL");
        //DECL_LIST @> DECL_LIST COMMA DECL (33)
        result.add("DECL_LIST@DECL_LIST@COMMA@DECL");
        //DECL @> VAR_DECL (34)
        result.add("DECL@VAR_DECL");
        //VAR_DECL @> NEW_NAME (35)
        result.add("VAR_DECL@NEW_NAME");
        //VAR_DECL @> VAR_DECL LP RP (36)
        result.add("VAR_DECL@VAR_DECL@LP@RP");
        //VAR_DECL @> VAR_DECL LP VAR_LIS RP (37)
        result.add("VAR_DECL@VAR_DECL@LP@VAR_LIS@RP");
        //VAR_DECL @> LP VAR_DECL RP (38)
        result.add("VAR_DECL@LP@VAR_DECL@RP");
        //VAR_DECL @> STAR VAR_DECL (39)
        result.add("VAR_DECL@STAR@VAR_DECL");
        return result;
    }

    private List<String> initEmunProductions() {
        List<String> result=new ArrayList<>();
        //ENUM_SPECIFIER @> ENUM_NT NAME_NT OPT_ENUM_LIST(40)
        result.add("ENUM_SPECIFIER@ENUM_NT@NAME_NT@OPT_ENUM_LIST");
        //ENUM_NT @> ENUM(41)
        result.add("ENUM_NT@ENUM");
        //ENUMERATOR_LIST @> ENUMERATOR(42)
        result.add("ENUMERATOR_LIST@ENUMERATOR");
        //EMERATOR_LIST @> ENUMERATOR_LIST COMMA ENUMERATOR(43)
        result.add("EMERATOR_LIST@ENUMERATOR_LIST@COMMA@ENUMERATOR");
        //ENUMERATOR @> NAME_NT(44)
        result.add("ENUMERATOR@NAME_NT");
        //NAME_NT @> NAME(45)
        result.add("NAME_NT@NAME");
        //ENUMERATOR @> NAME_NT EQUAL CONST_EXPR(46)
        result.add("ENUMERATOR@NAME_NT@EQUAL@CONST_EXPR");
        //CONST_EXPR @> NUMBER (47)
        result.add("CONST_EXPR@NUMBER");
        //OPT_ENUM_LIST @> LC ENUMERATOR_LIST RC (48)
        result.add("OPT_ENUM_LIST@LC@ENUMERATOR_LIST@RC");
        //TYPE_SPECIFIER @> ENUM_SPECIFIER (49)
        result.add("TYPE_SPECIFIER@ENUM_SPECIFIER");
        return result;
    }

    private List<String> initFunctionDefinition() {
        List<String> result=new ArrayList<>();
        //EXT_DEF @> OPT_SPECIFIERS FUNCT_DECL COMPOUND_STMT(50)
        result.add("EXT_DEF@OPT_SPECIFIERS@FUNCT_DECL@COMPOUND_STMT");
        //COMPOUND_STMT@> LC LOCAL_DEFS STMT_LIST RC(51)
        result.add("COMPOUND_STMT@LC@LOCAL_DEFS@STMT_LIST@RC");
        //LOCAL_DEFS @> DEF_LIST(52)
        result.add("LOCAL_DEFS@DEF_LIST");
        //EXPR @> NO_COMMA_EXPR(53)
        result.add("EXPR@NO_COMMA_EXPR");
        //NO_COMMA_EXPR @> NO_COMMA_EXPR EQUAL NO_COMMA_EXPR(54)
        result.add("NO_COMMA_EXPR@NO_COMMA_EXPR@EQUAL@NO_COMMA_EXPR");
        //NO_COMMA_EXPR @> NO_COMMA_EXPR QUEST  NO_COMMA_EXPR COLON NO_COMMA_EXPR(55)
        result.add("NO_COMMA_EXPR@NO_COMMA_EXPR@QUEST@NO_COMMA_EXPR@COLON@NO_COMMA_EXPR");
        //NO_COMMA_EXPR @> BINARY(56)
        result.add("NO_COMMA_EXPR@BINARY");
        //BINARY @> UNARY (57)
        result.add("BINARY@UNARY");
        //UNARY @> NUMBER (58)
        result.add("UNARY@NUMBER");
        //UNARY @> NAME (59)
        result.add("UNARY@NAME");
        //UNARY @> STRING(60)
        result.add("UNARY@STRING");
        //STMT_LIST @> STMT_LIST STATEMENT(61)
        result.add("STMT_LIST@STMT_LIST@STATEMENT");
        //STMT_LIST @>  STATEMENT(62)
        result.add("STMT_LIST@STATEMENT");
        //STATEMENT @> EXPR SEMI(63)
        result.add("STATEMENT@EXPR@SEMI");
        //STATEMENT @> RETURN EXPR SEMI (64)
        result.add("STATEMENT@RETURN@EXPR@SEMI");
        //BINARY @> BINARY RELOP BINARY (65)
        result.add("BINARY@BINARY@RELOP@BINARY");
        //BINARY @> BINARY EQUOP BINARY (66)
        result.add("BINARY@BINARY@EQUOP@BINARY");
        //BINARY @> BINARY START BINARY (67)
        result.add("BINARY@BINARY@START@BINARY");
        //STATEMENT @> LOCAL_DEFS(68)
        result.add("STATEMENT@LOCAL_DEFS");
        //COMPOUND_STMT @> LC RC(69)
        result.add("COMPOUND_STMT@LC@RC");
        //COMPOUNT_STMT @> LC STMT_LIST RC(70)
        result.add("COMPOUNT_STMT@LC@STMT_LIST@RC");
        return result;
    }

    private List<String> initFunctionDefinitionWithIfElse() {
        List<String> result=new ArrayList<>();
        //STATEMENT @> COMPOUND_STMT (71)
        result.add("STATEMENT@COMPOUND_STMT");
        //IF_STATEMENT @> IF LP TEST RP STATEMENT (72)
        result.add("IF_STATEMENT@IF@LP@TEST@RP@STATEMENT");
        //IF_ELSE_STATEMENT @> IF_STATEMENT 73
        result.add("IF_ELSE_STATEMENT@IF_STATEMENT");
        //IF_ELSE_STATEMENT @>IF_ELSE_STATEMENT ELSE STATEMENT 74
        result.add("IF_ELSE_STATEMENT@IF_ELSE_STATEMENT@ELSE@STATEMENT");
        //STATEMENT @> IF_ELSE_STATEMENT 75
        result.add("STATEMENT@IF_ELSE_STATEMENT");
        //TEST @> EXPR 76
        result.add("TEST@EXPR");
        //DECL @> VAR_DECL EQUAL INITIALIZER 77
        result.add("DECL@VAR_DECL@EQUAL@INITIALIZER");
        //INITIALIZER @> EXPR 78
        result.add("INITIALIZER@EXPR");
        return result;
    }

    private List<String> initFunctionDefinitionWithSwitchCase() {
        List<String> result=new ArrayList<>();
        //STATEMENT @> SWITCH LP EXPR RP COMPOUND_STATEMENT (79)
        result.add("STATEMENT@SWITCH@LP@EXPR@RP@COMPOUND_STATEMENT");
        //STATEMENT @> CASE CONST_EXPR COLON(80)
        result.add("STATEMENT@CASE@LP@CONST_EXPR@COLON");
        //STATEMENT @> DEFAULT COLON (81)
        result.add("STATEMENT@DEFAULT@COLON");
        //STATEMENT @> BREAK SEMI; (82)
        result.add("STATEMENT@BREAK@SEMI");
        return result;
    }

    private List<String> initFunctionDefinitionWithLoop() {
        List<String> result=new ArrayList<>();
        //STATEMENT @> WHILE LP TEST RP STATEMENT (83)
        result.add("STATEMENT@WHILE@LP@TEST@RP@STATEMENT");
        //STATEMENT @> FOR LP OPT_EXPR  TEST SEMI END_OPT_EXPR RP STATEMENT(84)
        result.add("STATEMENT@FOR@LP@OPT_EXPR@TEST@SEMI@END_OPT_EXPR@RP@STATEMENT");
        //OPT_EXPR @> EXPR SEMI(85)
        result.add("OPT_EXPR@EXPR@SEMI");
        //OPT_EXPR @> SEMI(86)
        result.add("OPT_EXPR@SEMI");
        //END_OPT_EXPR @> EXPR(87)
        result.add("END_OPT_EXPR@EXPR");
        //STATEMENT @> DO STATEMENT WHILE LP TEST RP SEMI(88)
        result.add("STATEMENT@DO@STATEMENT@WHILE@LP@TEST@RP@RP@SEMI");
        return result;
    }

    private List<String> initComputingOperation() {
        List<String> result=new ArrayList<>();
        //BINARY @> BINARY STAR BINARY(89)
        result.add("BINARY@BINARY@STAR@BINARY");
        //BINARY @> BINARY DIVOP BINARY(90)
        result.add("BINARY@BINARY@DIVOP@BINARY");
        //BINARY @> BINARY SHIFTOP BINARY(91)
        result.add("BINARY@BINARY@SHIFTOP@BINARY");
        //BINARY @> BINARY AND BINARY(92)
        result.add("BINARY@BINARY@AND@BINARY");
        //BINARY @> BINARY XOR BINARY(93)
        result.add("BINARY@BINARY@XOR@BINARY");
        //BINARY @> BINARY PLUS BINARY(94)
        result.add("BINARY@BINARY@PLUS@BINARY");
        //BINARY @> BINARY MINUS BINARY(95)
        result.add("BINARY@BINARY@MINUS@BINARY");
        //UNARY @> UNARY INCOP i++ (96)
        result.add("UNARY@UNARY@INCOP");
        //UNARY @> INCOP UNARY ++i (97)
        result.add("UNARY@INCOP@UNARY");
        //UNARY @> MINUS UNARY  a = @a (98)
        result.add("UNARY@MINUS@UNARY");
        //UNARY @> STAR UNARY b = *a (99)
        result.add("UNARY@STAR@UNARY");
        //UNARY @> UNARY STRUCTOP NAME  a = tag@>name (100)
        result.add("UNARY@UNARY@STRUCTOP@NAME");
        //UNARY @> UNARY LB EXPR RB b = a[2]; (101)
        result.add("UNARY@UNARY@LB@EXPR@RB");
        //UNARY @> UNARY  LP ARGS RP  fun(a, b ,c) (102)
        result.add("UNARY@UNARY@LP@ARGS@RP");
        //UNARY @> UNARY LP RP  fun() (103)
        result.add("UNARY@UNARY@LP@RP");
        //ARGS @> NO_COMMA_EXPR (104)
        result.add("ARGS@NO_COMMA_EXPR");
        //ARGS @> NO_COMMA_EXPR COMMA ARGS (105)
        result.add("ARGS@NO_COMMA_EXPR@COMMA@ARGS");
        return result;
    }

    private List<String> initRemaindingProduction() {
        List<String> result=new ArrayList<>();
        //STATEMENT @> TARGET COLON STATEMENT
        result.add("STATEMENT@TARGET@COLON@STATEMENT");
        //STATEMENT @> GOTO TARGET SEMI
        result.add("STATEMENT@GOTO@TARGET@SEMI");
        //TARGET @> NAME
        result.add("TARGET@NAME");
        //VAR_DECL @> VAR_DECL LB CONST_EXPR RB  a[5] (109)
        result.add("VAR_DECL@VAR_DECL@LB@CONST_EXPR@RB");
        //COMPOUND_STMT@> LC  STMT_LIST RC(110)
        result.add("COMPOUND_STMT@LC@STMT_LIST@RC");
        //STATEMENT @> RETURN SEMI (111)
        result.add("STATEMENT@RETURN@SEMI");
        //UNARY @> LP EXPR RP (112)
        result.add("UNARY@LP@EXPR@RP");
        //UNARY @> UNARY DECOP i@@ (113)
        result.add("UNARY@UNARY@DECOP");
        return result;
    }

}
