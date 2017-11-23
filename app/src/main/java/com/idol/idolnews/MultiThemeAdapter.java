package com.idol.idolnews;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idol.idolnews.entity.EditorsEntity;
import com.idol.idolnews.loader.GlideImageLoader;

import java.util.List;

/**
 * Created by 53478 on 2017/11/8.
 */

public class MultiThemeAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultiThemeAdapter(Context context, List<MultipleItem> data){
        super(data);
        addItemType(MultipleItem.IMG,R.layout.item_story_list_content);
        addItemType(MultipleItem.TEXT,R.layout.item_theme_story_list_section);
    }
    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.IMG:
                helper.setText(R.id.story_title_tv,item.getEntity().getTitle());
                if(item.getEntity().getImages() == null ){
                    ImageView imageView = (ImageView) helper.getView(R.id.story_iv);
                    imageView.setVisibility(View.GONE);
                }else {
                    Glide.with(mContext).load(item.getEntity().getImages().get(0)).crossFade().into((ImageView) helper.getView(R.id.story_iv));
                }
                break;
                case MultipleItem.TEXT:
                    LinearLayout layout = (LinearLayout) helper.getView(R.id.editor_layout);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45, 45);
                    layoutParams.setMargins(30, 0, 0, 0);
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    layout.removeViews(1,layout.getChildCount()-1);
                    final List<EditorsEntity> editors = item.getThemeContentListEntity().getEditors();
                    int imgCount = editors.size();
                    for (int i = 0;i < imgCount; ++i) {
                        ImageView imageView = new ImageView(mContext);
                        imageView.setLayoutParams(layoutParams);
                        GlideImageLoader.getInstance().displayCircleImage(mContext, editors.get(i).getAvatar(), imageView);
                        layout.addView(imageView);
                    }
                    break;
        }

    }
}
