package top.modty.ccompiler.dfaNfa;


import java.util.*;


public class MinimizeDFA {
	HashMap<String, List<HashMap<String, Object>>> response;
	private DfaConstructor dfaCounstructor = null;
	private DfaGroupManager groupManager = new DfaGroupManager();
	private static final int ASCII_NUM = 128;
	private int[][] dfaTransTable = null;
	int[][] minDfa = null;
	private List<Dfa> dfaList = null;
	private DfaGroup newGroup = null;
	private HashMap<Integer,String> minDfaMap;
	private boolean addNewGroup = false;
	private static final int STATE_FAILURE = -1;
	
    public MinimizeDFA(DfaConstructor theConstructor) {
		minDfaMap=new HashMap<>();
		response=new HashMap<>();
		response.put("edg",new ArrayList<>());
		response.put("state",new ArrayList<>());
		this.dfaCounstructor = theConstructor;
        dfaList = dfaCounstructor.getDfaList();
        dfaTransTable = dfaCounstructor.getDfaTransTable();
    }
    
    public HashMap<String, List<HashMap<String, Object>>> minimize() {
    	DfaGroup.ends=new HashSet();
    	/*
    	 * 生成两个分区，分别将非接收状态节点和接收状态节点放入两个分区
    	 */
    	addNoAcceptingDfaToGroup();
    	addAcceptingDfaToGroup();
    	
    	/*
    	 * 根据输入对每个分区进行分割，一点有新的分区生成，那么就必须对所以分区再进行分割
    	 * 因为新分区产生后，原来不可分割的分区可能就可以分割了
    	 */
    	
    	do {
    		addNewGroup = false;
    		doGroupSeperationOnNumber();
    		doGroupSeperationOnCharacter();
    	}while (addNewGroup);
		minDfaMap=DfaGroup.map;
		createMiniDfaTransTable();
    	printMiniDfaTable();
    	return response;
    }
    
    private void createMiniDfaTransTable() {
    	/*
    	 * 把点与点的跳转关系转换为分区与分区的转移关系
    	 */
    	initMiniDfaTransTable();
    	Iterator it = dfaList.iterator();
    	while (it.hasNext()) {
    		/*
    		 * 先取出原来每一个Dfa节点，查找它对应某个ascii码输入时的跳转节点
    		 */
    		int from = ((Dfa)it.next()).stateNum;
    		for (int i = 0; i < ASCII_NUM; i++) {
    			if (dfaTransTable[from][i] != STATE_FAILURE) {
    				int to = dfaTransTable[from][i];
    				/*
    				 * 找到两个节点对应的分区
    				 */
    				DfaGroup fromGroup = groupManager.getContainingGroup(from);
    				DfaGroup toGroup = groupManager.getContainingGroup(to);
    				/*
    				 * 把点和点的跳转关系转变为分区间的跳转关系
    				 */
    				minDfa[fromGroup.groupNum()][i] = toGroup.groupNum();
    			}
    		}
    	}
    }
    
    private void printMiniDfaTable() {
    	for(Integer key :minDfaMap.keySet()){
			HashMap<String,Object> stateTemp=new HashMap<>();
			stateTemp.put("id",key);
			stateTemp.put("label",minDfaMap.get(key));
			if(DfaGroup.ends.contains(key)){
				stateTemp.put("class","type-fail");
			}else {
				stateTemp.put("class","type-suss");
			}
			response.get("state").add(stateTemp);
		}
    	DfaGroup.map.clear();
    	DfaGroup.GROUP_COUNT=0;
    	for (int i = 0; i < groupManager.size(); i++)
    		for (int j = 0; j < groupManager.size(); j++) {
				HashMap<String,Object> edgTemp=new HashMap<>();
				edgTemp.put("start",i);
				edgTemp.put("end",j);
    		    if (isOnNumberClass(i,j)) {
    		    	System.out.println("from " + i + " to " + j + " on D");
					edgTemp.put("label","D");
					response.get("edg").add(edgTemp);
				}else {
					char c=isOnDot(i,j);
					if(c!='0'){
						System.out.println("From state " + i + " to state " + j + " on "+c);
						edgTemp.put("label",c);
						response.get("edg").add(edgTemp);
					}
				}
    		}
    }
    
    private boolean isOnNumberClass(int from, int to) {
    	char c = '0';
    	for (c = '0'; c <= '9'; c++) {
    		if (minDfa[from][c] != to) {
    			return false;
    		}
    	}
    	
    	return true;
    }

