package name.oho.baking;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import name.oho.baking.network.NetworkModule;
import timber.log.Timber;

/**
 * Created by tobi on 17.02.18.
 */

public class BakingApplication extends Application {

    private AppComponent mAppComponent;

    // Convenience method for getting application context
    public static BakingApplication get(Context context) {
        return (BakingApplication) context.getApplicationContext();
    }

    // Getter for AppComponent
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Timber.plant(new Timber.DebugTree());

        // Create instance of AppComponent

        mAppComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }
}
