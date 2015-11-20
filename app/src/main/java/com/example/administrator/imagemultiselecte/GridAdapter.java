package com.example.administrator.imagemultiselecte;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.imagemultiselecte.imageSelector.Bucket;
import com.example.administrator.imagemultiselecte.imageSelector.ImageItem;
import com.example.administrator.imagemultiselecte.imageSelector.LocalImageLoader;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/11/20.
 */
public class GridAdapter extends BaseAdapter {
    private ArrayList<ImageItem> images;
    private Context mContext = null;
    private LayoutInflater inflater = null;

    public GridAdapter(Context context, ArrayList<ImageItem> images){
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.images = images;
    }

    public void setImages(ArrayList<ImageItem>images){
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_image, null);
            holder = new ViewHolder();
            holder.iv_image = (ImageView)convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        ImageItem image = images.get(position);
        holder.url = image.data;
        if(holder.url.equals("")){
            holder.iv_image.setImageResource(R.drawable.icon_unfocused);
        }else{
            holder.iv_image.setImageBitmap(null);
            new LocalImageLoader().displayImage(holder.iv_image, image.thumbnail, image.data, new LocalImageLoader.PerformImage() {
                @Override
                public void performImage(ImageView imageView, Bitmap bitmap, Object... params) {
                    if (imageView != null && bitmap != null) {
                        String url = (String) params[0];
                        if (url != null && url.equals(holder.url)) {
                            ((ImageView) imageView).setImageBitmap(bitmap);
                        }
                    }
                }
            });
        }


        return convertView;
    }


    public class ViewHolder{
        String url;
        ImageView iv_image;
    }
}
