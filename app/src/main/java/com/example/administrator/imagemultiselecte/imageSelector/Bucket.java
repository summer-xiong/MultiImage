package com.example.administrator.imagemultiselecte.imageSelector;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/11/19.
 * 图片文件夹类
 */
public class Bucket implements Serializable {

    private static final long serialVersionUID = 8388675188436274732L;
    //文件数
    public int count;
    //文件夹id
    public Long bucket_id;
    //文件夹名称
    public String bucket_name;
    //图片文件
    LinkedList<ImageItem> images;
}
