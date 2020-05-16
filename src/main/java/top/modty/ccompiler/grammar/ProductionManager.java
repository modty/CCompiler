package top.modty.ccompiler.grammar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class ProductionManager {
	
	private static ProductionManager self = null;
	public int productionNum=0;
	public FirstFollowSelectSetBuilder firstFollowSelectSetBuilder = new FirstFollowSelectSetBuilder();
	
    private HashMap<Integer, ArrayList<Production>> productionMap = new HashMap<Integer, ArrayList<Production>>();


	public HashMap<Integer, ArrayList<Production>> getProductionMap() {
		return productionMap;
	}

	public static ProductionManager getProductionManager() {
    	if (self == null) {
    		self = new ProductionManager();
    	}
    	
    	return self;
    }
    
    public void initProductions() {
    	CGrammarInitializer cGrammarInstance =  CGrammarInitializer.getInstance();
    	
    	productionMap = cGrammarInstance.getProductionMap();
    	productionNum=cGrammarInstance.productionNum;
    		
    }
    
    public void runFirstFollowSelectSetBuilder() {
    	firstFollowSelectSetBuilder.runFirstSets();
    	firstFollowSelectSetBuilder.runFollowSets();
    	// 设定文法不适合做LL1
//    	firstFollowSelectSetBuilder.runSelectSets();
//		firstFollowSelectSetBuilder.runParseSets();
    }
    public void runFollowSetAlogorithm(){

	}
	public void runSelectSetAlogorithm(){}
    public FirstFollowSelectSetBuilder getFirstFollowSelectSetBuilder() {
    	return firstFollowSelectSetBuilder;
    }
    
    public void printAllProductions() {
    	for (Entry<Integer, ArrayList<Production>> entry : productionMap.entrySet()) {
    		ArrayList<Production> list = entry.getValue();
    		for (int i = 0; i < list.size(); i++) {
    			list.get(i).print();
    			System.out.print("\n");
    		}
    	}
    }
    
    
    public ArrayList<Production> getProduction(int left) {
    	return productionMap.get(left);
    }
    
    public Production getProductionByIndex(int index) {
    	
    	for (Entry<Integer, ArrayList<Production>> item : productionMap.entrySet()) {
    		ArrayList<Production> productionList = item.getValue();
    		for (int i = 0; i < productionList.size(); i++) {
    			if (productionList.get(i).getProductionNum() == index) {
    				return productionList.get(i);
    			}
    		}
    	}
    	
    	return null;
    }
    
    private ProductionManager() {
    	
    }
}
