package com.example.administrator.imagemultiselecte.imageSelector;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.administrator.imagemultiselecte.R;

import java.util.LinkedList;

public class PhotoSelectActivity extends Activity implements BucketFragment.OnBucketListener, BucketImageFragment.OnBucketImageListener {
    private BucketFragment bucket_fragment;
    private BucketImageFragment bucket_ImageFragment;

    private FragmentManager fragmentManager;
    private BucketBuild bucket_build;
    private int MaxImages = 9;
    private LinkedList<ImageItem> pickedImage = new LinkedList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);

        bucket_build = new BucketBuild(this);
        bucket_fragment = BucketFragment.newInstance(bucket_build.getBucketList());

        initFragment();

        int current = getIntent().getIntExtra("current", 0);
        MaxImages = MaxImages -current;
    }


    private void initFragment(){
        fragmentManager = getFragmentManager();
        FragmentTransaction transation = fragmentManager.beginTransaction();

        transation.add(R.id.fl_content, bucket_fragment);
        transation.commit();

        pickedImage.clear();
        bucket_ImageFragment = BucketImageFragment.newInstance(bucket_build.getLatestBucket(), pickedImage, MaxImages);
        transation = fragmentManager.beginTransaction();
        transation.replace(R.id.fl_content, bucket_ImageFragment);
        transation.addToBackStack(null);
        transation.commit();
    }

    @Override
    public void onBucketImageComplete() {
        Intent data = new Intent();
        data.putExtra("pickedImage", pickedImage);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBucketCompleted() {

    }

    @Override
    public void onBucketItemClick(int position, Long id) {
        pickedImage.clear();

        bucket_ImageFragment = BucketImageFragment.newInstance(bucket_build.getBucket(id), pickedImage, MaxImages);
        FragmentTransaction transation = fragmentManager.beginTransaction();
        transation.replace(R.id.fl_content, bucket_ImageFragment);
        transation.addToBackStack(null);
        transation.commit();
    }
}
