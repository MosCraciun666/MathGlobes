/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serban.stoenescu.mathcrap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.Random;
import java.util.Vector;
import serban.stoenescu.graphics.OperationGraphic;
import serban.stoenescu.operations.Operation;
import serban.stoenescu.operations.OperationFactory;

/**
 *
 * @author Serban
 */
public class OperationMatrix
{
 private  int sizeX,sizeY;
 private int SPACE = 105;//!ATENTIE!
 private OperationGraphic[][] ops;
 private OperationGraphic draggedGraphic = null;
 private MyCustomDrawable mcd;
 private static Bitmap operationBitmap;
 private static Bitmap operationBitmapSelected;
 private static float SCALE = 0.75f;
 public static float getScale(){return SCALE;}
 public static void setScale(float scale){SCALE=scale;}

 public static void setOperationBitmap(Bitmap b){operationBitmap=b;}
 public static void setOperationBitmapSelected(Bitmap b){operationBitmapSelected=b;}

 private Paint linePaint;

 public OperationMatrix(MyCustomDrawable mcd,int sx,int sy)
 {
  int i,j;
  this.mcd=mcd;
  sizeX=sx;
  sizeY=sy;
  ops=new OperationGraphic[sizeX][sizeY];
  linePaint=new Paint();
  linePaint.setColor(Color.WHITE);
  for(j=0;j<1;j++)
  for(i=0;i<sizeX;i++)
  {
      ops[j][i]=new OperationGraphic(i*SPACE,j*SPACE);
      ops[j][i].setOperation((OperationFactory.getInstance()).generateOperation(-1));
  }
 }

 public boolean fullRow()
 {
  int i,j,count=0;
  for(i=0;i<sizeX;i++)
  {
      count=0;
      for(j=0;j<sizeY;j++)
      {
        if(ops[j][i]!=null)
            count++;
      }
      if(count==sizeY)
          return true;
  }
  return false;
 }

 /*eroarea a fost la coloana 1, i=1;*/
 public synchronized void addOperationToColumn(Operation newOp,int column)
 {
  int i;
  try
  {
      for(i=sizeY-1;i>=1;i--)
      {
          ops[i][column]=ops[i-1][column];
      }
      ops[0][column]=new OperationGraphic(column*SPACE,0*SPACE);
      ops[0][column].setOperation(newOp);
  }
  catch(Exception e)
  {
      mcd.setLog(e.getMessage()+" of type "+e.getClass().getName());
  }
 }
  public MyCustomDrawable getMCD(){return mcd;}


 public void setMCD(MyCustomDrawable mcd){this.mcd=mcd;}

 public synchronized/*?*/ void reset()
 {
  int i,j;
  for(i=0;i<sizeX;i++)
  {
      ops[0][i]=new OperationGraphic(i*SPACE,0);
      ops[0][i].setOperation((OperationFactory.getInstance()).generateOperation(-1));
  }
   for(i=1;i<sizeY;i++)
      for(j=0;j<sizeX;j++)
        ops[i][j]=null;
 }

 private Random r=new Random();
 public void pushRow(boolean addButtonPressed,boolean paused)
 {
  int i,j;
  int rx,ry,targetResult;
  System.out.println("Push me 1!");

  //ar trebui sa raman aici asta!
  //altfel poti sa apesi a-mpulea pe butonul "+" cat esti in pauza
  if(paused) return;
  
  System.out.println("Push me 2!");
  if(fullRow()) 
  {
  System.out.println("Push me 3!");
      if(addButtonPressed==false)//game over!
      {
        System.out.println("Push me 4!");
    	MathGlobes.gameOverWithLevel(MyCustomDrawable.getLevel());
        mcd.resetGame();
       // mcd.simulateBackPressed();
        //mcd.getGraphicActivity().finish();//vezi, ma tampitule, ca poate se fute urmatoarea chestie
        mcd.getGraphicActivity().intentToMathCrap(); 
      }
  }
  else
  {
   for(i=0;i<sizeY;i++)
      for(j=0;j<sizeX;j++)
          if(ops[i][j]!=null)
          {
              //there are conflicts between this and the case when the pointer is dragged
              //(when draggedGraphic.hardMove() is called)
              ops[i][j].moveUp(SPACE);
              ops[i][j].returnToDefault();
          }
   /*asta dauneaza acum!
    if(draggedGraphic!=null)
   {
       draggedGraphic.defaultUp(SPACE);
   }*/
   mcd.resetBar();
   boolean done=false;
   int retryCount;
   for(i=0;i<sizeX;i++)
   {
       done=false;
       retryCount=0;
       do
       {
           rx=r.nextInt(sizeX);
           ry=r.nextInt(sizeY);
           int k;
           if(MyCustomDrawable.getLevel()!=0)
           {
	           for(k=0;k<MyCustomDrawable.getLevel()/5; k++)//retry until you find a random match
	           {
	        	if(ops[ry][rx]==null)
	           	{
	               rx=r.nextInt(sizeX);
	               ry=r.nextInt(sizeY);        	   
	           	}
	        	else break;
	           }
           }
           if(ops[ry][rx]==null)
           {
               targetResult=-1;
           }
           else 
           {
               targetResult = ops[ry][rx].getOperationResult();
               done=true;
           }
           retryCount++;
           if(retryCount>=1) done=true;
       }while(!done);
       addOperationToColumn(OperationFactory.getInstance().generateOperation(targetResult),i);
   }

   for(i=0;i<sizeX;i++)
   {
    correctColumnNullReferences(i);
    //mai trebuie cacatul asta? correctYCoordinate(i);
   }
  }
  System.out.println("Push me 6!");
 }

