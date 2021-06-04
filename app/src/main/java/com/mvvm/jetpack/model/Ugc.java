package com.mvvm.jetpack.model;

import androidx.databinding.Bindable;

import java.io.Serializable;

public class Ugc implements Serializable {
    public int likeCount;
    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasdiss;

    public boolean hasLiked;

//    @Bindable
//    public boolean isHasLiked() {
//        return hasLiked;
//    }
//
//    public void setHasLiked(boolean hasLiked) {
//        if (this.hasLiked == hasLiked)
//            return;
//        if (hasLiked) {
//            likeCount = likeCount + 1;
//            setHasdiss(false);
//        } else {
//            likeCount = likeCount - 1;
//        }
//        this.hasLiked = hasLiked;
//        notifyPropertyChanged(BR._all);
//    }
}
