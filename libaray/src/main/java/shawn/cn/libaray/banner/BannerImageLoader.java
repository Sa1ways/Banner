package shawn.cn.libaray.banner;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Shawn on 2017/7/24.
 */

public interface BannerImageLoader {

    <T extends ImageView> void loadImage(Context context, String url, T iv);

    <T extends ImageView> void loadImage(Context context, int resource, T iv);

}
