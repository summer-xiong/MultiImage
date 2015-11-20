package com.example.administrator.imagemultiselecte.imageSelector;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.imagemultiselecte.R;

import java.util.LinkedList;

/**
 * Created by Administrator on 2015/11/19.
 */
public class BucketAdapter extends BaseAdapter{

    private LinkedList<Bucket> bucket_list;
    private Context mContext = null;
    private LayoutInflater inflater = null;

    public BucketAdapter(Context context, LinkedList<Bucket>buckets){
        mContext = context;
        inflater = LayoutInflater.from(context);
        bucket_list = buckets;
    }

    @Override
    public int getCount() {
        return bucket_list.size();
    }

    @Override
    public Object getItem(int position) {
        return bucket_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_bucket, null);
            holder = new ViewHolder();
            holder.iv_image = (ImageView)convertView.findViewById(R.id.iv_content);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_num = (TextView)convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        ImageItem image = bucket_list.get(position).images.get(0);
        holder.url = image.data;
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
        holder.tv_num.setText(String.valueOf(bucket_list.get(position).count));
        holder.tv_name.setText(bucket_list.get(position).bucket_name);
        return convertView;
    }


    public class ViewHolder{
        String url;
        ImageView iv_image;
        TextView tv_name;
        TextView tv_num;
    }
}
