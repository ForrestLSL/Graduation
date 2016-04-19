package com.lsl.graduation.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;

import com.lsl.graduation.R;
import com.lsl.graduation.bean.NewsModel;
import com.lsl.graduation.net.context.BitmapContext;
import com.lsl.graduation.utils.UIHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/14.
 */
public class HeaderView extends View{
    private ViewPager ImagerPager;
    private ViewPager TextPager;
    private RadioGroup group;
    private List<NewsModel> datas;
    private PagerAdapter mAdapter;
    private List<ImageView> viewList = new ArrayList<ImageView>();

    public HeaderView(Context context,List<NewsModel> datas) {
        super(context);
        this.datas=datas;
        initView(context);

    }
    public HeaderView(Context context, AttributeSet attrs,List<NewsModel> datas) {
        super(context, attrs);
        this.datas=datas;
        initView(context);
    }
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr,List<NewsModel> datas) {
        super(context, attrs, defStyleAttr);
        this.datas=datas;
        initView(context);
    }

    /**
     * 初始化View
     */
    private void initView(Context context) {
        LinearLayout header= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.fragment_header,null);
        ImagerPager= (ViewPager) header.findViewById(R.id.header_image_pager);
        TextPager= (ViewPager) header.findViewById(R.id.header_text_pager);
        group= (RadioGroup) header.findViewById(R.id.header_group);
        initViewPager(context);

    }

    private void initViewPager(final Context context) {
        viewList.clear();
        for (NewsModel news:datas) {
            creatItemView(context,news);
            createDot(context);
        }
        ((RadioButton) group.getChildAt(0)).setChecked(true);
        mAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(viewList.get(position
                        % viewList.size()));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ImageView view = viewList.get(position % viewList.size());
                new BitmapContext()
                        .get(((NewsModel) view.getTag()).getImgsrc())
                        .ImageView(view)
                        .flag(BitmapContext.FLAG_CACHE_FIRST)
                        .defaultImage(
                                context.getResources().getDrawable(
                                        R.mipmap.default_image)).load();
                if (view.getParent() != null) {
                    ((ViewPager) view.getParent()).removeView(view);
                }
                ((ViewPager) container).addView(view);
                return view;
            }

            @Override
            public int getCount() {
                if (viewList.size() == 1) {
                    return 1;
                }
                return Integer.MAX_VALUE;
            }

        };
        ImagerPager.setAdapter(mAdapter);
        ImagerPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ((RadioButton) group.getChildAt(position
                        % group.getChildCount())).setChecked(true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if(arg1 != 0.0f && arg2 != 0){
//                    handler.removeMessages(AUTO_MEG);
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
//                    handler.removeMessages(AUTO_MEG);
//                    handler.sendEmptyMessageDelayed(AUTO_MEG, AUTO_DELAY);
                }
            }
        });
        if (viewList.size() > 1) {
            ImagerPager.setCurrentItem(viewList.size() * 100);
        }
//        initViewPagerScroll();

//        handler.removeMessages(AUTO_MEG);
//        handler.sendEmptyMessageDelayed(AUTO_MEG, AUTO_DELAY);
    }
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(
                    ImagerPager.getContext());
            mScroller.set(ImagerPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }
    public class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 2000;// 滑动速度

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }
    }
    /**
     * 创建ImageView
     * @param context
     * @param newModel
     */
    private void creatItemView(final Context context,NewsModel newModel) {
        ImageView itemView = new ImageView(context);
        itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        itemView.setImageResource(R.mipmap.default_image);
        itemView.setTag(newModel);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.showMsg(context,"点击了图片");
            }
        });
        itemView.setTag(newModel);
        viewList.add(itemView);
    }

    /**
     * 添加底部白点
     * @param context
     */
    private void createDot(Context context) {
        final RadioButton dotButton = new RadioButton(context);
        dotButton.setLayoutParams(new LinearLayout.LayoutParams(getResources()
                .getDimensionPixelSize(R.dimen.dp15), getResources()
                .getDimensionPixelSize(R.dimen.dp15)));
        dotButton.setClickable(false);
        dotButton.setButtonDrawable(R.drawable.shape_circle_grey);
        group.addView(dotButton);
    }
}
