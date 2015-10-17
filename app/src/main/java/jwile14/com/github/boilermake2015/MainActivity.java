package jwile14.com.github.boilermake2015;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLatitude, mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ParseUser.logOutInBackground();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#EEEEEE"));
        tabLayout.setBackgroundColor(Color.parseColor("#242424"));
        tabLayout.setupWithViewPager(viewPager);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        // Checks if you're logged in
        if (ParseUser.getCurrentUser() == null) {
            navigateToLogin();
        } else {
            ParseUser curUser = ParseUser.getCurrentUser();

            if(curUser.get(ParseConstants.KEY_PROFILE_PIC) == null) {
                Drawable d = ContextCompat.getDrawable(this, R.drawable.generic_profile_pic);
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();

                ParseFile profilePic = new ParseFile("profile_pic.png", bitmapdata);

                curUser.put(ParseConstants.KEY_PROFILE_PIC, profilePic);
                curUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            viewPager.invalidate();
                        }
                    }
                });

            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            if (ParseUser.getCurrentUser() != null) {
                ParseUser curUser = ParseUser.getCurrentUser();
                curUser.put(ParseConstants.KEY_LATITUDE, mLatitude);
                curUser.put(ParseConstants.KEY_LONGITUDE, mLongitude);
                curUser.saveInBackground();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);

        // Prevents the user from hitting the back button while sent to the login screen
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        ArrayList<String> listExample = new ArrayList<>();
        for (int a = 0; a < 15; a++) {
            listExample.add("Message " + (a + 1));
        }
        listExample.add("");

        adapter.addFrag(new PlaceholderFragment(), "Books");
        ListFragment exampleListFragment = new ListFragment();
        Bundle listBundle = new Bundle();
        listBundle.putStringArrayList("list", listExample);
        exampleListFragment.setArguments(listBundle);
        adapter.addFrag(exampleListFragment, "Messages");

        adapter.addFrag(new ProfileFragment(), "Profile");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);

        for (int a = 0; a < viewPager.getChildCount(); a++) {
            View nextChild = viewPager.getChildAt(a);
            if (nextChild instanceof TextView) {
                TextView curChildTextView = (TextView) nextChild;
                curChildTextView.setTextColor(Color.parseColor("#800000"));
            }
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
        } else if (id == R.id.logout) {
            ParseUser.getCurrentUser();
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    navigateToLogin();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }
}
