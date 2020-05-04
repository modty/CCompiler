package top.modty.ccompiler.commons.constants;

import java.util.*;

/**
 * @author 点木
 * @date 2020-05-04 21:20
 * @mes
 */
public class Grammar {
    public HashMap<String, Map<String,String[]>> grammers;
    public Grammar(){
        grammers=new HashMap<>();
        grammers.put("VariableDecalation",initVariableDecalationProductions());
        grammers.put("Function",initFunctionProductions());
        grammers.put("Structure",initStructureProductions());
        grammers.put("Emun",initEmunProductions());
        grammers.put("FunctionDefinition",initFunctionDefinition());
        grammers.put("FunctionDefinitionWithIfElse",initFunctionDefinitionWithIfElse());
        grammers.put("FunctionDefinitionWithSwitchCase",initFunctionDefinitionWithSwitchCase());
        grammers.put("FunctionDefinitionWithLoop",initFunctionDefinitionWithLoop());
        grammers.put("ComputingOperation",initComputingOperation());
        grammers.put("Remainding",initRemaindingProduction());
    }

    private Map<String,String[]> initVariableDecalationProductions() {
        Map<String,String[]> result=new HashMap<>();
        //PROGRAM -> EXT_DEF_LIST
        result.put("PROGRAM", new String[]{"EXT_DEF_LIST"});
        //EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
        result.put("EXT_DEF_LIST", new String[]{"EXT_DEF_LIST","EXT_DEF"});
        //EXT_DEF -> OPT_SPECIFIERS EXT_DECL_LIST  SEMI
        result.put("EXT_DEF", new String[]{"OPT_SPECIFIERS","EXT_DECL_LIST","SEMI"});
        //EXT_DEF -> OPT_SPECIFIERS  SEMI
        result.put("EXT_DEF", new String[]{"OPT_SPECIFIERS","SEMI"});
        //EXT_DECL_LIST ->   EXT_DECL
        result.put("EXT_DECL_LIST", new String[]{"EXT_DECL"});
        ///EXT_DECL_LIST ->EXT_DECL_LIST COMMA EXT_DECL
        result.put("EXT_DECL_LIST", new String[]{"EXT_DECL_LIST","COMMA","EXT_DECL"});
        //EXT_DECL -> VAR_DECL
        result.put("EXT_DECL", new String[]{"VAR_DECL"});
        //OPT_SPECIFIERS -> SPECIFIERS
        result.put("OPT_SPECIFIERS", new String[]{"SPECIFIERS"});
        //SPECIFIERS -> TYPE_OR_CLASS
        result.put("SPECIFIERS", new String[]{"TYPE_OR_CLASS"});
        //SPECIFIERS -> SPECIFIERS TYPE_OR_CLASS
        result.put("SPECIFIERS", new String[]{"SPECIFIERS","TYPE_OR_CLASS"});
        //TYPE_OR_CLASS -> TYPE_SPECIFIER
        result.put("TYPE_OR_CLASS", new String[]{"TYPE_SPECIFIER"});
        //TYPE_SPECIFIER ->  TYPE
        result.put("TYPE_SPECIFIER", new String[]{"TYPE"});
        //NEW_NAME -> NAME
        result.put("NEW_NAME", new String[]{"NAME"});
        //VAR_DECL ->  NEW_NAME(13)
        result.put("VAR_DECL", new String[]{"NEW_NAME"});
        ///VAR_DECL ->START VAR_DECL
        result.put("VAR_DECL", new String[]{"START","VAR_DECL"});
        return result;
    }

