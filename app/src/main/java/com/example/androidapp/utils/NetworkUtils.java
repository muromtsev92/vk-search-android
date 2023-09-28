package com.example.androidapp.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private static final String VK_BASE_API_URL = "https://dev.vk.com";
    private static final String VK_USERS_GET = "/ru/method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String PARAM_VERSION = "v";

    public static URL generateURL(String userId) {
        Uri builtUri = Uri.parse( VK_BASE_API_URL + VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(PARAM_USER_ID, userId)
                .appendQueryParameter(PARAM_VERSION, "5.8")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
