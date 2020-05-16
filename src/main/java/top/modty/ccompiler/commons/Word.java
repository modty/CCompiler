package top.modty.ccompiler.commons;

/**
 * @author 点木
 * @date 2020-05-04 12:48
 * @mes
 */
public class Word {
    private String word;
    private int position;
    private int type;
    private String mes;

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", position=" + position +
                ", type=" + type +
                ", mes='" + mes + '\'' +
                '}';
    }

    public Word(String word,String mes) {
        this.word = word;
        this.mes=mes;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Word(String word) {
        this.word = word;
    }

    public Word(String word, int position, int type, String mes) {
        this.word = word;
        this.position = position;
        this.type = type;
        this.mes = mes;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