    private Map<String,String[]> initFunctionProductions() {
        Map<String,String[]> result=new HashMap<>();
        //EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL SEMI(15)
        result.put("EXT_DEF", new String[]{"OPT_SPECIFIERS","FUNCT_DECL","SEMI"});
        //FUNCT_DECL -> NEW_NAME LP VAR_LIST RP(16)
        result.put("FUNCT_DECL", new String[]{"NEW_NAME","LP","VAR_LIST","RP"});
        //FUNCT_DECL ->  NEW_NAME LP RP(17)
        result.put("FUNCT_DECL", new String[]{"NEW_NAME","LP","RP"});
        //VAR_LIST ->  PARAM_DECLARATION
        result.put("VAR_LIST", new String[]{"PARAM_DECLARATION"});
        //VAR_LIST -> VAR_LIST COMMA PARAM_DECLARATION
        result.put("VAR_LIST", new String[]{"VAR_LIST","COMMA","PARAM_DECLARATION"});
        //PARAM_DECLARATION -> TYPE_NT VAR_DECL
        result.put("PARAM_DECLARATION", new String[]{"TYPE_NT","VAR_DECL"});
        //TYPE_NT -> TYPE_SPECIFIER
        result.put("TYPE_NT", new String[]{"TYPE_SPECIFIER"});
        //TYPE_NT -> TYPE TYPE_SPECIFIER
        result.put("TYPE_NT", new String[]{"TYPE","TYPE_SPECIFIER"});
        return result;

    }

    private Map<String,String[]> initStructureProductions() {
        Map<String,String[]> result=new HashMap<>();
        //TYPE_SPECIFIER -> STRUCT_SPECIFIER  (23)
        result.put("TYPE_SPECIFIER", new String[]{"STRUCT_SPECIFIER"});
        //STTUCT_SPECIFIER -> STRUCT OPT_TAG LC DEF_LIST RC (24)
        result.put("STTUCT_SPECIFIER", new String[]{"STRUCT","OPT_TAG","LC","DEF_LIST","RC"});
        //STRUCT_SPECIFIER -> STRUCT TAG (25)
        result.put("STRUCT_SPECIFIER", new String[]{"STRUCT","TAG"});
        //OPT_TAG -> TAG (26)
        result.put("OPT_TAG", new String[]{"TAG"});
        //TAG -> NAME (27)
        result.put("TAG", new String[]{"NAME"});
        //DEF_LIST ->  DEF (28)
        result.put("DEF_LIST", new String[]{"DEF"});
        //DEF_LIST -> DEF_LIST DEF (29)
        result.put("DEF_LIST", new String[]{"DEF_LIST","DEF"});
        //DEF -> SPECIFIERS DECL_LIST SEMI (30)
        result.put("DEF", new String[]{"SPECIFIERS","DECL_LIST","SEMI"});
        //DEF -> SPECIFIERS SEMI (31)
        result.put("DEF", new String[]{"SPECIFIERS","SEMI"});
        //DECL_LIST -> DECL (32)
        result.put("DECL_LIST", new String[]{"DECL"});
        //DECL_LIST -> DECL_LIST COMMA DECL (33)
        result.put("DECL_LIST", new String[]{"DECL_LIST","COMMA","DECL"});
        //DECL -> VAR_DECL (34)
        result.put("DECL", new String[]{"VAR_DECL"});
        //VAR_DECL -> NEW_NAME (35)
        result.put("VAR_DECL", new String[]{"NEW_NAME"});
        //VAR_DECL -> VAR_DECL LP RP (36)
        result.put("VAR_DECL", new String[]{"VAR_DECL","LP","RP"});
        //VAR_DECL -> VAR_DECL LP VAR_LIS RP (37)
        result.put("VAR_DECL", new String[]{"VAR_DECL","LP","VAR_LIS","RP"});
        //VAR_DECL -> LP VAR_DECL RP (38)
        result.put("VAR_DECL", new String[]{"LP","VAR_DECL","RP"});
        //VAR_DECL -> STAR VAR_DECL (39)
        result.put("VAR_DECL", new String[]{"STAR","VAR_DECL"});
        return result;
    }

