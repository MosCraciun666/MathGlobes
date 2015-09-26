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
public class AddButton
{
    // private int width=100,height=100;
    private int x,y;
    private ShapeDrawable sd;
    private int color=0xFFFF00FF;
    private Paint paint;

    private static Bitmap bitmap;

    public static void setBitmap(Bitmap b){bitmap=b;}

    public AddButton()
    {

        sd = new ShapeDrawable(new OvalShape());
        sd.getPaint().setColor(color);
        paint = new Paint();
          //NU!!canvas.drawPaint(paint);
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(20);
    }

    public boolean wasClicked(int clickX,int clickY)
    {
        //old: return (clickX>=x && clickX<= x+width) && (clickY>=y && clickY<=y+height);
         return (clickX>=x && clickX<= x+bitmap.getWidth()) && (clickY>=y && clickY<=y+bitmap.getHeight());
    }


    public void draw(Canvas canvas,int x,int y)
    {
      this.x=x;
      this.y=y;
      //sd.setBounds(x, y, x + width, y + height);
      //sd.draw(canvas);=
      canvas.drawBitmap(bitmap, x, y, null);
    }

}
