package top.modty.ccompiler.commons;

/**
 * @author 点木
 * @date 2020-05-05 16:05
 * @mes
 */
public class Node {
    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name+"\"" +
                ", \"value\":" + value +
                '}';
    }
}
