 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serban.stoenescu.mathcrap;

//import android.R;
import android.app.Activity;
import android.os.Bundle;
import com.google.analytics.tracking.android.EasyTracker;

/**
 *
 * @author Serban
 */
public class HelpActivity extends Activity
{
    
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.help);
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
