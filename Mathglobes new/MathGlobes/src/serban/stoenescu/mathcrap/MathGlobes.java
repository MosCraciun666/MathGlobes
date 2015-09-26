/*TODO
 * de facut operatiile mai usoare
 */
package serban.stoenescu.mathcrap;
 
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import util.IabHelper;
import util.IabResult;
import util.Purchase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apptracker.android.track.AppTracker;
import com.google.analytics.tracking.android.EasyTracker;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;


public class MathGlobes extends Activity
{
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private boolean pendingPublishReauthorization = true;
	
	boolean unlocked = false;
	
    private static int startLevel = 1, recommendedLevel = 5;
    private static int highScore = 0,  lastScore = 0;
    private static int ads = 0;
    public static int getStartLevel(){return startLevel;}
    private Button newGameFromLevelButton;
    private static int NUMBER_OF_LEVEL_RECORDS = 5;
    private static int[] lastLevels = new int[NUMBER_OF_LEVEL_RECORDS];
    private static int LEVEL_OFFSET = 1;
    private static boolean wasHighScore = false;
    private MathGlobes instance;
    
    private IabHelper mHelper;
	private boolean loaded  = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            
            //+billing
            String cheiaPulii = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx+KKlcL/jbFUVU2/2qr6qjXBA0Nkv+M3/kqJT+AcTlTcJIR16q4vLTzNmz2SqCrF7t/WkBLBqrokWTTnY1ai1n+Iq3E3vBTrDDOdAUDcuZDjbDHCkOCbBdA35PMVm/nLGuUuY5Tj7cycenkErjfzOdNRxE1Kr5b8YYzcHsbwj2UDnXxPLS2H3l4BPRLtMq7KUBEk93c5x3pyRyK23/iP/Jt7UT+pl79EPQAPR2iywIBH2q1rkjHpKp9twqCAZQ5XUueLudxRbxYEPwpoWiG5zYSO";
            String cheiaPizdii = "/AACuDViQ29nFmZVqKHD+7VwSLQ+wpLEpPplzvBlH8y35XCkCVrSkAbTx8aHcQIDAQAB";
            String base64EncodedPublicKey=cheiaPulii+cheiaPizdii;