    private Map<String,String[]> initEmunProductions() {
        Map<String,String[]> result=new HashMap<>();
        //ENUM_SPECIFIER -> ENUM_NT NAME_NT OPT_ENUM_LIST(40)
        result.put("ENUM_SPECIFIER", new String[]{"ENUM_NT","NAME_NT","OPT_ENUM_LIST"});
        //ENUM_NT -> ENUM(41)
        result.put("ENUM_NT", new String[]{"ENUM"});
        //ENUMERATOR_LIST -> ENUMERATOR(42)
        result.put("ENUMERATOR_LIST", new String[]{"ENUMERATOR"});
        //EMERATOR_LIST -> ENUMERATOR_LIST COMMA ENUMERATOR(43)
        result.put("EMERATOR_LIST", new String[]{"ENUMERATOR_LIST","COMMA","ENUMERATOR"});
        //ENUMERATOR -> NAME_NT(44)
        result.put("ENUMERATOR", new String[]{"NAME_NT"});
        //NAME_NT -> NAME(45)
        result.put("NAME_NT", new String[]{"NAME"});
        //ENUMERATOR -> NAME_NT EQUAL CONST_EXPR(46)
        result.put("ENUMERATOR", new String[]{"NAME_NT","EQUAL","CONST_EXPR"});
        //CONST_EXPR -> NUMBER (47)
        result.put("CONST_EXPR", new String[]{"NUMBER"});
        //OPT_ENUM_LIST -> LC ENUMERATOR_LIST RC (48)
        result.put("OPT_ENUM_LIST", new String[]{"LC","ENUMERATOR_LIST","RC"});
        //TYPE_SPECIFIER -> ENUM_SPECIFIER (49)
        result.put("TYPE_SPECIFIER", new String[]{"ENUM_SPECIFIER"});
        return result;
    }

    private Map<String,String[]> initFunctionDefinition() {
        Map<String,String[]> result=new HashMap<>();
        //EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL COMPOUND_STMT(50)
        result.put("EXT_DEF", new String[]{"OPT_SPECIFIERS","FUNCT_DECL","COMPOUND_STMT"});
        //COMPOUND_STMT-> LC LOCAL_DEFS STMT_LIST RC(51)
        result.put("COMPOUND_STMT", new String[]{"LC","LOCAL_DEFS","STMT_LIST","RC"});
        //LOCAL_DEFS -> DEF_LIST(52)
        result.put("LOCAL_DEFS", new String[]{"DEF_LIST"});
        //EXPR -> NO_COMMA_EXPR(53)
        result.put("EXPR", new String[]{"NO_COMMA_EXPR"});
        //NO_COMMA_EXPR -> NO_COMMA_EXPR EQUAL NO_COMMA_EXPR(54)
        result.put("NO_COMMA_EXPR", new String[]{"NO_COMMA_EXPR","EQUAL","NO_COMMA_EXPR"});
        //NO_COMMA_EXPR -> NO_COMMA_EXPR QUEST  NO_COMMA_EXPR COLON NO_COMMA_EXPR(55)
        result.put("NO_COMMA_EXPR", new String[]{"NO_COMMA_EXPR","QUEST","NO_COMMA_EXPR","COLON","NO_COMMA_EXPR"});
        //NO_COMMA_EXPR -> BINARY(56)
        result.put("NO_COMMA_EXPR", new String[]{"BINARY"});
        //BINARY -> UNARY (57)
        result.put("BINARY", new String[]{"UNARY"});
        //UNARY -> NUMBER (58)
        result.put("UNARY", new String[]{"NUMBER"});
        //UNARY -> NAME (59)
        result.put("UNARY", new String[]{"NAME"});
        //UNARY -> STRING(60)
        result.put("UNARY", new String[]{"STRING"});
        //STMT_LIST -> STMT_LIST STATEMENT(61)
        result.put("STMT_LIST", new String[]{"STMT_LIST","STATEMENT"});
        //STMT_LIST ->  STATEMENT(62)
        result.put("STMT_LIST", new String[]{"STATEMENT"});
        //STATEMENT -> EXPR SEMI(63)
        result.put("STATEMENT", new String[]{"EXPR","SEMI"});
        //STATEMENT -> RETURN EXPR SEMI (64)
        result.put("STATEMENT", new String[]{"RETURN","EXPR","SEMI"});
        //BINARY -> BINARY RELOP BINARY (65)
        result.put("BINARY", new String[]{"BINARY","RELOP","BINARY"});
        //BINARY -> BINARY EQUOP BINARY (66)
        result.put("BINARY", new String[]{"BINARY","EQUOP","BINARY"});
        //BINARY -> BINARY START BINARY (67)
        result.put("BINARY", new String[]{"BINARY","START","BINARY"});
        //STATEMENT -> LOCAL_DEFS(68)
        result.put("STATEMENT", new String[]{"LOCAL_DEFS"});
        //COMPOUND_STMT -> LC RC(69)
        result.put("COMPOUND_STMT", new String[]{"LC","RC"});
        //COMPOUNT_STMT -> LC STMT_LIST RC(70)
        result.put("COMPOUNT_STMT", new String[]{"LC","STMT_LIST","RC"});
        return result;
    }

