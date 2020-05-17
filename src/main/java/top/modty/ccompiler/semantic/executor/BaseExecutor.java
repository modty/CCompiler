 package top.modty.ccompiler.semantic.executor;

 import top.modty.ccompiler.commons.constants.ICodeKey;
 import top.modty.ccompiler.semantic.code.ProgramGenerator;
 import top.modty.ccompiler.semantic.inter.Executor;
 import top.modty.ccompiler.semantic.inter.ICodeNode;
 import top.modty.ccompiler.semantic.inter.IExecutorBrocaster;

 /**
  * @author 点木
  * @date 2020-05-05 16:05
  * @mes 基类结点解释器
  */
 public abstract class BaseExecutor implements Executor {
     // 是否接着执行
     private static boolean continueExecute = true;
     private static Object  returnObj = null;
     IExecutorBrocaster executorBrocaster = null;
     ProgramGenerator generator;
     public static boolean inIfElseStatement = false;
     public static boolean isCompileMode = false;
     //change here
     public static boolean resultOnStack = false;
     //change here
     public static String funcName = "";



     public BaseExecutor() {
         executorBrocaster = ExecutorBrocasterImpl.getInstance();
         generator = ProgramGenerator.getInstance();
     }

     protected void setReturnObj(Object obj) {
         this.returnObj = obj;
     }

     protected Object getReturnObj() {
         return returnObj;
     }

     protected void clearReturnObj() {
         this.returnObj = null;
     }

     protected void isContinueExecution(boolean execute) {
         this.continueExecute = execute;
     }

     /**
      * @author 点木
      * @date 2020/5/17
      * @return
      * @params 结点
      * @mes 遍历到执行树上的某个节点时，要根据该节点进行相应的代码执行操作，
      * 在此之前，如果该节点有孩子的话，需要先执行他的孩子，
      * 它收到所以孩子的执行结果后，再根据这些结果进行相应的操作。
     */
     protected void executeChildren(ICodeNode root) {
         ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
         root.reverseChildren();

         int i = 0;
         while (i < root.getChildren().size()) {

             if (continueExecute != true) {
                 break;
             }

             ICodeNode child = root.getChildren().get(i);


             executorBrocaster.brocastBeforeExecution(child);

             Executor executor = factory.getExecutor(child);
             if (executor != null) {
                 executor.Execute(child);
             }
             else {
                 System.err.println("Not suitable Executor found, node is: " + child.toString());
             }

             executorBrocaster.brocastAfterExecution(child);

             i++;
         }
     }


     /**
      * @author 点木
      * @date 2020/5/17 
      * @return
      * @params root:父结点，child:子结点
      * @mes 把孩子节点的所有相关信息拷贝到父节点。
     */
     protected void copyChild(ICodeNode root, ICodeNode child) {
         root.setAttribute(ICodeKey.SYMBOL, child.getAttribute(ICodeKey.SYMBOL));
         root.setAttribute(ICodeKey.VALUE, child.getAttribute(ICodeKey.VALUE));
         root.setAttribute(ICodeKey.TEXT, child.getAttribute(ICodeKey.TEXT));
     }


     protected ICodeNode executeChild(ICodeNode root, int childIdx) {
         //把孩子链表的倒转放入到节点本身，减少逻辑耦合性
         root.reverseChildren();
         ICodeNode child;
         ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
         child = (ICodeNode)root.getChildren().get(childIdx);
         Executor executor = factory.getExecutor(child);
         ICodeNode res = (ICodeNode)executor.Execute(child);

         return res;
     }
 }
