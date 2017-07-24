# Custom Banner
![image](https://github.com/Sa1ways/Banner/blob/master/shot/banner.gif)

# the custom attrs below can be used in the layout.xml

     <attr name="pageSize" format="integer"/>
     <attr name="decorationBgColor" format="color"/>
     <attr name="scrollerDuration" format="integer"/>
     <attr name="intervalDuration" format="integer"/>
     <attr name="bottomMargin" format="integer"/>
     <attr name="normalIndicatorDrawable" format="reference"/>
     <attr name="currentIndicatorDrawable" format="reference"/>
     <attr name="enableAutoScroll" format="boolean"/>
     <attr name="enableCycle" format="boolean"/>

#useage in layout.xml:

      <shawn.cn.libaray.banner.Banner
              android:id="@+id/banner"
              app:intervalDuration="3000"
              app:scrollerDuration="400"
              app:enableAutoScroll="false"
              app:enableCycle="false"
              app:currentIndicatorDrawable="@drawable/indicator_current_vp"
              app:normalIndicatorDrawable="@drawable/indicator_normal_vp"
              android:layout_width="match_parent"
              android:layout_height="220dp"/>
#useage in java code:

      ((Banner)findViewById(R.id.banner))
                      // set the current-page indicator drawable
                      .setCurrentIndicatorDrawable(getIndicatorDrawable(R.drawable.indicator_current_vp))
                      // set the default indicator drawable
                      .setNormalIndicatorDrawable(getIndicatorDrawable(R.drawable.indicator_normal_vp))
                      //  adjust the ViewPager Scroller's Duration through the reflection for smooth page-flip
                      .adjustScrollerDuration(450)
                      // set the Interval between page-flip
                      .setIntervalDuration(5000)
                      // indicator layout bottom margin
                      .setIndicatorBottomMargin(10)
                      // page click callback
                      .setOnPageClickListener(this)
                      // set enable auto scroll or not
                      .setEnableAutoScroll(true)
                      // set enable cycle or not
                      .setEnableCycle(true)
                      // show data( String[] | @ResInt[])
                      .setData(URLS);

