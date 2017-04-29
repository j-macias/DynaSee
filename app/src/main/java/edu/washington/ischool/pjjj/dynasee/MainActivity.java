package edu.washington.ischool.pjjj.dynasee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/*        import com.kongqw.bottomnavigationlib.KqwBottomNavigation;
        import com.kongqw.bottomnavigationlib.OnBottomNavigationSelectedListener;
        import com.kongqw.bottomnavigationlib.ToastUtil;

 */

public class MainActivity extends AppCompatActivity implements OnBottomNavigationSelectedListener {

    private BottomNavigation mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigation = (BottomNavigation) findViewById(R.id.kbn);

        mBottomNavigation.setBottomNavigationSelectedListener(this);
    }

    @Override
    public void onValueSelected(int index) {
        ToastUtil.show(this, "index = " + index);
    }

//    public void rb1(View view){
//        mKqwBottomNavigation.setBottomNavigationClick(1);
//    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
