package com.idol.idolnews;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idol.idolnews.entity.LatestDailyEntity;
import com.idol.idolnews.entity.StoriesEntity;

import java.util.List;

/**
 * Created by 上官若枫 on 2017/10/13.
 */

public class MultipleItemAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
    private  List<String> imageList;
    private  List<String> titleList;
    private List<StoriesEntity> storyList;
    private LatestDailyEntity entity;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultipleItemAdapter(Context context,List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.IMG,R.layout.item_story_list_content);
        addItemType(MultipleItem.TEXT,R.layout.item_story_list_section_head);
    }
    /*public void setDate(LatestDailyEntity entity){
        storyList = entity.getStories();
        imageList = new ArrayList();
        titleList = new ArrayList();
        for(int i = 0;i <  storyList.size();i++){
            imageList.add( storyList.get(i).getImages().get(0));
            titleList.add( storyList.get(i).getTitle());
        }
    }*/



    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
       // setDate(item.getLatestDailyEntity());
        switch (helper.getItemViewType()) {
            case MultipleItem.IMG:
                //helper.getLayoutPosition()
                //helper.setImageResource(R.id.story_iv, imageList.get(helper.getLayoutPosition()+1));
               // helper.setText(R.id.story_title_tv,titleList.get(helper.getLayoutPosition()-2));
                helper.setText(R.id.story_title_tv,item.getEntity().getTitle());
                Glide.with(mContext).load(item.getEntity().getImages().get(0)).crossFade().into((ImageView) helper.getView(R.id.story_iv));
                break;
            case MultipleItem.TEXT:
                if(helper.getLayoutPosition() != 1){
                    helper.setText(R.id.story_list_header,item.getData());
                }
                break;
        }
    }


}

