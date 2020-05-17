package top.modty.ccompiler.LL1;

import top.modty.ccompiler.commons.Node;
import top.modty.ccompiler.commons.Nodes;
import top.modty.ccompiler.commons.Word;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.*;


public class TextParse {
	HashMap<String, String> predictmap;
	ArrayList<Word> input_cache;
	ArrayList<Node> deduce_str;
	HashMap<String, List<HashMap<String, Object>>> response;
	List<String> terminals;
	ArrayList<String[]> actions;

	private int max=50000;
	public TextParse(ArrayList<Word> input_cache){
		predictmap = new HashMap<String, String>();
		this.input_cache = input_cache;
		deduce_str = new ArrayList<>();
		response=new HashMap<>();
		response.put("edg",new ArrayList<>());
		response.put("state",new ArrayList<>());
		terminals=LL1Productions.getInstance().terminals;
		actions=new ArrayList<>();
		getPredictMap();
	}

	public ArrayList<String[]> getActions() {
		return actions;
	}

	// 句法分析
	public HashMap<String, List<HashMap<String, Object>>> Parsing(){
		// 初始符号压入栈
		deduce_str.add(new Node(0,"S"));
		String right;
		String leftandinput;
		String process="";
		addState(0,"S","type-ready");
		int inputCacheIndex=0;
		while (deduce_str.size()>0) {
			Node currentNode=deduce_str.get(deduce_str.size()-1);
			if(inputCacheIndex>=input_cache.size()){
				while (deduce_str.size()>0){
					process = "";
					for (int i=deduce_str.size()-1;i>-1;i--) {
						process = process+deduce_str.get(i).value+" ";
					}
					actions.add(new String[]{currentNode.value+"-->"+"$",process});
					System.out.println(process+"::"+currentNode.value+"-->"+"$");
					addState(max,"$","type-fail");
					addEdg(currentNode.id,max);
					max--;
					deduce_str.remove(deduce_str.size()-1);
				}
				break;
			}
			// 输入缓冲区与推导符号串第一个字符相等的话，删掉
			if(input_cache.get(inputCacheIndex).getWord().toUpperCase().equals(currentNode.value)){
				process = "";
				for (int i=deduce_str.size()-1;i>-1;i--) {
					process = process+deduce_str.get(i).value+" ";
				}
				addState(response.get("state").size()+1,input_cache.get(inputCacheIndex).getMes(),"type-suss");
				addEdg(currentNode.id,response.get("state").size());
				System.out.println(process+"::"+currentNode.value);
				actions.add(new String[]{currentNode.value,process});
				deduce_str.remove(deduce_str.size()-1);
				inputCacheIndex++;
			}else {
				// 匹配字符
				leftandinput = currentNode.value+"-"+input_cache.get(inputCacheIndex).getWord().toUpperCase();
				// 能够找到匹配的
				if((right=predictmap.get(leftandinput))!=null){
					right=right.trim();
					// 输出产生式和推导过程
					process = "";
					for (int i=deduce_str.size()-1;i>-1;i--) {
						process = process+deduce_str.get(i).value+" ";
					}
					System.out.println(process+"::"+currentNode.value+"--->"+right);
					actions.add(new String[]{currentNode.value+"-->"+right,process});
					// 删掉产生的字符，压入堆栈
					deduce_str.remove(deduce_str.size()-1);
					if(right.equals("$")){
						addState(response.get("state").size()+1,"$","type-fail");
						addEdg(currentNode.id,response.get("state").size());
					}
					else {
						String[] arg = right.trim().split(" ");
						for(int i=arg.length-1;i>-1;i--){
							// 反向压入堆栈
							deduce_str.add(new Node(response.get("state").size()+1,arg[i]));
							addState(response.get("state").size()+1,arg[i],"type-ready");
							addEdg(currentNode.id,response.get("state").size());
						}
					}
				}
				// 否则的话报错
				else {
					// 重新书写process
					process="";
					for (int i=deduce_str.size()-1;i>-1;i--) {
						process = process+deduce_str.get(i)+" ";
					}
					System.out.println(process+"   "+"ERROR!  无法识别的字符"+input_cache.get(inputCacheIndex)+"产生式"+leftandinput);
					actions.add(new String[]{"ERROR!  无法识别的字符"+input_cache.get(inputCacheIndex)+"产生式"+leftandinput,process});
					inputCacheIndex++;
				}
			}
		}
		return response;
	}
	// 获得预测分析表中的产生式以及对应的select集
	// 存储方式为键值对的形式
	public void getPredictMap(){
		HashMap<String,HashMap<String, ArrayList<String>>> predict=LL1Productions.getInstance().predict;
		for(String left:predict.keySet()){
			HashMap<String,ArrayList<String>>temp=predict.get(left);
			for(String symbol:temp.keySet()){
				String right="";
				for(String s:temp.get(symbol)){
					right+=" "+s;
				}
				predictmap.put(left+"-"+symbol,right);
			}
		}
	}
	private void addEdg(int start,int end){
		HashMap<String,Object> temp=new HashMap<>();
		temp.put("start",start);
		temp.put("end",end);
		temp.put("option",new HashMap<>());
		response.get("edg").add(temp);
	}
	private void addState(int id,String label,String cls){
		HashMap<String,Object> temp=new HashMap<>();
		temp.put("id",id);
		temp.put("label",label);
		temp.put("class",cls);
		response.get("state").add(temp);
	}
	class Node{
		public int id;
		public String value;

		public Node(int id, String value) {
			this.id = id;
			this.value = value;
		}
	}
}