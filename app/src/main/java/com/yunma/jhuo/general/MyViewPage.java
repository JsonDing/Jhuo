package com.yunma.jhuo.general;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.*;
import android.widget.ImageView;

import com.yunma.R;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.utils.AppManager;

import java.util.ArrayList;

/**
 *
 */
public class MyViewPage extends MyCompatActivity {
    /**
     * Viewpager对象
     */
    private ViewPager viewPager;
    private ImageView imageView;
    /**
     * 创建一个数组，用来存放每个页面要显示的View
     */
    private ArrayList<View> pageViews;
    /**
     * 创建一个imageview类型的数组，用来表示导航小圆点
     */
    private ImageView[] imageViews;
    /**
     * 装显示图片的viewgroup
     */
    private ViewGroup viewPictures;
    /**
     * 导航小圆点的viewgroup
     */
    private ViewGroup viewPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        AppManager.getAppManager().addActivity(this);
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<>();
        pageViews.add(inflater.inflate(R.layout.viewpager01, null));
        pageViews.add(inflater.inflate(R.layout.viewpager02, null));
        pageViews.add(inflater.inflate(R.layout.viewpager03, null));
       // pageViews.add(inflater.inflate(R.layout.viewpager04, null));

        // 小圆点数组，大小是图片的个数
        imageViews = new ImageView[pageViews.size()];

        // 从指定的XML文件中加载视图
        viewPictures = (ViewGroup) inflater.inflate(R.layout.viewpager, null);
        viewPager = (ViewPager) viewPictures.findViewById(R.id.guidePagers);
        viewPoints = (ViewGroup) viewPictures.findViewById(R.id.viewPoints);
        setContentView(viewPictures);
        viewPager.setAdapter(new NavigationPageAdapter());
        // 为viewpager添加监听，当view发生变化时的响应
        viewPager.setOnPageChangeListener(new NavigationPageChangeListener());
    }


    private class NavigationPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化每个Item
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));

            return pageViews.get(position);
        }

        // 销毁每个Item
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }

    }

    private class NavigationPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }

        }

        @Override
        public void onPageSelected(int position) {
           /* // 循环主要是控制导航中每个小圆点的状态
            for (int i = 0; i < imageViews.length; i++) {
                // 当前view下设置小圆点为选中状态
                imageViews[i].setImageDrawable(getResources().getDrawable(
                        R.drawable.page_indicator_focused));
                // 其余设置为飞选中状态
                if (position != i)
                    imageViews[i].setImageDrawable(getResources().getDrawable(
                            R.drawable.page_indicator_unfocused));
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    // 开始按钮方法，开始按钮在XML文件中onClick属性设置；
    // 我试图把按钮在本activity中实例化并设置点击监听，但总是报错，使用这个方法后没有报错，原因没找到
    public void startbutton(View v) {
        Intent intent = new Intent(MyViewPage.this, MainActivity.class);
        startActivity(intent);
        AppManager.getAppManager().finishActivity(this);
    }

    public void setSecondAnimal() {

    }

    public void setThirdAnimal() {

    }

    public void setFourthAnimal() {

    }
}
