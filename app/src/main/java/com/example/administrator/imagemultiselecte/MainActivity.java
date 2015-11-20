package com.example.administrator.imagemultiselecte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.administrator.imagemultiselecte.imageSelector.ImageItem;
import com.example.administrator.imagemultiselecte.imageSelector.PhotoSelectActivity;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private GridView gv_image;
    private GridAdapter gd;
    private ArrayList<ImageItem> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = new ArrayList<ImageItem>();
        ImageItem image = new ImageItem();
        image.data = "";
        image.date_taken = "";
        image.thumbnail = "";
        images.add(image);
        gv_image = (GridView)findViewById(R.id.gv_image);


        gd = new GridAdapter(this, images);
        gv_image.setAdapter(gd);
        gv_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem image = (ImageItem) parent.getAdapter().getItem(position);
                if (image.data.equals("")) {
                    pickImages();
                }
            }
        });
    }


    private static final int REQUEST_CODE_PICK = 1;
    public void pickImages(){
        Intent intent = new Intent();
        intent.setClass(this, PhotoSelectActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK &&  data != null){
            if(requestCode == REQUEST_CODE_PICK){
                ArrayList<ImageItem> pickedImage = (ArrayList<ImageItem>)data.getSerializableExtra("pickedImage");
                images.addAll(images.size() - 1, pickedImage);

                gd.setImages(images);
            }
        }
    }
}
