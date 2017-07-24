package shawn.cn.libaray.banner;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Shawn on 2017/7/23.
 */

public class BannerAdapter extends PagerAdapter implements View.OnTouchListener, View.OnClickListener {

    public static final String TAG = BannerAdapter.class.getSimpleName();

    public static BannerImageLoader mImageLoader;

    public static final int DEFAULT_ADJUST_DURATION = 200;

    private int mAdjustDuration = DEFAULT_ADJUST_DURATION;

    private int mInterval ;

    private Handler mHandler = new Handler();

    private boolean sInitTimer = true;

    private int mCurrPageIndex;

    private SparseArray<ImageView> mIvContainer;

    private Context mContext;

    private ViewPager mViewPager;

    private String[] mUrlData;

    private int[] mResData;

    private Banner.OnPageClickListener mOnPageClickListener;

    private Banner.OnPageSwitchListener mOnPageSwitchListener;

    private CountDownTimer mTimer;

    public BannerAdapter(Context context) {
        this.mContext = context;
        this.mIvContainer = new SparseArray<>();
    }

    public void onAttachToViewPager(ViewPager vp) {
        this.mViewPager = vp;
        this.mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        this.mViewPager.setOnTouchListener(this);
    }

    public void setOnPageClickListener(Banner.OnPageClickListener listener) {
        this.mOnPageClickListener = listener;
    }

    public void setOnPageSwitchListener(Banner.OnPageSwitchListener listener) {
        this.mOnPageSwitchListener = listener;
    }

    public void setResData(int[] mResData) {
        if (mResData == null || mResData.length == 0) {
            return;
        }
        this.mResData = mResData;
        this.mIvContainer.clear();
        this.mUrlData = null;
        this.notifyDataSetChanged();
        this.mViewPager.setCurrentItem(0);
        startTimer();
    }

    public void setUrlData(String[] mUrlData) {
        if (mUrlData == null || mUrlData.length == 0) {
            return;
        }
        this.mUrlData = mUrlData;
        this.mIvContainer.clear();
        this.mResData = null;
        this.notifyDataSetChanged();
        this.mViewPager.setCurrentItem(0);
        startTimer();
    }

    public void setInterval(int interval) {
        this.mInterval = interval;
    }

    public void setAdjustDuration(int duration) {
        if(duration >= DEFAULT_ADJUST_DURATION){
            this.mAdjustDuration = duration;
        }
    }

    private void startTimer() {
        if (mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
            this.sInitTimer = true;
        }
        this.mTimer = new CountDownTimer(Integer.MAX_VALUE, mInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (sInitTimer) {
                    sInitTimer = false;
                    return;
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }

            @Override
            public void onFinish() {

            }
        };
        this.mTimer.start();
    }

    /**
     * return the double size of data length , to fake the effect of cycle
     *
     * @return
     */
    @Override
    public int getCount() {
        int count = 0;
        if (mResData != null || mUrlData != null) {
            count = mResData == null ? mUrlData.length : mResData.length;
            count *= 2;
        }
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if((mResData == null && mUrlData == null) || mImageLoader == null){
            return null;
        }
        boolean sResData = (mResData != null);
        ImageView iv = mIvContainer.get(position);
        if (iv == null) {
            iv = new ImageView(mContext);
            iv.setOnClickListener(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if(sResData)
                mImageLoader.loadImage(mContext,(mResData[position % (getCount() / 2)]),iv);
            else
                mImageLoader.loadImage(mContext,(mUrlData[position % (getCount() / 2)]),iv);
            mIvContainer.put(position, iv);
        }
        container.addView(iv);
        return iv;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * adjust the current position through the ViewPager setCurrentItem
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setCurrentItem(getCount() / 2, false);
                    }
                }, mAdjustDuration);
            } else if (position == (getCount() - 1)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setCurrentItem(getCount() / 2 - 1, false);
                    }
                }, mAdjustDuration);
            }
            mCurrPageIndex = position % (getCount() / 2);
            mOnPageSwitchListener.onChange(mCurrPageIndex);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mIvContainer.get(position));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (mTimer != null) mTimer.cancel();
                break;
            case MotionEvent.ACTION_UP:
                startTimer();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mOnPageClickListener != null)
            mOnPageClickListener.onClick(mCurrPageIndex);
    }

}
