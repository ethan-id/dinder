package com.example.dinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * VolleySingleton using Android Volley to manage HTTP Requests being sent to and from the Springboot Server
 */
public class VolleySingleton {
    /**
     * The instance of the VolleySingleton class; Static so that there are not more than one instances within the app
     */
    private static VolleySingleton instance;
    /**
     * The RequestQueue used by Volley to execute requests added to it
     */
    private RequestQueue requestQueue;
    /**
     * The ImageLoader used by Volley to load images
     */
    private ImageLoader imageLoader;
    /**
     * Static context provided to the Singleton on instantiation
     */
    private static Context ctx;

    /**
     * Constructor used to instantiate the VolleySingleton class
     * @param context  Conetext of the Android Application
     */
    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * Retrieves the singleton instance of the VolleySingleton class, initializing it if necessary.
     *
     * @param context The application's context, used for initializing the VolleySingleton if needed.
     * @return The singleton instance of the VolleySingleton class.
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * Retrieves the request queue for handling network requests. If the request queue is not initialized,
     * it creates a new one using the application's context.
     *
     * @return The request queue used for handling network requests.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds a given request to the request queue for execution.
     *
     * @param req The request to be executed.
     * @param <T> The type of the parsed response this request expects.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Retrieves the image loader used for loading and caching images.
     *
     * @return The image loader instance.
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}
