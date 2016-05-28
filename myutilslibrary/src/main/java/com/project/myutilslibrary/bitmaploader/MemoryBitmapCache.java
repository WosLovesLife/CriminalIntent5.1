package com.project.myutilslibrary.bitmaploader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by zhangH on 2016/5/15.
 */
public class MemoryBitmapCache {
    private LruCache<String,Bitmap> mLruCache;

    public MemoryBitmapCache(Context context) {
        /** 图片缓存最多占用当前应用总内存的1/8 */
        int cacheSize = (int) Runtime.getRuntime().maxMemory()/8;
        Log.w("MemoryBitmapCache", "MemoryBitmapCache: cacheSize: "+cacheSize );
        /** 使用LRU算法,作为缓存集合 */
        mLruCache = new LruCache<String,Bitmap>(cacheSize){
            /** 该类原生按照Value的数量限定缓存数量,这里通过重写sizeOf()方法改为按照空间限定缓存数量 */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public Bitmap getCache(String url) {
        return mLruCache.get(url);
    }

    public void saveCache(Bitmap bitmap, String url) {
        mLruCache.put(url,bitmap);
    }
}
