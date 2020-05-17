package top.modty.ccompiler.symbolFirst;

/**
 * @author 点木
 * @date 2020-05-16 21:01
 * @mes
 */
public class SymbolNode {
    private String stackValue;
    private String action;
    private String actionMes;

    public SymbolNode(String stackValue, String action, String actionMes) {
        this.stackValue = stackValue;
        this.action = action;
        this.actionMes = actionMes;
    }

    public String getStackValue() {
        return stackValue;
    }

    public void setStackValue(String stackValue) {
        this.stackValue = stackValue;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionMes() {
        return actionMes;
    }

    public void setActionMes(String actionMes) {
        this.actionMes = actionMes;
    }
}
