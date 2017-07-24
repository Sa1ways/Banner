package shawn.cn.libaray.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import shawn.cn.libaray.R;


/**
 * Created by Shawn on 2017/7/23.
 */

public class Banner extends FrameLayout  {

    public static final String TAG = Banner.class.getSimpleName();

    public static final int DEFAULT_INTERVAL = 6000;

    public static final int DEFAULT_SCROLLER_DURATION = 250;

    private int mInterval = DEFAULT_INTERVAL;

    private int mScrollerDuration;

    private int mIndicatorBottomMargin;

    private int mDecorationBgColor;

    private BannerAdapter mBannerAdapter;

    private int mPageSize;

    private ViewPager mViewPager;

    private LinearLayout mDecorationLayout;

    private Drawable mNormalIndicatorDrawable;

    private Drawable mCurrentIndicatorDrawable;

    private boolean sEnableCycle;

    private boolean sEnableAutoScroll;

    public Banner setOnPageClickListener(OnPageClickListener listener) {
        this.mBannerAdapter.setOnPageClickListener(listener);
        return this;
    }

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initView();
    }

    public static void setUpImageLoader(BannerImageLoader loader){
        BannerAdapter.mImageLoader = loader;
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.Banner);
        mDecorationBgColor = array.getColor(R.styleable.Banner_decorationBgColor,Color.TRANSPARENT);
        mNormalIndicatorDrawable = array.getDrawable(R.styleable.Banner_normalIndicatorDrawable);
        mCurrentIndicatorDrawable = array.getDrawable(R.styleable.Banner_currentIndicatorDrawable);
        mScrollerDuration = array.getInt(R.styleable.Banner_scrollerDuration,DEFAULT_SCROLLER_DURATION);
        mInterval = array.getInt(R.styleable.Banner_intervalDuration,DEFAULT_INTERVAL);
        mIndicatorBottomMargin = array.getInt(R.styleable.Banner_bottomMargin,0);
        sEnableAutoScroll = array.getBoolean(R.styleable.Banner_enableAutoScroll,true);
        sEnableCycle = array.getBoolean(R.styleable.Banner_enableCycle,true);
        array.recycle();
    }

    private void initView() {
        // add viewPager into banner
        mViewPager = new ViewPager(getContext());
        mBannerAdapter= new BannerAdapter(getContext());
        mBannerAdapter.setEnableAutoScroll(sEnableAutoScroll);
        mBannerAdapter.setEnableCycle(sEnableCycle);
        mBannerAdapter.setInterval(mInterval);
        mBannerAdapter.onAttachToViewPager(mViewPager);
        mBannerAdapter.setOnPageSwitchListener(mOnPageSwitchListener);
        mViewPager.setAdapter(mBannerAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mViewPager, params);
        adjustScrollerDuration(mScrollerDuration);
    }

    public Banner setEnableCycle(boolean enableCycle) {
        this.sEnableCycle = enableCycle;
        this.mBannerAdapter.setEnableCycle(enableCycle);
        return this;
    }

    public Banner setEnableAutoScroll(boolean enableAutoScroll) {
        this.sEnableAutoScroll = enableAutoScroll;
        this.mBannerAdapter.setEnableAutoScroll(enableAutoScroll);
        return this;
    }

    public Banner setNormalIndicatorDrawable(Drawable drawable) {
        this.mNormalIndicatorDrawable = drawable;
        return this;
    }

    public Banner setCurrentIndicatorDrawable(Drawable drawable) {
        this.mCurrentIndicatorDrawable = drawable;
        return this;
    }

    public Banner setData(String...urls){
        this.mPageSize = urls.length;
        this.mBannerAdapter.setUrlData(urls);
        generateDecoration();
        adjustIndicatorBottomMargin();
        return this;
    }

    public Banner setData(int...resources){
        this.mPageSize = resources.length;
        this.mBannerAdapter.setResData(resources);
        generateDecoration();
        adjustIndicatorBottomMargin();
        return this;
    }

    public Banner setIntervalDuration(int interval){
        mBannerAdapter.setInterval(interval);
        return this;
    }

    public Banner setIndicatorBottomMargin(int bottomMargin) {
        this.mIndicatorBottomMargin = bottomMargin;
        return this;
    }

    public Banner adjustScrollerDuration(int duration){
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            FixedScroller targetScroller = new FixedScroller(getContext(),duration);
            scroller.set(mViewPager,targetScroller);
            mBannerAdapter.setAdjustDuration(duration/3*2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private void generateDecoration() {
        if(mDecorationLayout != null){
            mDecorationLayout.removeAllViews();
        }else{
            mDecorationLayout = new LinearLayout(getContext());
            mDecorationLayout.setGravity(Gravity.CENTER);
            mDecorationLayout.setOrientation(LinearLayout.HORIZONTAL);
            mDecorationLayout.setBackgroundColor(mDecorationBgColor);
            // add decoration into banner
            LayoutParams decorParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            decorParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            decorParams.gravity = Gravity.BOTTOM;
            addView(mDecorationLayout, decorParams);
        }
        // generate circle
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = dp2px(6);
        params.width = dp2px(6);
        params.setMargins(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
        for (int i = 0; i < mPageSize; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageDrawable( i == 0 ?mCurrentIndicatorDrawable:mNormalIndicatorDrawable);
            mDecorationLayout.addView(iv, params);
        }
    }

    private void adjustIndicatorBottomMargin(){
        MarginLayoutParams params = (MarginLayoutParams) mDecorationLayout.getLayoutParams();
        params.bottomMargin = mIndicatorBottomMargin;
        mDecorationLayout.setLayoutParams(params);
    }

    private void adjustIndicator(int position){
        for (int i = 0; i < mDecorationLayout.getChildCount(); i++) {
            ImageView iv = (ImageView) mDecorationLayout.getChildAt(i);
            iv.setImageDrawable(position == i?mCurrentIndicatorDrawable :mNormalIndicatorDrawable);
        }
    }

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    private OnPageSwitchListener mOnPageSwitchListener = new OnPageSwitchListener() {
        @Override
        public void onChange(int currPage) {
            adjustIndicator(currPage);
        }
    };

    public interface OnPageClickListener{
        void onClick(int currPage);
    }

    public interface OnPageSwitchListener{
       void onChange(int currPage);
    }

}
