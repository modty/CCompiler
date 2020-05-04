package top.modty.ccompiler.error;

/**
 * @author 点木
 * @date 2020-05-03 18:20
 * @mes 错误信息
 */
public class Error {

    private String error_word;
    private String error_mes;
    private int error_postion;

    public Error(String error_word, String error_mes, int error_postion) {
        this.error_word = error_word;
        this.error_mes = error_mes;
        this.error_postion = error_postion;
    }

    @Override
    public String toString() {
        return "Error{" +
                "error_word='" + error_word + '\'' +
                ", error_mes='" + error_mes + '\'' +
                ", error_postion=" + error_postion +
                '}';
    }
}
