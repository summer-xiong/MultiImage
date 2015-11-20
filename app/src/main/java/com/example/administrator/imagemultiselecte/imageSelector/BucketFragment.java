package com.example.administrator.imagemultiselecte.imageSelector;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;


import com.example.administrator.imagemultiselecte.R;

import java.util.LinkedList;


public class BucketFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    private LinkedList<Bucket> buckets;

    private OnBucketListener mListener;
    private GridView gv_bucket;
    private BucketAdapter bucketAdapter;

    private Button btn_header_left;
    private Button btn_header_right;

    public static BucketFragment newInstance(LinkedList<Bucket> bk) {
        BucketFragment fragment = new BucketFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, bk);
        fragment.setArguments(args);
        return fragment;
    }

    public BucketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            buckets = (LinkedList<Bucket>)getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bucket, container, false);

        btn_header_left = (Button)view.findViewById(R.id.btn_header_left);
        btn_header_left.setOnClickListener(this);
        btn_header_right = (Button)view.findViewById(R.id.btn_header_right);
        btn_header_right.setOnClickListener(this);


        gv_bucket = (GridView)view.findViewById(R.id.gv_bucket);
        bucketAdapter = new BucketAdapter(getActivity(), buckets);
        gv_bucket.setAdapter(bucketAdapter);

        gv_bucket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener != null){
                    Bucket bucket = (Bucket)parent.getAdapter().getItem(position);
                    mListener.onBucketItemClick(position, bucket.bucket_id);
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBucketListener) activity;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_header_left:
                getActivity().finish();
                break;
            case R.id.btn_header_right:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    public interface OnBucketListener {

        public void onBucketCompleted();

        public void onBucketItemClick(int position, Long id);
    }

}
