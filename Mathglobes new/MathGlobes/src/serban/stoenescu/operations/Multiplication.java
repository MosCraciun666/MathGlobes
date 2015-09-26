package serban.stoenescu.operations;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serban
 */
public class Multiplication extends Operation
{
    public Multiplication(int op1,int op2,int level)
    {
        super(op1,op2,level);
    }
    @Override
    public int getResult()
    {
        return op1*op2;
    }

    @Override
    public int getScore()
    {
        return 3+level;
    }
    public String getSymbol(){return "x";}

    @Override
    public void operationSolved()
    {
    }
}
