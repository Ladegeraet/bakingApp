package name.oho.baking;

/**
 * Created by tobi on 17.02.18.
 */

import javax.inject.Singleton;

import dagger.Component;
import name.oho.baking.network.NetworkModule;

@Singleton
@Component(modules = NetworkModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
