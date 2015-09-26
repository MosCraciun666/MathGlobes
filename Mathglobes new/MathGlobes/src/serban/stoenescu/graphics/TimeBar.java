 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serban.stoenescu.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import serban.stoenescu.mathcrap.MyCustomDrawable;

/**
 *
 * @author Serban
 */
public class TimeBar
{
     private long startTime;
     private int duration,auxDuration;
     private Paint paint;
     private MyCustomDrawable mcd;
     private boolean paused;
     private long barSize;
     private long startPauseTime,endPauseTime;
     public void setMCD(MyCustomDrawable mcd){this.mcd=mcd;}
     
     public TimeBar(int duration)
     {
      this.duration=duration;
      auxDuration=duration;
      paint = new Paint();
      paint.setColor(Color.WHITE);
      paint.setStrokeWidth(10);
      paused = false;
      startPauseTime = 0;
      endPauseTime = 0;
     }

     public void setPaused(boolean paused)
     {
         if(paused == true)
         {
             startPauseTime = System.currentTimeMillis();
         }
         else
         {
             endPauseTime = System.currentTimeMillis();
         }
         barSize = duration-(System.currentTimeMillis()-startTime);
         this.paused=paused;
     }

     public void setDuration(int duration)
     {
         //this.duration=duration;
         auxDuration=duration;
     }

     public void start()
     {
         if(paused==false)//nesigur si periculos ca din cur scos
         {
          startPauseTime = 0;
         }
         endPauseTime = 0;
         startTime=System.currentTimeMillis();
     }

     public void draw(Canvas canvas,int x,int y)
     {
       //old: canvas.drawRect(x, y, x+25, y+15*(duration-(System.currentTimeMillis()-startTime))/1000, paint);

       if(paused==false) barSize=duration-(System.currentTimeMillis()-startTime)+(endPauseTime-startPauseTime);
       canvas.drawRoundRect( 
                new RectF(x, y, x+25,(int) (y+5*(barSize)/1000)),
                3,
                3,
                paint);

        duration=auxDuration;

        //System.out.println("Barsize="+barSize+" D="+duration+" CTM="+System.currentTimeMillis()+" ST="+startTime+" EPT="+endPauseTime+" SPT="+startPauseTime);
        if(barSize<0)
        {
            mcd.newRowThread();
            mcd.getOperationMatrix().pushRow(false,paused);
            start();
        }
     }


}
