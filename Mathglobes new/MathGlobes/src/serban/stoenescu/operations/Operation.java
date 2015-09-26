package serban.stoenescu.operations;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serban
 */
public abstract class Operation
{
 protected int op1,op2;
 protected int level;
 public Operation(int op1,int op2,int level)
 {
     this.op1=op1;
     this.op2=op2;
     this.level=level;
 }
 public abstract int getResult();
 public abstract int getScore();
 public abstract String getSymbol();
 public abstract void operationSolved();
 @Override
 public String toString()
 {
  return op1+" "+getSymbol()+" "+op2;
 }
}
