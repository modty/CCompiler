package top.modty.ccompiler.dfaNfa;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class NfaPrinter {
	public HashMap<Integer, HashMap<String,Object>> nfaTable;
	private static final int ASCII_NUM = 128;
	private boolean start = true;

	public NfaPrinter() {
		nfaTable=new HashMap<>();
	}

	private String printCCL(Set<Byte> set) {
		String s="[ ";
    	System.out.print("[ ");
    	for (int i = 0; i < ASCII_NUM; i++) {
    		if (set.contains((byte)i)) {
    			if (i < ' ') {
					s+="^"+(char)(i+'@');
    				System.out.print("^" + (char)(i + '@'));
    			}
    			else {
					s+=((char)i);
    				System.out.print((char)i);
    			}
    		}
    	}
    	s+=" ]";
    	System.out.print(" ]");
    	return s;
    }
    
    public void printNfa(Nfa startNfa) {
    	if (startNfa == null || startNfa.isVisited()) {
    		return;
    	}
    	
    	if (start) {
    		System.out.println("--------NFA--------");
    	}
    	
    	startNfa.setVisited();
    	
    	printNfaNode(startNfa);
    	
    	if (start) {
    		System.out.print("  (START STATE)");
    		start = false;
    	}
    	
    	System.out.print("\n");
    	
    	printNfa(startNfa.next);
    	printNfa(startNfa.next2);
    }
    
    private void printNfaNode(Nfa node) {
    	if (node.next == null) {
    		System.out.print("TERMINAL");
    	}
    	else {
    		HashMap<String,Object> temp=new HashMap<>();
    		List<Integer> list=new ArrayList<>();
			nfaTable.put(node.getStateNum(),temp);
			list.add(node.next.getStateNum());
    		System.out.print("NFA state: " + node.getStateNum());
    		System.out.print("--> " + node.next.getStateNum());
    		if (node.next2 != null) {
				list.add(node.next2.getStateNum());
    			System.out.print(" " + node.next2.getStateNum() );
    		}
    		temp.put("next",list);
    		System.out.print(" on:");
    		switch (node.getEdge()) {
    		case Nfa.CCL:
    			temp.put("conditions",printCCL(node.inputSet));
				break;
    		case Nfa.EPSILON:
    			System.out.print("EPSILON ");
				temp.put("conditions","EPSILON ");
    			break;
    		default:
    			System.out.print((char)node.getEdge());
				temp.put("conditions",node.getEdge());
    			break;
    		}
    	}
    }
    
}
