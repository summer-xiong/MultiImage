package com.example.administrator.imagemultiselecte.imageSelector;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/19.
 */
public class BucketBuild {
    private HashMap<Long, Bucket>bMap = new HashMap<>();
    private Bucket mLatestBucket = new Bucket();

    /**最近图片的张数， 显示在最近相册中*/
    private int maxLatest = 50;

    public BucketBuild(Context context){
        buildBucket(context);
    }

    public Bucket getBucket(Long bucket_id){
        return bMap.get(bucket_id);
    }
    /**最近使用图片*/
    public Bucket getLatestBucket(){
        return mLatestBucket;
    }

    public LinkedList<Bucket> getBucketList(){
        LinkedList<Bucket> bucket_list = new LinkedList<>();
        Iterator it = bMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            bucket_list.add((Bucket)me.getValue());
        }

        return bucket_list;
    }

    private void buildBucket(Context context) {
        Map<Long, String> thumbnail = thumbnail(context);
        ContentResolver r = context.getContentResolver();
        Bucket bucket; ImageItem ii;
        Cursor cursor = MediaStore.Images.Media.query(r, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);
        if (null != cursor) {

            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                int data = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                int date_taken = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
                int bucket_id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID);
                int bucket_name = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);

                if (id > -1) {
                    do {
                        bucket = bMap.get(cursor.getLong(bucket_id));
                        if(bucket == null){
                            bucket = new Bucket();
                            bucket.bucket_id = cursor.getLong(bucket_id);
                            bucket.bucket_name = cursor.getString(bucket_name);
                            bucket.images = new LinkedList<ImageItem>();

                            bMap.put(bucket.bucket_id, bucket);
                        }
                        bucket.count ++;
                        ii = new ImageItem();
                        ii.id = cursor.getLong(id);
                        ii.data = cursor.getString(data);
                        ii.date_taken = cursor.getString(date_taken);
                        ii.thumbnail = thumbnail.get(ii.id);
                        bucket.images.add(ii);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        }

        performLatest();
    }


    private Map<Long, String> thumbnail(Context context) {
        Map<Long, String> m = new HashMap<Long, String>();
        ContentResolver r = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Thumbnails.query(r, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
                int data = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                int image_id = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                if (id > -1) {
                    do {
                        m.put(cursor.getLong(image_id), cursor.getString(data));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        }
        return m;
    }


    //最近使用， 排序
    private void performLatest(){
        LinkedList<ImageItem> all = new LinkedList<>();
        Iterator it = bMap.entrySet().iterator();
        Map.Entry me; Bucket bucket;
        while(it.hasNext()){
            me = (Map.Entry)it.next();
            bucket = (Bucket)me.getValue();

            all.addAll(bucket.images);
        }

        ComparetorImageItem(all);
        if(all.size() > maxLatest){
            mLatestBucket.count = maxLatest;
            mLatestBucket.bucket_id = 0l;
            mLatestBucket.bucket_name = "最近使用";
            mLatestBucket.images = new LinkedList<ImageItem>();
            mLatestBucket.images.addAll(all.subList(all.size() - maxLatest - 1, all.size()));
        }else{
            mLatestBucket.count = all.size();
            mLatestBucket.bucket_id = 0l;
            mLatestBucket.bucket_name = "最近使用";
            mLatestBucket.images = new LinkedList<ImageItem>();
            mLatestBucket.images.addAll(all);
        }
    }



    public class ImageItemComparetor implements Comparator<ImageItem> {
        @Override
        public int compare(ImageItem lhs, ImageItem rhs) {
            long l, r;
            try{
                l = Long.valueOf(lhs.date_taken);
            }catch (NumberFormatException e){
                l = 0l;
            }

            try{
                r = Long.valueOf(rhs.date_taken);
            }catch (NumberFormatException e){
                r = 0l;
            }

            if(l > r)
                return 1;
            else if(l < r)
                return -1;
            else
                return 0;
        }
    }

    public final void ComparetorImageItem(LinkedList<ImageItem> list){
        Collections.sort(list, this.new ImageItemComparetor());
    }
}
