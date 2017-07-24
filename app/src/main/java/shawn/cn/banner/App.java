package shawn.cn.banner;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import shawn.cn.libaray.banner.Banner;
import shawn.cn.libaray.banner.BannerImageLoader;

/**
 * Created by Shawn on 2017/7/24.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Banner.setUpImageLoader(new BannerImageLoader() {
            @Override
            public <T extends ImageView> void loadImage(Context context, String url, T iv) {
                Glide.with(context).load(url).placeholder(R.mipmap.dy_loading).into(iv);
            }

            @Override
            public <T extends ImageView> void loadImage(Context context, int resource, T iv) {
                Glide.with(context).load(resource).placeholder(R.mipmap.dy_loading).into(iv);
            }

        });
    }
}
