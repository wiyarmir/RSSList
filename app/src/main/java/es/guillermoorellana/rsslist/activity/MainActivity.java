package es.guillermoorellana.rsslist.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.fragment.ConfigFragment;
import es.guillermoorellana.rsslist.fragment.OnFragmentInteractionListener;
import es.guillermoorellana.rsslist.fragment.SplashFragment;


public class MainActivity extends Activity implements OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(
                            R.id.container,
                            SplashFragment.newInstance(
                                    getResources().getInteger(R.integer.splash_timeout),
                                    getString(R.string.splash_next_fragment)),
                            SplashFragment.FRAGMENT_TAG
                    )
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.getScheme().equals("fragment")) {
            String fragmentTag = uri.getHost();
            Log.d(TAG, fragmentTag);
            if (fragmentTag.equals("config")) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new ConfigFragment(), ConfigFragment.FRAGMENT_TAG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //ft.setTransitionStyle(R.animator.fade_out);
                ft.setCustomAnimations(0, R.animator.fade_out);
                ft.commit();
            }
        }
    }
}
