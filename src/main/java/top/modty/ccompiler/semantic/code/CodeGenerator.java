package top.modty.ccompiler.semantic.code;
import top.modty.ccompiler.commons.constants.Directive;
import top.modty.ccompiler.commons.constants.Instruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;


/**
 * @author 点木
 * @date 2020-05-05 16:05
 * @mes  该类先创建一个以.j结尾的文件，然后通过PrintWriter 把内容写入到该文件中，
 * emit开头的函数把相关的java汇编指令写入文件。
 */
public class CodeGenerator {
    private  PrintWriter assemblyFile;
    private  int instructionCount = 0;
    private  boolean buffered = false;
    private  boolean classDefine = false;
    private  String bufferedContent = "";
    private  String classDefinition = "";
    protected static String programName = "CSourceToJava";
    private HashMap<String, String>nameToDeclaration = new HashMap<String, String>();
    
    public void setNameAndDeclaration(String name, String declaration) {
    	nameToDeclaration.put(name, declaration);
    }
    
    public String getDeclarationByName(String name) {
    	return nameToDeclaration.get(name);
    }
    
    
    public void setInstructionBuffered(boolean isBuffer) {
	   this.buffered = isBuffer;
    }
    
    public void setClassDefinition(boolean isClass) {
    	this.classDefine = isClass;
    }
    
    public CodeGenerator() {
    	
        String assemblyFileName = programName + ".j";
    	
    	try {
			assemblyFile = new PrintWriter(new PrintStream(new 
					File(assemblyFileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void emitString(String s) {
    	if (buffered) {
    		bufferedContent += s + "\n";
    		return;
    	} 
    	
    	if (classDefine) {
    		classDefinition += s + "\n";
    		return;
    	} 
    	
    	assemblyFile.print(s);
    	assemblyFile.flush();
    	
    }
    
    protected void emitBufferedContent() {
    	if (bufferedContent.isEmpty() == false) {
    		assemblyFile.println(bufferedContent);
    		assemblyFile.flush();
    	}
    }
    
    protected void emitClassDefinition() {
    	if (classDefinition.isEmpty() == false) {
    		assemblyFile.println(classDefinition);
    		assemblyFile.flush();
    	}
    }
    
   
    
    public void emitDirective(Directive directive) {
    	if (buffered) {
    		bufferedContent += directive.toString() + "\n";
    		return;
    	} 
    	
    	if (classDefine) {
    		classDefinition += directive.toString() + "\n";
    		return;
    	} 
    	
    	assemblyFile.println(directive.toString());
    	assemblyFile.flush();
    	++instructionCount;
    }
    
    public void emitDirective(Directive directive, String operand) {
    	if (buffered) {
    		bufferedContent += directive.toString() + " " + operand + "\n";
    		return;
    	} 
    	
    	if (classDefine) {
    		classDefinition += directive.toString() + " " + operand + "\n";
    		return;
    	}
    	
    	assemblyFile.println(directive.toString() + " " + operand);
    	assemblyFile.flush();
    	++instructionCount;
    }
    
    public void emitDirective(Directive directive, int operand) {
    	if (buffered) {
    		bufferedContent += directive.toString() + " " + operand + "\n";
    		return;
    	}
    	
    	if (classDefine) {
    		classDefinition += directive.toString() + " " + operand + "\n";
    		return;
    	}
    	
    	assemblyFile.println(directive.toString() + " " + operand);
    	++instructionCount;
    }
    
    public void emitDirective(Directive directive, String operand1, String operand2) {
    	if (buffered) {
    		bufferedContent += directive.toString() + " " + operand1 + " " + operand2 + "\n";
    		return;
    	}
    	
    	if (classDefine) {
    		classDefinition += directive.toString() + " " + operand1 + " " + operand2 + "\n";
    		return;
    	}
    	
    	assemblyFile.println(directive.toString() + " " + operand1 + " " + operand2);
    	++instructionCount;
    }
    
    public void emitDirective(Directive directive, String operand1, String operand2, String operand3) {
    	if (buffered) {
    		bufferedContent += directive.toString() + " " + operand1 + " " + operand2 + " " + operand3 + "\n";
    		return;
    	}
    	
    	if (classDefine) {
    		classDefinition += directive.toString() + " " + operand1 + " " + operand2 + " " + operand3 + "\n";
    		return;
    	}
    	
    	assemblyFile.println(directive.toString() + " " + operand1 + " " + operand2 + " " + operand3);
    	++instructionCount;
    }
    
    public void emit(Instruction opcode) {
    	if (buffered) {
    		bufferedContent += "\t" + opcode.toString() + "\n";
    		return;
    	}
    	
    	if (classDefine) {
    		classDefinition += "\t" + opcode.toString() + "\n";
    		return;
    	}
    	
    	assemblyFile.println("\t" + opcode.toString());
    	assemblyFile.flush();
    	++instructionCount;
    }
    
    public void emit(Instruction opcode, String operand) {
    	if (buffered) {
    		bufferedContent += "\t" + opcode.toString() + "\t" + operand + "\n";
    		return;
    	}
    	
    	if (classDefine) {
    		classDefinition += "\t" + opcode.toString() + "\t" + operand + "\n";
    		return;
    	}
    	
    	assemblyFile.println("\t" + opcode.toString() + "\t" + operand);
    	assemblyFile.flush();
    	++instructionCount;
    }
    
    public void emitBlankLine() {
    	if (buffered) {
    		bufferedContent += "\n";
    		return;
    	}
    	
    	if (classDefine) {
    		classDefinition += "\n";
    		return;
    	}
    	
    	assemblyFile.println();
    	assemblyFile.flush();
    }
    
    public void finish() {
    	assemblyFile.close();
    }
    
    
}
