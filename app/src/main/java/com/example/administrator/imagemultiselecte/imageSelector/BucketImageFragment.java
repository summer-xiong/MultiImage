package com.example.administrator.imagemultiselecte.imageSelector;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import com.example.administrator.imagemultiselecte.R;

import java.util.LinkedList;

public class BucketImageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";


    private OnBucketImageListener mListener;
    private Bucket bucket = null;
    private LinkedList<ImageItem> pickedImage = null;
    private int max = 0;


    private Button btn_header_left;
    private Button btn_header_right;
    private TextView tv_header_title;

    private TextView tv_picked;
    private Button btn_done;

    private GridView gv_image = null;
    private BucketImageAdapter bucket_image_adapter = null;
    public static BucketImageFragment newInstance(Bucket bucket, LinkedList<ImageItem> picked, int max) {
        BucketImageFragment fragment = new BucketImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, bucket);
        args.putSerializable(ARG_PARAM2, picked);
        args.putInt(ARG_PARAM3, max);
        fragment.setArguments(args);
        return fragment;
    }

    public BucketImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bucket = (Bucket)getArguments().getSerializable(ARG_PARAM1);
            pickedImage = (LinkedList<ImageItem>) getArguments().getSerializable(ARG_PARAM2);
            max = getArguments().getInt(ARG_PARAM3);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bucket_image, container, false);
        btn_header_left = (Button)view.findViewById(R.id.btn_header_left);
        if(bucket != null && bucket.bucket_name.equals("最近使用")){
            btn_header_left.setText("相册");
        }else{
            //btn_header_left.setBackgroundResource(R.drawable.icon_back_selector);
        }
        btn_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btn_header_right = (Button)view.findViewById(R.id.btn_header_right);
        btn_header_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tv_header_title = (TextView)view.findViewById(R.id.tv_header_title);
        tv_header_title.setText(bucket.bucket_name);

        gv_image = (GridView)view.findViewById(R.id.gv_image);
        bucket_image_adapter = new BucketImageAdapter(getActivity(), bucket, max);
        bucket_image_adapter.setAdapterListener(new BucketImageAdapter.AdapterListener() {
            @Override
            public boolean isPicked(int position) {
                ImageItem image = bucket.images.get(position);
                for (int i = 0; i < pickedImage.size(); i++) {
                    if (image.id == pickedImage.get(i).id) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void setPicked(int position) {
                pickedImage.add(bucket.images.get(position));
                tv_picked.setText("" + pickedImage.size());
            }

            @Override
            public void setUnPicked(int position) {
                ImageItem image = bucket.images.get(position);
                for (int i = 0; i < pickedImage.size(); i++) {
                    if (image.id == pickedImage.get(i).id) {
                        pickedImage.remove(i);
                        break;
                    }
                }

                tv_picked.setText("" + pickedImage.size());
            }

            @Override
            public int getPickedNum() {
                return pickedImage.size();
            }
        });
        gv_image.setAdapter(bucket_image_adapter);

        tv_picked = (TextView)view.findViewById(R.id.tv_picked);
        tv_picked.setText("" + pickedImage.size());


        btn_done = (Button)view.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBucketImageComplete();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBucketImageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnBucketImageListener {
        public void onBucketImageComplete();
    }

}
