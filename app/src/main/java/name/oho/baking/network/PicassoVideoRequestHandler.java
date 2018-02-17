package name.oho.baking.network;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by tobi on 17.02.18.
 */

public class PicassoVideoRequestHandler extends RequestHandler {
    public static final String MP4 = "mp4";

    @Override
    public boolean canHandleRequest(Request data) {
        return data.uri.toString().contains(MP4);
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(request.uri.toString(), new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            throw new IOException("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return new Result(bitmap, Picasso.LoadedFrom.DISK);
    }
}
