package top.modty.ccompiler.dfaNfa;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MacroHandler {
	/*
	 * 宏定义如下：
	 * 宏名称 <空格>  宏内容 [<空格>]
	 */
	
	private HashMap<String, String> macroMap = new HashMap<String, String>();

	public MacroHandler(HashMap<String,String> macro) {
		this.macroMap=macro;
	}
	
	public String expandMacro(String macroName) throws Exception {
		if (macroMap.containsKey(macroName) == false) {
			ErrorHandler.parseErr(ErrorHandler.Error.E_NOMAC);
		}
		else {
			return "(" + macroMap.get(macroName) + ")";
		}
		
		return "ERROR"; //走到这里的话就是bug
	}
	
	public void printMacs() {
		if (macroMap.isEmpty()) {
			System.out.println("There are no macros");
		}
		else {
			Iterator iter = macroMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
				System.out.println("Macro name: " + entry.getKey() + " Macro content: " + entry.getValue());
			}
		}
	}
}
