package com.whosmyqueen.easypublicproject.adapter.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whosmyqueen.easypublicproject.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * 封装控件行布局的每一行数据
 */
public class ViewHolder {
    private View mCurrentView;//当前行对象
    private int mPosition;//当前行的position
    private SparseArray<View> views;//当前行对象上的所有控件
    private ImageOptions mImageOptions;

    public void setPosition(int position) {
        mPosition = position;
    }

    //1:
    public ViewHolder(Context context, int position, ViewGroup parent, int layoutRes) {
        this.mPosition = position;
        this.mCurrentView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        this.views = new SparseArray<View>();
        this.mCurrentView.setTag(this);
        mImageOptions = new ImageOptions.Builder().setFadeIn(true).setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.me)
                .build();
    }


    //2:
    public <T extends View> T getView(int viewId) {    //R.id.iv_head
        View view = views.get(viewId);
        if (view == null) {
            view = mCurrentView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public void setImageView(int viewId, String imgUrl) {
        ImageView imageView = getView(viewId);
        x.image().bind(imageView, imgUrl, mImageOptions);
    }

    //3:
    public void setImageView(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
    }

    public void setTextView(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    //4:
    public View getCurrentView() {
        return mCurrentView;
    }

    public int getPosition() {
        return mPosition;
    }
}
