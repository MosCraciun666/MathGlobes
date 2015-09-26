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
import serban.stoenescu.mathcrap.OperationMatrix;

/**
 *
 * @author Serban
 */
public class PauseButton
{
    // private int width=100,height=100;
    private int x,y;
    private ShapeDrawable sd;
    private int color=0xFFFF00FF;
    private boolean paused = false;
    private Paint paint;

    private static Bitmap activeBitmap;
    private static Bitmap pausedBitmap;

    public static void setActiveBitmap(Bitmap b){activeBitmap=b;}
    public static void setPausedBitmap(Bitmap b){pausedBitmap=b;}

    public PauseButton()
    {
        sd = new ShapeDrawable(new OvalShape());
        sd.getPaint().setColor(color);
        paint = new Paint();
          //NU!!canvas.drawPaint(paint);
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(20);
        paused = false;
    }

    public void invertState()
    {
        paused=!paused;
    }

    public boolean wasClicked(int clickX,int clickY)
    {
        //old: return (clickX>=x && clickX<= x+width) && (clickY>=y && clickY<=y+height);
         return (clickX>=x && clickX<= x+activeBitmap.getWidth()) && (clickY>=y && clickY<=y+activeBitmap.getHeight());
    }


    public void draw(Canvas canvas,int x,int y)
    {
      this.x=x;
      this.y=y;
      if(paused==false)
          canvas.drawBitmap(activeBitmap, x, y, null);
      else
          canvas.drawBitmap(pausedBitmap, x, y, null);
    }

}
