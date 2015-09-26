package serban.stoenescu.operations;


import java.util.Random;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serban
 */
public class OperationFactory
{
 private int numberOfOperationTypes = 5;
 private int level = 1;
 private static OperationFactory instance;
 
 public void setLevel(int level) {this.level=level;}
 
 public static OperationFactory getInstance()
    {
     if(instance==null)
         instance=new OperationFactory();
     return instance;
 }

 private int randomDivisor(int x)
 {
  int i = 1;
  Vector<Integer> v=new Vector<Integer>();
  if(x==0) return 0;
  for(i=1;i<=x;i++)
  {
      if(x%i==0)
        v.add(i);
  }
  Random r=new Random();
  int index=r.nextInt(v.size());
  return v.get(index);
 }
 public Operation[] generateOperationPair()
 {
  Operation[] ops=new Operation[2];
  int result=(new Random()).nextInt(10*level);
  ops[0]=generateOperation(result);
  ops[1]=generateOperation(result);
  return ops;
 }
 public Operation generateOperation(int result)//set result to -1 for undefined, set to !=-1 to some predefined result
 {
  int type;
  int op1,op2;
  int min;

  if(level<numberOfOperationTypes) min=level;
  else min=numberOfOperationTypes;
  
  Operation operation=new SingleNumberOperation(-1,0,0);
  Random r=new Random();

  
  if(result==-1)
    result=r.nextInt(5+level);
  type=r.nextInt(min);
  switch(type)
  {
      case 0:
          operation=new SingleNumberOperation(result,0,level);
      break;

      case 1:
          if(result==0)
          {
              op1=op2=0;
          }
          else
          {
            op1=r.nextInt(result);
            op2=result-op1;
          }
          operation=new Addition(op1,op2,level);
      break;

      case 2:
          //result=(int)(result*0.8);

          if(result==-1)
            result=r.nextInt(5+level);
          if(result==0)
          {
              op1=op2=0;
          }
          else
          {
            op1=r.nextInt(result);
            op2=op1+result;
          }
          operation=new Subtraction(op2,op1,level);
      break;

      case 3:

          if(result==-1)
            result=r.nextInt(5+level);
          op1=randomDivisor(result);
          if(op1==0) op2=0;
          else op2=result/op1;
          operation=new Multiplication(op1,op2,level);
      break;

      case 4:
          if(result==-1)
            result=r.nextInt(level);
          if(result==0) result=1;
          int minim,maxim=7*level;
          if(maxim<result) maxim=3*result;
          if(result<level) minim=result;
          else minim=level;
          do
          {
              op1=r.nextInt(minim);
              if(op1==0) op1=1;
              op2=result*op1;
          }while(op2>maxim);
          operation=new Division(op2,op1,level);
      break;

      default:
          System.out.println("Error! What fucking operation is this shit?" +type);
      break;
  }
  return operation;
 }
}
