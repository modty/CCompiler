package top.modty.ccompiler.grammar;
import top.modty.ccompiler.commons.constants.CTokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class FirstFollowSelectSetBuilder {
	
    private HashMap<Integer, Symbols> symbolMap = new HashMap<Integer, Symbols>();
    public ArrayList<Symbols> symbolArray = new ArrayList<Symbols>();
    private boolean runFirstSetPass = true;
  	private boolean runFollowSetPass=true;
  	private boolean runSelectSetPass=true;
	public int[][] parseTable;
    public FirstFollowSelectSetBuilder() {
    	initProductions();
    }

    public boolean isSymbolNullable(int sym) {
    	Symbols symbol= symbolMap.get(sym);
    	if (symbol == null) {
    		return false;
    	}
    	return symbol.isNullable ? true : false;
    }
    
    private void initProductions() {
    	symbolMap = CGrammarInitializer.getInstance().getSymbolMap();
    	symbolArray = CGrammarInitializer.getInstance().getSymbolArray();
    }
	public void runFirstSets() {
		while (runFirstSetPass) {
			runFirstSetPass = false;

			Iterator<Symbols> it = symbolArray.iterator();
			while (it.hasNext()) {
				Symbols symbol = it.next();
				addSymbolFirstSet(symbol);
			}
		}

//    	printAllFirstSet();
//		System.out.println("============");
	}
	public void printAllFirstSet() {
		Iterator<Symbols> it = symbolArray.iterator();
		while (it.hasNext()) {
			Symbols sym = it.next();
			printFirstSet(sym);
		}
	}
	private void addSymbolFirstSet(Symbols symbol) {
		if (isSymbolTerminals(symbol.value) == true) {
			if (symbol.firstSet.contains(symbol.value) == false) {
				symbol.firstSet.add(symbol.value);
			}
			return;
		}

		for (int i = 0;  i < symbol.productions.size(); i++) {
			int[] rightSize = symbol.productions.get(i);
			if (rightSize.length == 0) {
				continue;
			}

			if (isSymbolTerminals(rightSize[0]) && symbol.firstSet.contains(rightSize[0]) == false) {
				runFirstSetPass = true;
				symbol.firstSet.add(rightSize[0]);
			}
			else if (isSymbolTerminals(rightSize[0]) == false) {
				//add first set of nullable
				int pos = 0;
				Symbols curSymbol = null;
				do {
					curSymbol = symbolMap.get(rightSize[pos]);
					if (symbol.firstSet.containsAll(curSymbol.firstSet) == false) {
						runFirstSetPass = true;

						for (int j = 0; j < curSymbol.firstSet.size(); j++) {
							if (symbol.firstSet.contains(curSymbol.firstSet.get(j)) == false) {
								symbol.firstSet.add(curSymbol.firstSet.get(j));
							}
						}//for (int j = 0; j < curSymbol.firstSet.size(); j++)

					}//if (symbol.firstSet.containsAll(curSymbol.firstSet) == false)

					pos++;
				}while(pos < rightSize.length && curSymbol.isNullable);
			} // else

		}//for (int i = 0; i < symbol.productions.size(); i++)
	}
	private void printFirstSet(Symbols symbol) {

		String s = CTokenType.getSymbolStr(symbol.value);
		s += "{ ";
		for (int i = 0; i < symbol.firstSet.size(); i++) {
			s += CTokenType.getSymbolStr(symbol.firstSet.get(i)) + " ";
		}
		s += " }";

		System.out.println(s);
		System.out.println("============");
	}
	public ArrayList<Integer> getFirstSet(int symbol) {
		Iterator<Symbols> it = symbolArray.iterator();
		while (it.hasNext()) {
			Symbols sym = it.next();
			if (sym.value == symbol) {
				return sym.firstSet;
			}
		}

		return null;

	}

	public void runFollowSets(){
		while (runFollowSetPass) {
			runFollowSetPass = false;
			Iterator<Symbols> it = symbolArray.iterator();
			while (it.hasNext()) {
				Symbols symbol = it.next();
				addSymbolFollowSet(symbol);
			}
		}
	}
	private void addSymbolFollowSet(Symbols symbol) {
		if (isSymbolTerminals(symbol.value) == true) {
			return;
		}

		for (int i = 0;  i < symbol.productions.size(); i++) {
			int[] rightSize = symbol.productions.get(i);

			/*
			 * s -> a' a1 a2 a3....an b
			 * a1 a2 a3...an are nullable, b is no nullable
			 * follow(a1) is union of first(a2) first(a3)... first(an) first(b)
			 * flllow(a2) is union of first(a3) first(a4) ... first(an) first(b)
			 * ...
			 */
			for (int j = 0; j < rightSize.length; j++) {
				Symbols current = symbolMap.get(rightSize[j]);
				if(isSymbolTerminals(current.value) == true) {
					continue;
				}

				for (int k = j + 1; k < rightSize.length; k++) {
					Symbols next = symbolMap.get(rightSize[k]);
					addSetToFollowSet(current, next.firstSet);
					if (next.isNullable == false) {
						break;
					}
				}
			}

			/*
			 * s -> ... a1 a2 a3 ... an
			 * a1 a2 a3 ... an are nullable no terminals
			 * everything in follow(s) is in follow(a1) follow(a2)...follow(an)
			 */
			int pos = rightSize.length - 1;
			while (pos >= 0) {
				Symbols current = symbolMap.get(rightSize[pos]);
				if (isSymbolTerminals(current.value) == false) {
					addSetToFollowSet(current, symbol.followSet);
				}
				if (isSymbolTerminals(current.value) == true && current.isNullable == false) {
					break;
				}
				pos--;
			}

		}

	}
	private void addSetToFollowSet(Symbols symbolBeAdded, ArrayList<Integer> set) {
		boolean add = false;
		if (symbolBeAdded.followSet.contains(set) == false) {
			for (int i = 0; i < set.size(); i++) {
				if (symbolBeAdded.followSet.contains(set.get(i)) == false) {
					symbolBeAdded.followSet.add(set.get(i));
					runFollowSetPass = true;
					add = true;
				}
			}
		}

		if (add) {
//			System.out.print("add symbol to followset:");
//			printFollowSet(symbolBeAdded);
		}
	}
	private void printFollowSet(Symbols symbol) {
		if (isSymbolTerminals(symbol.value) == true) {
			return;
		}

		String s = CTokenType.getSymbolStr(symbol.value);
		s += "{ ";
		for (int i = 0; i < symbol.followSet.size(); i++) {
			s += CTokenType.getSymbolStr(symbol.followSet.get(i)) + " ";
		}
		s += " }";

		System.out.println(s);
	}

	public void runSelectSets(){
		for(int i=0;i<symbolArray.size();i++){
			addSymbolSelectionSet(symbolArray.get(i),i);
		}
	}
	private void addSymbolSelectionSet(Symbols symbol,int index) {
		if (isSymbolTerminals(symbol.value) == true) {
			return;
		}

		boolean isNullableProduction = true;
		for (int i = 0;  i < symbol.productions.size(); i++) {
			int[] rightSize = symbol.productions.get(i);
			ArrayList<Integer> selection = new ArrayList<Integer>();

			for (int j = 0; j < rightSize.length; j++) {
				Symbols next = symbolMap.get(rightSize[j]);
				if (next.isNullable == false) {
					isNullableProduction = false;
					addSetToSelectionSet(selection, next.firstSet);
					break;
				}

				addSetToSelectionSet(selection, next.firstSet);
			}

			if (isNullableProduction) {
				addSetToSelectionSet(selection, symbol.followSet);
			}

			symbol.selectionSet.put((index+1)*100+i,selection);
			isNullableProduction = true;
		}

	}
	private void addSetToSelectionSet(ArrayList<Integer> selectionSet, ArrayList<Integer> set) {
		for (int i = 0; i < set.size(); i++) {
			if (selectionSet.contains(set.get(i)) == false) {
				selectionSet.add(set.get(i));
			}
		}
	}

    private boolean isSymbolTerminals(int symbol) {
    	return CTokenType.isTerminal(symbol);
    }


    public void runParseSets(){
    	initializeParseTable();
    	setParsetTable();
    	printParseTable();
	}
	private void initializeParseTable() {
		parseTable = new int[CTokenType.LAST_NON_TERMINAL_INDEX+1][CTokenType.LAST_TERMINAL_INDEX-CTokenType.FIRST_TERMINAL_INDEX];
		for (int i = 0; i < CTokenType.LAST_NON_TERMINAL_INDEX+1; i++) {
			for (int j = 0; j < CTokenType.LAST_TERMINAL_INDEX-CTokenType.FIRST_TERMINAL_INDEX; j++) {
				parseTable[i][j] = -1;
			}
		}
	}

	private void setParsetTable() {
		Iterator it = symbolArray.iterator();
		while (it.hasNext()) {
			Symbols symbol = (Symbols) it.next();
			if (isSymbolTerminals(symbol.value)) {
				continue;
			}
			for (int i :symbol.selectionSet.keySet()) {
				ArrayList<Integer> selection = symbol.selectionSet.get(i);
				for (int j = 0; j < selection.size(); j++) {
					parseTable[symbol.value-CTokenType.FIRST_NON_TERMINAL_INDEX][selection.get(j) -CTokenType.FIRST_TERMINAL_INDEX] = i;
				}
			}
		}
	}

	private void printParseTable() {
		for (int i = 0; i < CTokenType.LAST_NON_TERMINAL_INDEX+1; i++) {
			for (int j = 0; j < CTokenType.LAST_TERMINAL_INDEX-CTokenType.FIRST_TERMINAL_INDEX; j++) {
				System.out.print(parseTable[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	public HashMap<String, ArrayList<String>> getFirstSympol(){
		HashMap<String,ArrayList<String>> firstSympol=new HashMap<>();
		for(Symbols symbols:symbolArray){
			ArrayList<String> temp=new ArrayList<>();
			firstSympol.put(CTokenType.getSymbolStr(symbols.value),temp);
			for(Integer i: symbols.firstSet){
				temp.add(CTokenType.getSymbolStr(i));
			}
		}
		return firstSympol;
	}
	public HashMap<String, ArrayList<String>> getFollowSympol(){
		HashMap<String,ArrayList<String>> firstSympol=new HashMap<>();
		for(Symbols symbols:symbolArray){
			ArrayList<String> temp=new ArrayList<>();
			firstSympol.put(CTokenType.getSymbolStr(symbols.value),temp);
			for(Integer i: symbols.followSet){
				temp.add(CTokenType.getSymbolStr(i));
			}
		}
		return firstSympol;
	}
	public HashMap<String, HashMap<String,ArrayList<String>>> getSelectSympol(){
		HashMap<String, HashMap<String,ArrayList<String>>> select=new HashMap<>();
		for(int i=0;i<parseTable.length;i++){
			for(int j=0;j<parseTable.length;j++){
				String terminal=CTokenType.getSymbolStr(j+CTokenType.FIRST_TERMINAL_INDEX);
				String nonTerminnal=CTokenType.getSymbolStr(i);
				if(!select.containsKey(nonTerminnal)){
					select.put(nonTerminnal,new HashMap<>());

				}
				if(!select.get(nonTerminnal).containsKey(terminal)){
					select.get(nonTerminnal).put(terminal,new ArrayList<>());
				}
				int action=parseTable[i][j];
				if(action!=-1){
					System.out.println(action);
					int preId=action/100-1;
					int lastId=action%100;
					int[] grammers=symbolArray.get(preId).productions.get(lastId);
					for(int i1:grammers){
						select.get(nonTerminnal).get(terminal).add(CTokenType.getSymbolStr(i1));
					}
				}
			}
		}
		return select;
	}
}
