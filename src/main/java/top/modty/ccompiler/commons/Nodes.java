package top.modty.ccompiler.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 点木
 * @date 2020-05-05 16:15
 * @mes
 */
public class Nodes {
    private String name;
    private List<Object> children;

    public Nodes() {
        children=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                ", \"children\":" + children +
                "}";
    }

    public Nodes(String name, List<Object> children) {
        this();
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }
}
