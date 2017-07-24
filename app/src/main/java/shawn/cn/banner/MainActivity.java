package shawn.cn.banner;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import shawn.cn.libaray.banner.Banner;

public class MainActivity extends AppCompatActivity implements Banner.OnPageClickListener {

    public static final String[] URLS  ={
            "http://upload-images.jianshu.io/upload_images/1590087-22972e117b4d5649." +
                    "jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "http://upload-images.jianshu.io/upload_images/1590087-f02e0eeed817e576." +
                    "jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "http://upload-images.jianshu.io/upload_images/1590087-3ba4fb3414e1dc28" +
                    ".jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"
    };

    public static final int[] RES = {R.mipmap.gtr,R.mipmap.link,R.mipmap.poroc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ((Banner)findViewById(R.id.banner))
                .setCurrentIndicatorDrawable(getIndicatorDrawable(R.drawable.indicator_current_vp))
                .setNormalIndicatorDrawable(getIndicatorDrawable(R.drawable.indicator_normal_vp))
                .adjustScrollerDuration(400)
                .setIntervalDuration(3000)
                .setIndicatorBottomMargin(10)
                .setOnPageClickListener(this)
                .setData(URLS);
    }

    @Override
    public void onClick(int currPage) {
        Toast.makeText(this,"click index"+currPage,Toast.LENGTH_SHORT).show();
    }

    private Drawable getIndicatorDrawable(int res){
        return getResources().getDrawable(res);
    }


}
