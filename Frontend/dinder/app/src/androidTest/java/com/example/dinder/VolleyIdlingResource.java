package com.example.dinder;

import androidx.test.espresso.IdlingResource;

import com.android.volley.RequestQueue;

public class VolleyIdlingResource implements IdlingResource {
    private final RequestQueue requestQueue;
    private ResourceCallback resourceCallback;

    public VolleyIdlingResource(RequestQueue queue) {
        this.requestQueue = queue;
    }

    @Override
    public String getName() {
        return VolleyIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        if (requestQueue.getSequenceNumber() > 0) {
            resourceCallback.onTransitionToIdle();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}

