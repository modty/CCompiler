package top.modty.ccompiler.error;

/**
 * @author 点木
 * @date 2020-05-03 18:35
 * @mes 错误类型枚举
 */
public enum ErrorType {
    // 词法分析出错
    ERROR_LEX;
    public static String getSymbolStr(int val) {
        return ErrorType.values()[val].toString();
    }
}
