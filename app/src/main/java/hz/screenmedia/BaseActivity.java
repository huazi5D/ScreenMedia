package hz.screenmedia;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2018-01-18.
 */

public class BaseActivity extends Activity {

    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mDensity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.densityDpi;

    }
}
