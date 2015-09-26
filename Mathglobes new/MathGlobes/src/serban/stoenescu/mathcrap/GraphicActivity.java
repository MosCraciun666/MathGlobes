 package serban.stoenescu.mathcrap;

/*
 * eroarea este Illegal Argument Excption in Random.nextInt();
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory; 
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.analytics.tracking.android.EasyTracker;
import java.util.Random;
import serban.stoenescu.graphics.AddButton;
import serban.stoenescu.graphics.OperationGraphic;
import serban.stoenescu.graphics.PauseButton;

public class GraphicActivity extends Activity
{
    private Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    private Bitmap background;
    private Bitmap operationBitmap;
    private Bitmap operationBitmapSelected;
    private Bitmap addButtonBitmap;
    private Bitmap pauseButtonActiveBitmap;
    private Bitmap pauseButtonPausedBitmap;
    private Canvas c = new Canvas(b);
    private static MathGlobes mc;
    private MyCustomDrawable myCustomDrawable = null;

    public static void setMC(MathGlobes mathCrap){mc=mathCrap;}

    public void intentToMathCrap()
    {
        Intent i2 = new Intent(GraphicActivity.this, MathGlobes.class);
        i2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//serios?
        startActivity(i2);
        finish();
    }

    public static void mathCrapSetHighScore(int score)
    {
        mc.setHighScore(score);
    }

    public static void mathCrapSetLastScore(int score)
    {
        mc.setLastScore(score);
    }

    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
        	MathGlobes.gameOverWithLevel(MyCustomDrawable.getLevel());
            myCustomDrawable.resetGame();
            myCustomDrawable.getGraphicActivity().intentToMathCrap();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        MyCustomDrawable.setGraphicActivity(this);
        try
        {
            super.onCreate(savedInstanceState);
            System.out.println("[TEMP]AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            System.out.println("[TEMP]BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            //setContentView(R.layout.main);

            b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            c = new Canvas(b);
            background=BitmapFactory.decodeResource(getResources(), R.drawable.bg1);

            operationBitmap=BitmapFactory.decodeResource(getResources(), R.drawable._disc);
            operationBitmapSelected=BitmapFactory.decodeResource(getResources(), R.drawable._disc2);
            addButtonBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.addbutton);
            pauseButtonActiveBitmap=BitmapFactory.decodeResource(getResources(), R.drawable._pause);
            pauseButtonPausedBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.resume);

            final MyCustomDrawable mcd=new MyCustomDrawable(this);
            myCustomDrawable = mcd;
            setContentView(mcd);
            if(background!=null)
                mcd.setBackground(background);
            OperationMatrix.setOperationBitmap(operationBitmap);
            System.out.println("[TEMP]3.6CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
            OperationMatrix.setOperationBitmapSelected(operationBitmapSelected);
            System.out.println("[TEMP]3.7CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
            AddButton.setBitmap(addButtonBitmap);
            PauseButton.setActiveBitmap(pauseButtonActiveBitmap);
            PauseButton.setPausedBitmap(pauseButtonPausedBitmap);

            System.out.println("[TEMP]4CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");

            final View touchView = mcd;//findViewById(R.id.touchView);

            System.out.println("[TEMP]DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");

            touchView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    int eventAction=event.getAction();

                    if(eventAction==MotionEvent.ACTION_DOWN)
                        mcd.clickDownEvent((int)event.getX(), (int)event.getY());
                    else if(eventAction==MotionEvent.ACTION_MOVE)
                    {
                        mcd.clickDragEvent((int)event.getX(),(int)event.getY());
                    }
                    else if(eventAction==MotionEvent.ACTION_UP)
                    {
                        mcd.setX((int)event.getX());
                        mcd.setY((int)event.getY());
                        mcd.clickUpEvent((int)event.getX(),(int)event.getY());
                    }
                    mcd.invalidate();//sau postInvalidate
                    return true;
                }
            });
        }
        catch(NullPointerException e)//de ce pula mea ai facut asta?
        {
            System.out.println("[TEMP] NULL BAAA BOULEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
            System.out.println("[TEMP] Message = "+e.getMessage()+"::"+e.getLocalizedMessage()+"::"+e.toString()+"::"+e.getCause());
        }
        catch(Exception e)
        {
            TextView tv=new TextView(this);
            tv.setText(e.getMessage()+" OF CLASS "+e.getClass().getName()+" localized message "+e.getLocalizedMessage());
            setContentView(tv);
        }
    }//end onCreate


 @Override
  public void onStart() {
    super.onStart();
    EasyTracker.getInstance(this).activityStart(this); // Add this method.
  }

  @Override
  public void onStop() {
    super.onStop();
    EasyTracker.getInstance(this).activityStop(this); // Add this method.
  }
}
