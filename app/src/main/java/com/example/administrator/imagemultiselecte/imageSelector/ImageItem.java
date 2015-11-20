package com.example.administrator.imagemultiselecte.imageSelector;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/19.
 */
public class ImageItem implements Serializable {
    private static final long serialVersionUID = 8012401588755198558L;
    public Long id;
    //路径
    public String data;
    //时间排序
    public String date_taken;
    //缩略图
    public String thumbnail;
}