    private Map<String,String[]> initFunctionDefinitionWithIfElse() {
        Map<String,String[]> result=new HashMap<>();
        //STATEMENT -> COMPOUND_STMT (71)
        result.put("STATEMENT", new String[]{"COMPOUND_STMT"});
        //IF_STATEMENT -> IF LP TEST RP STATEMENT (72)
        result.put("IF_STATEMENT", new String[]{"IF","LP","TEST","RP","STATEMENT"});
        //IF_ELSE_STATEMENT -> IF_STATEMENT 73
        result.put("IF_ELSE_STATEMENT", new String[]{"IF_STATEMENT"});
        //IF_ELSE_STATEMENT ->IF_ELSE_STATEMENT ELSE STATEMENT 74
        result.put("IF_ELSE_STATEMENT", new String[]{"IF_ELSE_STATEMENT","ELSE","STATEMENT"});
        //STATEMENT -> IF_ELSE_STATEMENT 75
        result.put("STATEMENT", new String[]{"IF_ELSE_STATEMENT"});
        //TEST -> EXPR 76
        result.put("TEST", new String[]{"EXPR"});
        //DECL -> VAR_DECL EQUAL INITIALIZER 77
        result.put("DECL", new String[]{"VAR_DECL","EQUAL","INITIALIZER"});
        //INITIALIZER -> EXPR 78
        result.put("INITIALIZER", new String[]{"EXPR"});
        return result;
    }

    private Map<String,String[]> initFunctionDefinitionWithSwitchCase() {
        Map<String,String[]> result=new HashMap<>();
        //STATEMENT -> SWITCH LP EXPR RP COMPOUND_STATEMENT (79)
        result.put("STATEMENT", new String[]{"SWITCH","LP","EXPR","RP","COMPOUND_STATEMENT"});
        //STATEMENT -> CASE CONST_EXPR COLON(80)
        result.put("STATEMENT", new String[]{"CASE","LP","CONST_EXPR","COLON"});
        //STATEMENT -> DEFAULT COLON (81)
        result.put("STATEMENT", new String[]{"DEFAULT","COLON"});
        //STATEMENT -> BREAK SEMI; (82)
        result.put("STATEMENT", new String[]{"BREAK","SEMI"});
        return result;
    }

    private Map<String,String[]> initFunctionDefinitionWithLoop() {
        Map<String,String[]> result=new HashMap<>();
        //STATEMENT -> WHILE LP TEST RP STATEMENT (83)
        result.put("STATEMENT", new String[]{"WHILE","LP","TEST","RP","STATEMENT"});
        //STATEMENT -> FOR LP OPT_EXPR  TEST SEMI END_OPT_EXPR RP STATEMENT(84)
        result.put("STATEMENT", new String[]{"FOR","LP","OPT_EXPR","TEST","SEMI","END_OPT_EXPR","RP","STATEMENT"});
        //OPT_EXPR -> EXPR SEMI(85)
        result.put("OPT_EXPR", new String[]{"EXPR","SEMI"});
        //OPT_EXPR -> SEMI(86)
        result.put("OPT_EXPR", new String[]{"SEMI"});
        //END_OPT_EXPR -> EXPR(87)
        result.put("END_OPT_EXPR", new String[]{"EXPR"});
        //STATEMENT -> DO STATEMENT WHILE LP TEST RP SEMI(88)
        result.put("STATEMENT", new String[]{"DO","STATEMENT","WHILE","LP","TEST","RP","RP","SEMI"});
        return result;
    }