 private void correctColumnNullReferences(int col)
 {
  int i;
  OperationGraphic og;
  for(i=0;i<sizeY-1;i++)
      if(ops[i][col]==null && ops[i+1][col]!=null)
      {
          og=new OperationGraphic(col+SPACE,i*SPACE);
          og.setOperation(OperationFactory.getInstance().generateOperation(-1));
          ops[i][col]=og;
      }
 }


 public void draw(Canvas canvas,boolean paused)
 {
  int i,j;
  for(i=0;i<sizeY;i++)
      for(j=0;j<sizeX;j++)
          if(ops[i][j]!=null)
          {
              if(ops[i][j].isSelected()==true)
                    ops[i][j].draw(canvas,operationBitmapSelected,SCALE,paused);
              else  ops[i][j].draw(canvas,operationBitmap,SCALE,paused);
          }
  canvas.drawLine(0, sizeY*SPACE*SCALE, sizeX*SPACE*SCALE, sizeY*SPACE*SCALE, linePaint);
 }


 public void clickDownEvent(int x,int y)
 {
     int i,j;
     draggedGraphic=null;
     for(i=0;i<sizeY;i++)
        for(j=0;j<sizeX;j++)
            if(ops[i][j]!=null)
            {
                if(ops[i][j].wasClicked(x, y))
                {
                    ops[i][j].colorSelected();
                    draggedGraphic=ops[i][j];
                }
            }
 }


 public void clickDragEvent(int x,int y)
 {
     if(draggedGraphic!=null)
        draggedGraphic.hardMove((int)(x*1/SCALE),(int)(y*1/SCALE));
     int i,j;
     for(i=0;i<sizeY;i++)
        for(j=0;j<sizeX;j++)
            if(ops[i][j]!=null)
            {
                if(ops[i][j].wasClicked(x, y))
                {
                    ops[i][j].colorSelected();
                }
                else ops[i][j].colorDeselected();
            }
 }


 public void clickUpEvent(int x,int y)
 {
     boolean ok=false;
     OperationGraphic endPoint=null;
     int i,j;
     for(i=0;i<sizeY;i++)
        for(j=0;j<sizeX;j++)
            if(ops[i][j]!=null)
            {
                if(ops[i][j].wasClicked(x, y))
                {
                    ops[i][j].colorSelected();//cam degeaba
                    if(draggedGraphic!=ops[i][j])
                    {
                        ok=true;
                        endPoint=ops[i][j];
                        break;
                    }
                }
            }
     if(draggedGraphic!=null)
         draggedGraphic.colorDeselected();
     if(endPoint!=null)
         endPoint.colorDeselected();
     if(ok==false && draggedGraphic!=null)
         draggedGraphic.returnToDefault();
     else if(draggedGraphic!=null)//ok==true implica endPoint!=null, deci nu mai faci test pentru conditia asta
     {
        if(endPoint.getOperationResult()==draggedGraphic.getOperationResult())
        {
            endPoint.getOperation().operationSolved();
            draggedGraphic.getOperation().operationSolved();
            MyCustomDrawable.increaseOperationsCleared();
            MyCustomDrawable.increaseScore(endPoint.getOperationScore());
            MyCustomDrawable.increaseScore(draggedGraphic.getOperationScore());
            eliminate(endPoint);
            eliminate(draggedGraphic);
            boolean empty= true;
            for(i=0;i<sizeY;i++)
                for(j=0;j<sizeX;j++)
                    if(ops[i][j]!=null)
                    {
                        empty=false;
                        break;
                    }
            if(empty)//give bonus for clearing the board
            {
                MyCustomDrawable.increaseScore(25*MyCustomDrawable.getLevel());
            }
        }
        else
        {
            draggedGraphic.returnToDefault();
        }
     }
     draggedGraphic=null;
 }

 private synchronized void eliminate(OperationGraphic g)
 {
     int i,j;
     for(i=0;i<sizeY;i++)
        for(j=0;j<sizeX;j++)
            if(g==ops[i][j])
            {
                ops[i][j]=null;
                fall(i,j);
                break;
            }
     for(i=0;i<sizeY;i++)
        for(j=0;j<sizeX;j++)
            if(ops[i][j]!=null)
                ops[i][j].returnToDefault();
     for(i=0;i<sizeX;i++)
         correctColumnReferences(i);
 }
 
 private synchronized void correctColumnReferences(int column)
 {
  int i;
  Vector<OperationGraphic> v=new Vector<OperationGraphic>();
  for(i=0;i<sizeY;i++)
      if(ops[i][column]!=null)
      {
          v.add(ops[i][column]);
      }
  for(i=0;i<sizeY;i++)//PERICULOS LA THREADURI!!
      ops[i][column]=null;
  int vSize=v.size();
  for(i=0;i<vSize;i++)
      ops[i][column]=v.get(i);
 }

 private void fall(int row,int column)
 {
  int i,nonNulls=0;
  for(i=row+1;i<sizeY;i++)
  {
      if(ops[i][column]!=null)
        ops[i][column].moveDown(SPACE);
  }
 }


}
