 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serban.stoenescu.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import serban.stoenescu.operations.Operation;
import serban.stoenescu.operations.OperationFactory;

/**
 *
 * @author Serban
 */
public class OperationGraphic 
{
 private int x,y;
 private int targetX,targetY;
 private int defaultX,defaultY;
 private int width = 100;
 private int height = 100;
 private int color = 0xFF0000FF;
 private int SPEED = 15;
 private int BOUND = 15;
 private ShapeDrawable sd;
 private Operation operation;
 private Paint paint;
 private boolean selected = false;
 private float scale;
 private int TEXTSIZE = 30;
 private static boolean paused;
 public static void setPaused(boolean p){paused=p;}

 public int getY(){return y;}
 public Operation getOperation(){return operation;}
 public void setOperation(Operation operation)
 {
     this.operation=operation;
 }

 public OperationGraphic(int x,int y)
 {
    this.x=x;
    this.y=y;
    defaultX=x;
    defaultY=y;
    targetX=x;
    targetY=y;
    selected =  false;
    sd = new ShapeDrawable(new OvalShape());
    sd.getPaint().setColor(color);
    sd.setBounds(x, y, x + width, y + height);
    paint = new Paint();
      //NU!!canvas.drawPaint(paint);
    paint.setColor(0xFFFFFFFF);
    paint.setTextSize(TEXTSIZE);
 }

 public boolean isSelected(){return selected;}

 public void colorSelected()
 {
  selected=true;
  color=0xFF00FF00;
  sd.getPaint().setColor(color);
 }

 public void colorDeselected()
 {
  selected=false;
  color=0xFF0000FF;
  sd.getPaint().setColor(color);
 }

 public void move(int x,int y)
 {
     targetX=x;
     targetY=y;
     sd.setBounds(x, y, x + width, y + height);
 }

 public void hardMove(int x,int y)
 {
     this.x=x-width/2;
     this.y=y-height/2;
     targetX=x-width/2;
     targetY=y-height/2;
     sd.setBounds(x, y, x + width, y + height);
 }

 public void moveDown(int offset)
 {
     defaultY-=offset;
     //targetY-=offset;
     move(x,y-offset);
 }
 public void moveDown()
 {
     moveDown(height);
 }
 public void moveUp(int offset)
 {
     defaultUp(offset);
     move(x,y+offset);
 }
 public void defaultUp(int offset)//used just this for the superposition bug
 {
     defaultY+=offset;
 }
 public void returnToDefault()
 {
     move(defaultX,defaultY);
 }
 public int getOperationResult()
 {
   return operation.getResult();
 }
 public int getOperationScore()
 {
   return operation.getScore();
 }

 public void draw(Canvas canvas, Bitmap b,float scale,boolean paused)
 {
     this.scale=scale;
     //paint.setTextSize((int)(TEXTSIZE*scale));

     if(!paused)
     {
     if(this.x<targetX) this.x+=SPEED;
     else if(this.x>targetX) this.x-=SPEED;
     if(this.y<targetY) this.y+=SPEED;
     else if(this.y>targetY) this.y-=SPEED;

     if(x>=targetX-BOUND && x<=targetX+BOUND)
         x=targetX;
     if(y>=targetY-BOUND && y<=targetY+BOUND)
         y=targetY;
     }
     sd.setBounds(x, y, x + width, y + height);
     
      //sd.draw(canvas);

      canvas.save();
      canvas.scale(scale,scale);
      canvas.drawBitmap(b, x, y, null);
      if(operation==null)//defense in case of accidenta null references where none should be
          operation=(OperationFactory.getInstance()).generateOperation(-1);
      int textLength = operation.toString().length();
      if(textLength >= 7)//avoid division by zero(exceptional case)
      {
          paint.setTextSize((int)(TEXTSIZE*6/textLength));
      }
      if(paused==false)
        canvas.drawText(operation.toString(), x+width/2-20-3*textLength, y+width/2+5, paint);
      canvas.restore();
 }


 public boolean wasClicked(int clickX,int clickY)
 {
     return (clickX>=x*scale && clickX<= (x+width)*scale) && (clickY>=y*scale && clickY<=(y+height)*scale);
 }

}