    private Map<String,String[]> initComputingOperation() {
        Map<String,String[]> result=new HashMap<>();
        //BINARY -> BINARY STAR BINARY(89)
        result.put("BINARY", new String[]{"BINARY","STAR","BINARY"});
        //BINARY -> BINARY DIVOP BINARY(90)
        result.put("BINARY", new String[]{"BINARY","DIVOP","BINARY"});
        //BINARY -> BINARY SHIFTOP BINARY(91)
        result.put("BINARY", new String[]{"BINARY","SHIFTOP","BINARY"});
        //BINARY -> BINARY AND BINARY(92)
        result.put("BINARY", new String[]{"BINARY","AND","BINARY"});
        //BINARY -> BINARY XOR BINARY(93)
        result.put("BINARY", new String[]{"BINARY","XOR","BINARY"});
        //BINARY -> BINARY PLUS BINARY(94)
        result.put("BINARY", new String[]{"BINARY","PLUS","BINARY"});
        //BINARY -> BINARY MINUS BINARY(95)
        result.put("BINARY", new String[]{"BINARY","MINUS","BINARY"});
        //UNARY -> UNARY INCOP i++ (96)
        result.put("UNARY", new String[]{"UNARY","INCOP"});
        //UNARY -> INCOP UNARY ++i (97)
        result.put("UNARY", new String[]{"INCOP","UNARY"});
        //UNARY -> MINUS UNARY  a = -a (98)
        result.put("UNARY", new String[]{"MINUS","UNARY"});
        //UNARY -> STAR UNARY b = *a (99)
        result.put("UNARY", new String[]{"STAR","UNARY"});
        //UNARY -> UNARY STRUCTOP NAME  a = tag->name (100)
        result.put("UNARY", new String[]{"UNARY","STRUCTOP","NAME"});
        //UNARY -> UNARY LB EXPR RB b = a[2]; (101)
        result.put("UNARY", new String[]{"UNARY","LB","EXPR","RB"});
        //UNARY -> UNARY  LP ARGS RP  fun(a, b ,c) (102)
        result.put("UNARY", new String[]{"UNARY","LP","ARGS","RP"});
        //UNARY -> UNARY LP RP  fun() (103)
        result.put("UNARY", new String[]{"UNARY","LP","RP"});
        //ARGS -> NO_COMMA_EXPR (104)
        result.put("ARGS", new String[]{"NO_COMMA_EXPR"});
        //ARGS -> NO_COMMA_EXPR COMMA ARGS (105)
        result.put("ARGS", new String[]{"NO_COMMA_EXPR","COMMA","ARGS"});
        return result;
    }

    private Map<String,String[]> initRemaindingProduction() {
        Map<String,String[]> result=new HashMap<>();
        //STATEMENT -> TARGET COLON STATEMENT
        result.put("STATEMENT", new String[]{"TARGET","COLON","STATEMENT"});
        //STATEMENT -> GOTO TARGET SEMI
        result.put("STATEMENT", new String[]{"GOTO","TARGET","SEMI"});
        //TARGET -> NAME
        result.put("TARGET", new String[]{"NAME"});
        //VAR_DECL -> VAR_DECL LB CONST_EXPR RB  a[5] (109)
        result.put("VAR_DECL", new String[]{"VAR_DECL","LB","CONST_EXPR","RB"});
        //COMPOUND_STMT-> LC  STMT_LIST RC(110)
        result.put("COMPOUND_STMT", new String[]{"LC","STMT_LIST","RC"});
        //STATEMENT -> RETURN SEMI (111)
        result.put("STATEMENT", new String[]{"RETURN","SEMI"});
        //UNARY -> LP EXPR RP (112)
        result.put("UNARY", new String[]{"LP","EXPR","RP"});
        //UNARY -> UNARY DECOP i-- (113)
        result.put("UNARY", new String[]{"UNARY","DECOP"});
        return result;
    }

}
