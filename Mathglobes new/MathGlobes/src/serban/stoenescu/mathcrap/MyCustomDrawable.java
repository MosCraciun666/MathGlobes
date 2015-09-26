package serban.stoenescu.mathcrap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.KeyEvent;
import android.view.View;
import serban.stoenescu.graphics.AddButton;
import serban.stoenescu.graphics.OperationGraphic;
import serban.stoenescu.graphics.PauseButton;
import serban.stoenescu.graphics.TimeBar;
import serban.stoenescu.operations.OperationFactory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serban
 */
public class MyCustomDrawable extends View
{
 private ShapeDrawable sd;
 private static GraphicActivity graphicActivity;
 public static void setGraphicActivity(GraphicActivity ga){graphicActivity=ga;}
 public static GraphicActivity getGraphicActivity(){return graphicActivity;}
 
 private static TimeBar timeBar;

 private int x = 10;
 private int y = 10;
 private int color=0xFFFFFFFF;
 private int width = 300;
 private int height = 50;
 private OperationMatrix matrix;
 private Paint textPaint;
 private static int score = 0;
 private static int operationsCleared=0;
 private static int duration;
 private static int level = 1;
 private float AMPLIFY_RATIO  = 1.125f;

 private enum State {PLAYING,PAUSED};
 private State state;

 public static int getLevel(){return level;}

 public static void increaseOperationsCleared()
 {
     operationsCleared++;
     if(operationsCleared % 12 == 0 || (operationsCleared == 5 && level == 1))
         increaseLevel();
 }

 public static void increaseLevel()
 {
  level++;
  duration+=1800;
  if(duration>35000) duration=35000;
  timeBar.setDuration(duration);
  OperationFactory.getInstance().setLevel(level);
 }
 public static void resetOperationsCleared(){operationsCleared=0;}

 public void simulateBackPressed()
 {
    KeyEvent k = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
    graphicActivity.dispatchKeyEvent(k);
 }


 public static void setDimension(int dim){matrixX=dim;matrixY=dim;}
 private static int matrixX = 4,matrixY = 4;

 private Bitmap background;
 private AddButton addButton;
 private PauseButton pauseButton;
 
 public void setBackground(Bitmap background) {this.background=background;}

 public static void increaseScore(int x)
 {
     score+=x;
 }

 public MyCustomDrawable(Context context)
 {
      super(context);

      state = State.PLAYING;

      duration = 5000;
      level=1;
      OperationFactory.getInstance().setLevel(level);
      
      score=0;
      operationsCleared=0;
 
      sd = new ShapeDrawable(new OvalShape());
      sd.getPaint().setColor(color);
      sd.setBounds(x, y, x + width, y + height);
      matrix=new OperationMatrix(this,matrixX,matrixY);
      matrix.setMCD(this);

      textPaint = new Paint();
      //NU!!canvas.drawPaint(paint);
      textPaint.setColor(0xFFFFFFFF);
      textPaint.setTextSize(20);

      timeBar=new TimeBar(duration);
      timeBar.setMCD(this);
      timeBar.start();//ATENTIE!!provizoriu!!

      addButton=new AddButton();
      pauseButton=new PauseButton();

      //OLD:resetGame();//DO NOT CALL HERE? THIS WILL INTRODUCE A LOT OF '1' IN THE LEVEL LIST
      resetGame();
 }

 private String log;
 public void setLog(String logText)
 {
     log=logText;
 }
 
 public final void resetGame()
 {
   int lastScore = score, lastLevel = level;
   System.out.println("Level=1");
   score=0;
   int i;
   level=1;
   resetOperationsCleared();
   OperationFactory.getInstance().setLevel(level);

   matrix.reset();
   timeBar.setDuration(5000);
   resetBar();

   for(i=0;i<MathGlobes.getStartLevel()-1;i++)
   {
    increaseLevel();
   }

   if(MathGlobes.getHighScore()<lastScore)
   {
       graphicActivity.mathCrapSetHighScore(lastScore);
       MathGlobes.wasHighScoreTrue();
   }
   graphicActivity.mathCrapSetLastScore(lastScore);
   //OLD: DO NOT CALL HERE?MathGlobes.gameOverWithLevel(lastLevel);
 }

public OperationMatrix getOperationMatrix(){return matrix;}

