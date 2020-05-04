package top.modty.ccompiler.commons.error;

import top.modty.ccompiler.commons.constants.ErrorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 点木
 * @date 2020-05-03 18:31
 * @mes 错误管理
 */
public class Errors {
    // 字符串缺少末尾结束
    public static final int Error_missing_the_ending_quataion_mark=0;

    private static volatile Errors instance;
    private Map<String, List<Error>> errorsMap;

    private Errors(){
        errorsMap=new HashMap<>();
        for(ErrorType errorType:ErrorType.values()){
            errorsMap.put(ErrorType.getSymbolStr(errorType.ordinal()),new ArrayList<>());
        }
    }
    public static Errors getInstance() {
        if(instance==null){
            instance=new Errors();
        }
        return instance;
    }


    public Error addError(ErrorType errorType,String error_word,String error_mes,int position){
        Error error=new Error(error_word,error_mes,position);
        errorsMap.get(ErrorType.getSymbolStr(errorType.ordinal())).add(error);
        return error;
    }

    public Map<String, List<Error>> getErrorsMap() {
        return errorsMap;
    }
}
