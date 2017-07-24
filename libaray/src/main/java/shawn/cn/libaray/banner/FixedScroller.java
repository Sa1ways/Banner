package shawn.cn.libaray.banner;

import android.content.Context;
import android.widget.Scroller;

/**
 * Created by Shawn on 2017/7/24.
 */

public class FixedScroller extends Scroller {

    public static final int DEFAULT_DURATION = 250;

    private int mScrollDuration = DEFAULT_DURATION;

    public FixedScroller(Context context) {
        super(context);
    }

    public FixedScroller(Context context,int duration) {
        super(context);
        this.mScrollDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
}
