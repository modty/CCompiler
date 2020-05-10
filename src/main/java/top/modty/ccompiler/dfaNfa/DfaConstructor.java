package top.modty.ccompiler.dfaNfa;


import java.util.*;


public class DfaConstructor {
    private NfaPair nfaMachine = null;
    private NfaIntepretor nfaIntepretor = null;
    
    private ArrayList<Dfa> dfaList = new ArrayList<Dfa>();
    public HashSet<Integer> dafHelper=new HashSet<>();
    public HashMap<String, List<HashMap<String, Object>>> dfa=new HashMap<>();
    public static HashSet<Integer> ends;
    //假定DFA状态机节点数不会超过254个
    private static final int MAX_DFA_STATE_COUNT = 254; 
    
    private static final int ASCII_COUNT = 128;

    private static final int STATE_FAILURE = -1;

    //使用二维数组表示DFA有限状态自动机
    private int[][] dfaStateTransformTable = new int[MAX_DFA_STATE_COUNT][ASCII_COUNT + 1];

    public DfaConstructor(NfaPair pair, NfaIntepretor nfaIntepretor) {
    	this.nfaIntepretor = nfaIntepretor;
    	//不要打印内部信息
    	this.nfaIntepretor.debug = false;

    	this.nfaMachine = pair;
		this.dfa.put("state",new ArrayList<>());
		initTransformTable();
    }
    
    private void initTransformTable() {
    	for (int i = 0; i < MAX_DFA_STATE_COUNT; i++)
    		for (int j = 0; j <= ASCII_COUNT; j++) {
    			dfaStateTransformTable[i][j] = STATE_FAILURE;
    		}
    }
    
    public int[][] convertNfaToDfa() {
    	Set<Nfa> input = new HashSet<Nfa>();
    	input.add(nfaMachine.startNode);
    	Set<Nfa> nfaStartClosure = nfaIntepretor.e_closure(input);
    	Dfa start = Dfa.getDfaFromNfaSet(nfaStartClosure);
    	dfaList.add(start);
    	
    	System.out.println("Create DFA start node:");
    	printDfa(start);
    	
    	
    	int nextState = STATE_FAILURE;
        int currentDfaIndex = 0;
    	while (currentDfaIndex < dfaList.size()) {
            Dfa currentDfa = dfaList.get(currentDfaIndex);
            //下面代码用于调试演示
            boolean debug = false;
            if (currentDfa.stateNum == 5 || currentDfa.stateNum == 7) {
            	debug = true;
            }
            //上面代码用于调试演示
            
    		for (char c = 0; c <= ASCII_COUNT; c++) {
    			Set<Nfa> move = nfaIntepretor.move(currentDfa.nfaStates, c);
    			
    			if (move.isEmpty()) {
    				nextState = STATE_FAILURE;
    			}
    			else {
        			Set<Nfa> closure = nfaIntepretor.e_closure(move);
        			
    				Dfa dfa = isNfaStatesExistInDfa(closure);
    				
    				
        			if ( dfa == null) {
        				
        				System.out.println("Create DFA node:");
//						this.nfaIntepretor.debug = false;
						Dfa newDfa = Dfa.getDfaFromNfaSet(closure);
        				printDfa(newDfa);
        				
        				dfaList.add(newDfa);
        				nextState = newDfa.stateNum;
        			}
        			else {
        				System.out.println("Get a existed dfa node:");
        				printDfa(dfa);
        				nextState = dfa.stateNum;
        			}
    			}
    			
    			if (nextState != STATE_FAILURE) {
    				System.out.println("	DFA from state: " + currentDfa.stateNum + " to state:" + nextState + " on char: " + c);
    			}
    			dfaStateTransformTable[currentDfa.stateNum][c] = nextState;
    	    }
    		
    		System.out.print("\n");
    		currentDfaIndex++;
    	}
		this.nfaIntepretor.debug = false;
    	return dfaStateTransformTable;
    }
    
    public ArrayList<Dfa>  getDfaList() {
    	return dfaList;
    }
    
    public int[][] getDfaTransTable() {
    	return dfaStateTransformTable;
    }
    
    
    private Dfa isNfaStatesExistInDfa(Set<Nfa> closure) {
    	
    	Iterator<Dfa> it = dfaList.iterator();
    	while (it.hasNext()) {
    		Dfa dfa = it.next();
    		if (dfa.hasNfaStates(closure)) {
    			return dfa;
    		}
    	}
    	return null;
    }
    
    private void printDfa(Dfa dfa) {
		HashMap<String,Object> state=new HashMap<>();
		System.out.print("\tDfa state: " + dfa.stateNum + " its nfa states are: ");
		String id="S"+dfa.stateNum+"\\n";
    	Iterator<Nfa> it = dfa.nfaStates.iterator();
    	boolean isEnd=false;
    	while (it.hasNext()) {
			int stateNum=it.next().getStateNum();
    		System.out.print(stateNum);
    		id+="[ "+stateNum;
			state.put("id","S"+dfa.stateNum+"\\n");
			if(!isEnd&&NfaPrinter.ends.contains(stateNum)){
				isEnd=true;
				ends.add(dfa.stateNum);
			}
			if (it.hasNext()) {
    			System.out.print(",");
    		}
			id+=" ]";
			state.put("id",dfa.stateNum);
			state.put("label",id);
			if (isEnd){
				state.put("class","type-fail");
			}else {
				state.put("class","type-suss");
			}
		}
    	System.out.print("\n");
    	if(!dafHelper.contains(dfa.stateNum)){
			this.dfa.get("state").add(state);
			dafHelper.add(dfa.stateNum);
		}
	}
    
    public HashMap<String, List<HashMap<String, Object>>> printDFA() {
    	dfa.put("edg",new ArrayList<>());
    	int dfaNum = dfaList.size();
    	for (int i = 0; i < dfaNum; i++)
    		for (int j = 0; j < dfaNum; j++) {
    			HashMap<String,Object> edg=new HashMap<>();
				edg.put("start",i);
				edg.put("end",j);
    			if (isOnNumberClass(i,j)) {
    				System.out.println("From state " + i + " to state " + j + " on D");
					edg.put("label","D");
					dfa.get("edg").add(edg);
				}else {
    				char c=isOnDot(i,j);
    				if(c!='0'){
						System.out.println("From state " + i + " to state " + j + " on "+c);
						edg.put("label",c);
						dfa.get("edg").add(edg);
					}
				}
    		}
		Dfa.STATE_NUM=0;
    	return dfa;
    }
    
    private boolean isOnNumberClass(int from, int to) {
    	char c = '0';
    	for (c = '0'; c <= '9'; c++) {
    		if (dfaStateTransformTable[from][c] != to) {
    			return false;
    		}
    	}
    	return true;
    }
    
    private char isOnDot(int from, int to) {
    	for(int i=0;i<dfaStateTransformTable[from].length;i++){
			if (dfaStateTransformTable[from][i] == to) {
				return (char) i;
			}
		}
    	return '0';
    }
}