            // compute your public key and store it in base64EncodedPublicKey
            mHelper = new IabHelper(this, base64EncodedPublicKey);
            
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            	   @Override
            	   public void onIabSetupFinished(IabResult result) {
            	      if (!result.isSuccess()) {
            	         // Oh noes, there was a problem.
            	         Log.d("BILLING", "Problem setting up In-app Billing: " + result);
            	      }
            	      else Log.i("BILLING","Hooray! IAB is fully set up!");
            	   }
            	});

            //-billing
            
            // Initialize Leadbolt SDK with your api key
            AppTracker.startSession(getApplicationContext(),"TbOizphizf5ZETKMaPBac8rV038MCUxp");
            // cache Leadbolt Ad without showing it
            AppTracker.loadModuleToCache(getApplicationContext(),"inapp");
            loaded = true;

            //new:
            setContentView(R.layout.main);

            ads = 0;

            
            System.out.println("Cacanarule 1");

            GraphicActivity.setMC(this);
            System.out.println("Cacanarule 2");
            instance = this;
            System.out.println("Cacanarule 3");

            for(int i = 0; i < NUMBER_OF_LEVEL_RECORDS; i++)
                lastLevels[i]=1;

            System.out.println("Cacanarule 4");
            openAsshole();

            TextView tv=(TextView)findViewById(R.id.textviewmain);
            tv.append("\n"+getString(R.string.highscorestring)+" "+highScore);

            Button newGameButton=(Button)findViewById(R.id.newgame);
            newGameButton.setText(getString(R.string.newgamestring));
            newGameFromLevelButton=(Button)findViewById(R.id.newgamefromlevel);
            newGameFromLevelButton.setText(getString(R.string.newgamefromlevelstring)+" ("+recommendedLevel+")");
           
            Button helpButton=(Button)findViewById(R.id.help);
            helpButton.setText(getString(R.string.helpstring));

            Button resetButton=(Button)findViewById(R.id.reset);
            resetButton.setText(getString(R.string.resetstring));

            newGameButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                	MyCustomDrawable.setDimension(4);
                    Intent i2 = new Intent(MathGlobes.this,GraphicActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);
                    startLevel=1;
                }
            });

            newGameFromLevelButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                	MyCustomDrawable.setDimension(4);
                    Intent i2 = new Intent(MathGlobes.this,GraphicActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);
                    startLevel=recommendedLevel;
                }
            });
            
            Button playExpertButton;
            playExpertButton=(Button)findViewById(R.id.newgameexpert);
            playExpertButton.setText(getString(R.string.play_expert));


            final IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    Log.d("BILLING", "Consumption finished. Purchase: " + purchase + ", result: " + result);

                    // if we were disposed of in the meantime, quit.
                    if (mHelper == null) return;

                    // We know this is the "gas" sku because it's the only one we consume,
                    // so we don't check which sku was consumed. If you have more than one
                    // sku, you probably should check...
                    if (result.isSuccess()) {
                        // successfully consumed, so we apply the effects of the item in our
                        // game world's logic, which in our case means filling the gas tank a bit
                    	unlocked = true;
                        Log.d("BILLING", "Consumption successful. Provisioning.");
                    }
                    else {
                    }
                      Log.d("BILLING", "End consumption flow.");
                }
            };

            

            // Callback for when a purchase is finished
            final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
            	public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                    Log.d("BILLING", "Purchase finished: " + result + ", purchase: " + purchase);

                    // if we were disposed of in the meantime, quit.
                    if (mHelper == null) return;

                    if (result.isFailure()) {
                		Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.errorpurchasing), Toast.LENGTH_LONG);
                		toast.show();
                        return;
                    }
                    if (!verifyDeveloperPayload(purchase)) {
                    	Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.errorpurchasing), Toast.LENGTH_LONG);
                		toast.show();
                        return;
                    }

                    Log.d("BILLING", "Purchase successful.");

                    if (purchase.getSku().equals("mathglobes.expertmode")) {
                        // bought 1/4 tank of gas. So consume it.
                        Log.d("BILLING", "Purchase is gas. Starting gas consumption.");
                        mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    }
                }
            };
            
            playExpertButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                	if(!unlocked)
                	{
                		/*
                		Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.locked), Toast.LENGTH_LONG);
                		toast.show();*/
                		mHelper.launchPurchaseFlow(instance, "mathglobes.expertmode", 10001,//pula mea daca stiu ce e cu 10001
                                mPurchaseFinishedListener, "payloadDummy");
                	}
                	else
                	{
                		MyCustomDrawable.setDimension(5);
                    	Intent i2 = new Intent(MathGlobes.this,GraphicActivity.class);
                    	i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    	startActivity(i2);
                    	startLevel=1;
                	}
                }
            });
            


            resetButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                    ResetActivity.setMathGlobesActivity(instance);
                    Intent i2 = new Intent(MathGlobes.this,ResetActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);
                }
            });

            helpButton.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                    Intent i2 = new Intent(MathGlobes.this,HelpActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i2);
                }
            });
            //AdBuddiz.showAd(this);
            System.out.println("Am terminat, ba boule ");
            //uiHelper.onCreate(savedInstanceState);
        }
        catch(NullPointerException e)//de ce pula mea ai facut asta? (era cu System.exit(0))
        {
            TextView tv=new TextView(this);
            tv.setText(e.getMessage()+" OF CLASS "+e.getClass().getName()+" localized message: "+e.getLocalizedMessage());
            setContentView(tv);
        }
        catch(Exception e)
        {
            TextView tv=new TextView(this);
            tv.setText(e.getMessage()+" OF CLASS "+e.getClass().getName()+" localized message: "+e.getLocalizedMessage());
            setContentView(tv);
        }
    }
    public void resetProgress()
    {
        int i;
        setHighScore(0);
        for(i=0;i<NUMBER_OF_LEVEL_RECORDS;i++)
            lastLevels[i]=1;
        refreshViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            //finish();
            try
            {
             onExit();
             /*System.exit(0);
             android.os.Process.killProcess(android.os.Process.myPid());*/
             return super.onKeyDown(keyCode,event);
            }
            catch(Exception e)
            {
             e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    //daca apelezi asta din thread-uri (si evident ca asa trebuie sa faci)
    //arunca o exceptie fatala daca incerci sa modifici ceva
    //la view-urile create de threadul principal
    public void setHighScore(int score)
    {
        highScore=score;
    }

    public void setLastScore(int score)
    {
        lastScore=score;
    }

    
    public static int getHighScore()
    {
        return highScore;
    }

    private void refreshViews()
    {
        int i, average = 0;
        for(i=0; i<NUMBER_OF_LEVEL_RECORDS; i++)
        {
             average += lastLevels[i];
             System.out.println("Last levels["+i+"] = "+lastLevels[i]);
        }
        average /= NUMBER_OF_LEVEL_RECORDS;
        recommendedLevel = average - LEVEL_OFFSET;
        if(recommendedLevel < 1)
             recommendedLevel = 1;
        if(newGameFromLevelButton!=null)//dumbass shit error
        {
            newGameFromLevelButton.setText(getString(R.string.newgamefromlevelstring)+" ("+recommendedLevel+")");
        }
        TextView tv=(TextView)findViewById(R.id.textviewmain);
        if(tv==null)//setContentView was not (properly) called
        {
        	setContentView(R.layout.main);
        	tv=(TextView)findViewById(R.id.textviewmain);
        }
        if(lastScore!=0)
        {
            if(wasHighScore)
                tv.setText("\n\n"+getString(R.string.lastscorestring)+lastScore+" "+getString(R.string.washighscorestring));
            else
                tv.setText("\n\n"+getString(R.string.lastscorestring)+lastScore+" "+getString(R.string.highscorestring)+" "+highScore);

            wasHighScore = false;
        }
        if(highScore==0)
        {
        	if(tv==null) System.out.println("Ba pula, tv = null");
        	if(getString(R.string.hello)==null) System.out.println("Ba pula, stringul = null");
            tv.setText("\n\n"+getString(R.string.hello));
        }

    }

    //cred ca aici trebuie sa pui aia, ba boule!
    @Override
    protected void onResume()
    {
        super.onResume();
        System.out.println("RESUME!");
        refreshViews();

        // call this when you want to display the Leadbolt Interstitial
        if(loaded)
        {
        	Random r = new Random();
        	int cacat = r.nextInt(2);
        	if(cacat==0)
        		AppTracker.loadModule(getApplicationContext(),"inapp");
        }
    }
    
    
    
    
    
    
    @Override
    public void onStop()
    {
         super.onStop();
         EasyTracker.getInstance(this).activityStop(this);
         onExit();
    }
    
    public void readUnlockedStatusFile()
    {

        FileInputStream fIn;
		try 
		{
			fIn = openFileInput("mathcraplocked.txt");
		} 
		catch (FileNotFoundException e) 
		{
			unlocked=false;
			e.printStackTrace();
			return;
		}
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader br=new BufferedReader(isr);

        String readString;
        try 
        {
			readString=br.readLine();
			unlocked = Boolean.parseBoolean(readString.trim());
		} 
        catch (IOException e) 
        {
			unlocked=false;
			e.printStackTrace();
			return;
		}
        catch(Exception e)
        {
			unlocked=false;
			e.printStackTrace();
			return;        	
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    public void openAsshole() 
    {
    	readUnlockedStatusFile();
    	
        boolean ok1=false,ok2=false;
        int i,average;
        highScore=1;
        BufferedReader reader = null;
        try
        {
            FileInputStream fIn = openFileInput("mathcrap.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            reader=new BufferedReader(isr);

            String readString;
            readString=reader.readLine();
            if(readString!=null)
                highScore=Integer.parseInt(readString.trim());
            else highScore = 0; //error case; still, continue
            setHighScore(highScore);
            ok1=true;

            for(i=0;i<NUMBER_OF_LEVEL_RECORDS;i++)
            {
                lastLevels[i] = 1;//just in case an excpetion is thrown  (accidental EOF encountered, especially the first time)
                readString = reader.readLine();
                if(readString != null)
                    lastLevels[i] = Integer.parseInt(readString.trim());
                else lastLevels[i] = 0; //error case; still, continue
            }
            ok2=true;
        }
        catch(FileNotFoundException e)
        {
            //I don't give a fuck (+this is normal after install), suppress this exception
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(!ok1)
                setHighScore(0);
            if(!ok2)
                recommendedLevel = 1;
            else
            {
                average = 0;
                for(i=0; i<NUMBER_OF_LEVEL_RECORDS; i++)
                    average += lastLevels[i];
                average /= NUMBER_OF_LEVEL_RECORDS;
                recommendedLevel = average - LEVEL_OFFSET;
                if(recommendedLevel < 1)
                    recommendedLevel = 1;
            }
            if(reader!=null)
            {
                try
                {
                    reader.close();
                }
                catch(IOException ioe)
                {
                    //I don't give a fuck about this shit
                }
            }
        }
    }

    //mathcraplocked
    public void onExitLocked()
    {
        int i;
        try
        {
           FileOutputStream fOut = openFileOutput("mathcraplocked.txt",MODE_WORLD_READABLE);
           OutputStreamWriter osw = new OutputStreamWriter(fOut);

           // Write the string to the file
           osw.write(unlocked+"\n");

           osw.flush();
           osw.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    
    public void onExit()
    {
        int i;
        onExitLocked();
        try
        {
           FileOutputStream fOut = openFileOutput("mathcrap.txt",MODE_WORLD_READABLE);
           OutputStreamWriter osw = new OutputStreamWriter(fOut);

           // Write the string to the file
           osw.write(highScore+"\n");
           for(i=0; i<NUMBER_OF_LEVEL_RECORDS; i++)
               osw.write(lastLevels[i]+"\n");

           osw.flush();
           osw.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void wasHighScoreTrue()
    {
        wasHighScore=true;
    }

    public static void gameOverWithLevel(int lvl)
    {
        int i,average = 0;
        System.out.println("Asshole level = "+lvl);
        for(i=0; i<NUMBER_OF_LEVEL_RECORDS-1;i++)
        {
        	if(lastLevels[i]==0) lastLevels[i]=1;
            lastLevels[i] = lastLevels[i+1];
            average += lastLevels[i+1];
            System.out.println("Suck my dick at level "+lastLevels[i]);
        }
        lastLevels[NUMBER_OF_LEVEL_RECORDS-1] = lvl;
        average += lvl;
        average /= NUMBER_OF_LEVEL_RECORDS;
        recommendedLevel = average - LEVEL_OFFSET;
        if(recommendedLevel < 1)
            recommendedLevel = 1;
    }

   
  @Override
  public void onStart()
  {
    super.onStart();
    System.out.println("Pisa-m-as pe tine de cacat cu ochi "+ads);
    System.out.println("Ba boule, te fut intre dintii care iti lipsesc, manca-mi-ai pula de zdreanta imputita");
    EasyTracker.getInstance(this).activityStart(this); // Add this method.
    //show fucking asshole ad

    System.out.println("Ai intrat in pizda ma-tii");
    //AdBuddiz.showAd(this); 
    System.out.println("Am executat tot, ba bou\' pulii mele");
    
    ads++;

  }
  
  
  

  
  @Override
  public void onDestroy() {
     super.onDestroy();
     if (mHelper != null) mHelper.dispose();
     mHelper = null;
  }
}
