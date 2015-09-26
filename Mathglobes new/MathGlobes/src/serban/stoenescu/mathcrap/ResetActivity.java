/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serban.stoenescu.mathcrap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.analytics.tracking.android.EasyTracker;

/**
 *
 * @author Serban
 */
public class ResetActivity extends Activity
{
    private static MathGlobes mathGlobesActivity = null;
    public static void setMathGlobesActivity(MathGlobes mg)
    {
        mathGlobesActivity = mg;
    }
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.reset);
        Button yesButton=(Button)findViewById(R.id.yes_reset_button);
        Button noButton=(Button)findViewById(R.id.no_reset_button);
        yesButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                    mathGlobesActivity.resetProgress();
                    finish();
                }
            });
        noButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                    finish();
                }
            });
    }

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
