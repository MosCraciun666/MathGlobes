package serban.stoenescu.operations;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serban
 */
public class SingleNumberOperation extends Operation
{
    public SingleNumberOperation(int op1,int op2,int level)
    {
        super(op1,op2,level);
    }
    @Override
    public int getResult()
    {
        return op1;
    }
    @Override
    public int getScore()
    {
        return 1;
    }

    @Override
    public String getSymbol()
    {
        return ".";
    }

    @Override
    public String toString()
    {
        return op1+"";
    }

    @Override
    public void operationSolved()
    {
    }
}