 public void setX(int x){this.x=x;}
 public void setY(int y){this.y=y;}


 public void resetBar()
 {
     timeBar.start();
 }

 
 @Override
 protected void onDraw(Canvas canvas)
 {
     int canvasWidth=canvas.getWidth();
     int canvasHeight=canvas.getHeight();
     int backgroundSizeX=background.getWidth();
     int backgroundSizeY=background.getHeight();
     float newScale;
     float scaleX=(float)canvasWidth/(float)backgroundSizeX;
     float scaleY=(float)canvasHeight/(float)backgroundSizeY;

     if(canvasWidth<=canvasHeight) 
     {
         newScale = (float)canvasWidth/768;
     }
     else 
     {
         newScale = (float)canvasHeight/976;
     }
     OperationMatrix.setScale(newScale*AMPLIFY_RATIO);

    
     canvas.save();
     canvas.scale(scaleX, scaleY);
     canvas.drawBitmap(background, 0, 0, textPaint);
     canvas.restore();

     //+draw game stuff
     if(state==State.PLAYING || state==state.PAUSED)
     {
     timeBar.draw(canvas,canvasWidth-(int)(100*OperationMatrix.getScale()), (int)(0.33f*canvasHeight)+(int)(180*OperationMatrix.getScale()));
     addButton.draw(canvas,canvasWidth-(int)(150*OperationMatrix.getScale()),(int)(30*OperationMatrix.getScale()));
     pauseButton.draw(canvas,canvasWidth-(int)(150*OperationMatrix.getScale()),(int)(180*OperationMatrix.getScale()));
     if(state==State.PAUSED)
        matrix.draw(canvas,true);
     else matrix.draw(canvas, false);

     canvas.drawText(graphicActivity.getString(R.string.scorestring)+" " +score +" " +graphicActivity.getString(R.string.levelstring)+" "+level,
                        canvas.getWidth()-450*OperationMatrix.getScale(),
                        canvas.getHeight()-150*OperationMatrix.getScale(),
                        textPaint);
     }
     //-draw game stuff


     /*NU STERGE!if(log!=null)
         canvas.drawText(log, 10, canvas.getHeight()-100*OperationMatrix.getScale(), textPaint);*/

      refreshDrawableState();
      invalidate();
 }

 public void newRowThread()
 {
     timeBar.setDuration(duration);
     timeBar.start();
 }

 
 public void clickDownEvent(int x,int y)
 {
     matrix.clickDownEvent(x, y);
     if(addButton.wasClicked(x, y))
     {
         if(state==State.PAUSED)
            matrix.pushRow(true,true);
         else matrix.pushRow(true,false);
         if(matrix.fullRow()==false)
         {
            newRowThread();
            increaseScore(level*5);
         }
     }
     if(pauseButton.wasClicked(x,y))
     {
            if(state==State.PAUSED)
                state=State.PLAYING;
            else if(state==State.PLAYING)
                state=State.PAUSED;

            if(state==State.PAUSED)
            {
                OperationGraphic.setPaused(true);//hide operations if paused!
                timeBar.setPaused(true);//stop/restart the time bar
            }
            else
            {
                OperationGraphic.setPaused(false);//hide operations if paused!
                timeBar.setPaused(false);//stop/restart the time bar
            }
            pauseButton.invertState();       
     }
 }

  
 public void clickDragEvent(int x,int y)
 {
     if(state==state.PLAYING)
        matrix.clickDragEvent(x, y);
 }

 public void clickUpEvent(int x,int y)
 {
     matrix.clickUpEvent(x,y);
 }

 public void drawDrawable(Canvas canvas)
 {
     sd.draw(canvas);
 }
 
}


