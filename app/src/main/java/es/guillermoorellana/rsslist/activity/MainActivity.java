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
import es.guillermoorellana.rsslist.fragment.RSSListFragment;
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.getScheme().equals("fragment")) {
            String fragmentTag = uri.getHost();
            Log.d(TAG, fragmentTag);
            if (fragmentTag.equals(ConfigFragment.FRAGMENT_TAG)) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new ConfigFragment(), ConfigFragment.FRAGMENT_TAG);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                //ft.setTransitionStyle(R.animator.fade_out);
                ft.setCustomAnimations(0, R.animator.fade_out);
                ft.commit();
            } else if (fragmentTag.equals(RSSListFragment.FRAGMENT_TAG)) {
                int feedId = Integer.parseInt(uri.getQueryParameter(RSSListFragment.ARG_FEED_ID));
                String feedTitle = uri.getQueryParameter(RSSListFragment.ARG_FEED_TITLE);
                String feedUrl = uri.getQueryParameter(RSSListFragment.ARG_FEED_URL);

                FragmentTransaction ft = getFragmentManager().beginTransaction();

                ft.add(R.id.container, RSSListFragment.newInstance(feedId, feedTitle, feedUrl), RSSListFragment.FRAGMENT_TAG);
                ft.addToBackStack(null);
                //ft.replace(R.id.container, RSSListFragment.newInstance(feedId, feedTitle, feedUrl), RSSListFragment.FRAGMENT_TAG);

                ft.commit();
            }
        }
    }
}