	private char isOnDot(int from, int to) {
		for(int i=0;i<minDfa[from].length;i++){
			if (minDfa[from][i] == to) {
				return (char) i;
			}
		}
		return '0';
	}

    
    private void initMiniDfaTransTable() {
    	minDfa = new int[groupManager.size()][ASCII_NUM];
        for (int i = 0; i <groupManager.size(); i++)
        	for (int j = 0; j < ASCII_NUM; j++) {
        		minDfa[i][j] = STATE_FAILURE;
        	}
    }
    
    private void doGroupSeperationOnNumber() {
    	//先对输入是数字字符时进行分割
    	for (int i = 0; i < groupManager.size(); i++) {
    		int dfaCount = 1;
    		newGroup = null;
    		DfaGroup group = groupManager.get(i);
    		
    		System.out.println("Handle seperation on group: ");
			group.printGroup();
    		Dfa first = group.get(0);
    		Dfa next = group.get(dfaCount);

    		//当一个分区包含不止一个节点时，next不会是空
    		while (next != null) {
    			for (char c = '0'; c < '0' + 9; c++) {
    				if (doGroupSeprationOnInput(group, first, next, c) == true) {
    					addNewGroup = true;
    					break;
    				}
    			}
    			
    		    dfaCount++;
    		    next = group.get(dfaCount);
    		}
    		group.commitRemove();
    	}
	}
    
    private void doGroupSeperationOnCharacter() {
    	//对输入是非数字字符时进行分割
    	for (int i = 0; i < groupManager.size(); i++) {
    		int dfaCount = 1;
    		newGroup = null;
    		DfaGroup group = groupManager.get(i);
    		
    		System.out.println("Handle seperation on group: ");
    		group.printGroup();
    		
    		Dfa first = group.get(0);
    		Dfa next = group.get(dfaCount);
    		//当一个分区包含不止一个节点时，next不会是空
    		while (next != null) {
    			for (char c = 0; c < ASCII_NUM; c++) {
    				if (Character.isDigit(c) == false && doGroupSeprationOnInput(group, first, next, c) == true) {
    					addNewGroup = true;
    					break;
    				}
    			}
    			
    		    dfaCount++;
    		    next = group.get(dfaCount);
    		}
    		
    		group.commitRemove();
    	}
    }
    
    private boolean doGroupSeprationOnInput(DfaGroup group, Dfa first, 
    		Dfa next , char c) {
    	/*
		 * 如果两个DFA节点跳转后的节点不在同一个分区，那么第二个节点必须从当前分区分割出去
		 */
    	
    	int goto_first = dfaTransTable[first.stateNum][c];
		int goto_next = dfaTransTable[next.stateNum][c];
		
		if (groupManager.getContainingGroup(goto_first) != 
				groupManager.getContainingGroup(goto_next)) {
			if (newGroup == null) {
				newGroup = groupManager.createNewGroup();
			}

			group.tobeRemove(next);
			newGroup.add(next);
			
			System.out.println("Dfa:" + first.stateNum + " and Dfa:" +
			next.stateNum + " jump to different group on input char " + c);
			
			System.out.println("remove Dfa:" + next.stateNum + " from group:" + group.groupNum() 
					+ " and add it to group:" + newGroup.groupNum());
			return true;
		}
	    
		return false;
    }
    
    private void addNoAcceptingDfaToGroup() {
    	Iterator it = dfaList.iterator();
    	DfaGroup group = groupManager.createNewGroup();
    	
    	while (it.hasNext()) {
    		Dfa dfa = (Dfa)it.next();
    		if (dfa.accepted == false) {
    			group.add(dfa);
    		}
    	}
    	
    	group.printGroup();
    }
    
    private void addAcceptingDfaToGroup() {
    	Iterator it = dfaList.iterator();
    	DfaGroup group = groupManager.createNewGroup();
    	
    	while (it.hasNext()) {
    		Dfa dfa = (Dfa)it.next();
    		if (dfa.accepted == true) {
    			group.add(dfa);
    		}
    	}
    	
    	group.printGroup();
    	
    	/*
    	 * 把集合 3,4,6,7 排列成 4,6,7,3
    	 * 这是为了调试演示需要，删除完全不会影响程序逻辑
    	 * 阅读代码时忽略下面这几句
    	 */
    	Dfa dfa = group.dfaGroup.get(0);
    	group.dfaGroup.remove(0);
    	group.dfaGroup.add(dfa);
    	
    }
}
