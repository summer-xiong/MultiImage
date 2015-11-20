package com.example.administrator.imagemultiselecte.imageSelector;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.imagemultiselecte.R;


/**
 * Created by Administrator on 2015/11/20.
 */
public class BucketImageAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater inflater = null;
    private Bucket bucket;
    private AdapterListener mListener = null;

    private int maxImages = 0;

    public BucketImageAdapter(Context context, Bucket bucket, int max) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.bucket = bucket;
        maxImages = max;
    }

    @Override
    public int getCount() {
        return bucket.images.size();
    }

    @Override
    public Object getItem(int position) {
        return bucket.images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_bucket_image, parent, false);

            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.iv_select = (ImageView)convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.url = bucket.images.get(position).data;
        new LocalImageLoader().displayImage(holder.iv_image, bucket.images.get(position).thumbnail, bucket.images.get(position).data, new LocalImageLoader.PerformImage() {
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

        if(mListener == null){
            return convertView;
        }

        if(mListener.isPicked(position)){
            holder.iv_select.setImageResource(R.drawable.icon_rule_selected);
        }else{
            holder.iv_select.setImageResource(R.drawable.icon_rule_unselected);
        }
        holder.iv_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = holder.url;
                if(mListener.isPicked(position)){
                    holder.iv_select.setImageResource(R.drawable.icon_rule_unselected);
                    mListener.setUnPicked(position);
                }else{
                    int count = mListener.getPickedNum();
                    if(count >= maxImages){
                        Toast.makeText(mContext, "图片张数超过最大限度", Toast.LENGTH_SHORT).show();
                    }else{
                        holder.iv_select.setImageResource(R.drawable.icon_rule_selected);
                        mListener.setPicked(position);
                    }
                }
            }
        });

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = holder.url;
                if(mListener.isPicked(position)){
                    holder.iv_select.setImageResource(R.drawable.icon_rule_unselected);
                    mListener.setUnPicked(position);
                }else{
                    int count = mListener.getPickedNum();
                    if(count >= maxImages){
                        Toast.makeText(mContext, "图片张数超过最大限度", Toast.LENGTH_SHORT).show();
                    }else{
                        holder.iv_select.setImageResource(R.drawable.icon_rule_selected);
                        mListener.setPicked(position);
                    }
                }
            }
        });

        return convertView;
    }

    public void setAdapterListener(AdapterListener listener){
        mListener = listener;
    }

    public class ViewHolder {
        public ImageView iv_image;
        public ImageView iv_select;
        public String url;
    }

    public interface AdapterListener{
        public boolean isPicked(int position);

        public void setPicked(int position);

        public void setUnPicked(int position);

        public int getPickedNum();
    }
}
