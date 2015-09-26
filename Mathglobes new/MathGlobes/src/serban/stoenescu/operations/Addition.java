package serban.stoenescu.operations;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serban
 */
public class Addition extends Operation
{
    public Addition(int op1,int op2,int level)
    {
        super(op1,op2,level);
    }
    @Override
    public int getResult()
    {
        return op1+op2;
    }

    @Override
    public int getScore()
    {
        return 2+level;
    }

    @Override
    public String getSymbol()
    {
        return "+";
    }

    @Override
    public void operationSolved() {
    }
}
