package top.modty.ccompiler.dfaNfa;


import java.util.*;


public class NfaPrinter {
	public HashMap<Integer, HashMap<String,Object>> nfaTable;
	HashMap<String, List<HashMap<String, Object>>> response;
	// 接受状态
	public static HashSet<Integer> ends;
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
    		System.out.println("--------NFA--------"+Thread.currentThread().getName());
    		response=new HashMap<>();
    		response.put("edg",new ArrayList<>());
    		response.put("state",new ArrayList<>());
    	}
    	
    	startNfa.setVisited();
    	
    	printNfaNode(startNfa);
    	
    	if (start) {
    		System.out.println("  (START STATE)");
    		start = false;
    	}
    	System.out.println();
		printNfa(startNfa.next);
    	printNfa(startNfa.next2);
    }
    
    private void printNfaNode(Nfa node) {
		HashMap<String,Object> treeNodeSytate=new HashMap<>();

		if (node.next == null) {
			treeNodeSytate.put("id",node.getStateNum());
			treeNodeSytate.put("label",String.valueOf(node.getStateNum()));
			treeNodeSytate.put("class","type-fail");
			ends.add(node.getStateNum());
    		System.out.print("TERMINAL");
    	}
    	else {
			HashMap<String,Object> treeNode=new HashMap<>();
    		HashMap<String,Object> temp=new HashMap<>();
    		List<Integer> list=new ArrayList<>();
			nfaTable.put(node.getStateNum(),temp);
			list.add(node.next.getStateNum());
    		System.out.print("NFA state: " + node.getStateNum());
			treeNodeSytate.put("id",node.getStateNum());
			treeNodeSytate.put("label",String.valueOf(node.getStateNum()));
			treeNodeSytate.put("class","type-suss");
    		System.out.print("--> " + node.next.getStateNum());
			treeNode.put("start",node.getStateNum());
			treeNode.put("end",node.next.getStateNum());
			System.out.print(" on:");
			switch (node.getEdge()) {
				case Nfa.CCL:
					temp.put("conditions",printCCL(node.inputSet));
					treeNode.put("label",String.valueOf(temp.get("conditions")).equals("[ ^@^A^B^C^D^E^F^G^H^I^K^L^N^O^P^Q^R^S^T^U^V^W^X^Y^Z^[^\\^]^^^_ !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ]")?"all":String.valueOf(temp.get("conditions")));
					break;
				case Nfa.EPSILON:
					System.out.print("EPSILON ");
					temp.put("conditions","EPSILON ");
					treeNode.put("label","EPSILON ");
					break;
				default:
					System.out.print((char)node.getEdge());
					temp.put("conditions",node.getEdge());
					treeNode.put("label",(char)node.getEdge());
					break;
			}
			if (node.next2 != null) {
				HashMap<String,Object> treeNode1=new HashMap<>();
				treeNode1.put("start",node.getStateNum());
				treeNode1.put("end",node.next2.getStateNum());
				treeNode1.put("label",String.valueOf(treeNode.get("label")));

				list.add(node.next2.getStateNum());
    			System.out.print(" " + node.next2.getStateNum() );
				response.get("edg").add(treeNode1);
    		}
    		temp.put("next",list);
    		response.get("edg").add(treeNode);
		}
		response.get("state").add(treeNodeSytate);
    }
}
