package com.mit.mylib.util;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public abstract class MSingleton<T> {
    private T mInstance;

    public MSingleton() {
    }

    protected abstract T create();

    public final T get() {
        synchronized(this) {
            if(this.mInstance == null) {
                this.mInstance = this.create();
            }

            return this.mInstance;
        }
    }
}
