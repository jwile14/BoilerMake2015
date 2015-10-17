package jwile14.com.github.boilermake2015;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by wilejd on 10/17/2015.
 */
public class Boilermake2015Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        // Parse.enableLocalDatastore(this);

        Parse.initialize(this, "d5TlKUU3vmnVgAZH2YfpJSW2TPydiaXwTjl4s67H", "4pMHGGPsvegoYOZ1px5OZ5U6WBiLZBl2ZP0vYmNd");

    }
}
