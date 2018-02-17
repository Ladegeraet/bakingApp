package name.oho.baking.network;

import java.util.List;

import name.oho.baking.model.Receipt;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tobi on 17.02.18.
 */

public interface BakingService {

    @GET("baking.json")
    Call<List<Receipt>> receipts();

}
