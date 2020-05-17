package top.modty.ccompiler.LL1;

import java.util.ArrayList;

/**
 * @author 点木
 * @date 2020-05-16 21:23
 * @mes
 */
public class LProduction{
    public String left;
    public String[] right;
    // 初始化select集
    ArrayList<String> select = new ArrayList<String>();
    public LProduction(String left, String[] right){
        this.left = left;
        this.right = right;
    }

    public ArrayList<String> returnRights(){
        ArrayList<String> arrayList=new ArrayList<>();
        for(int i=0;i<right.length;i++)
            arrayList.add(right[i]);
        return arrayList;
    }

    public String returnLeft(){
        return left;
    }
}
