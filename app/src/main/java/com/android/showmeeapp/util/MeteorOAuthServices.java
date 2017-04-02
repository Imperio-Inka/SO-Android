package com.android.showmeeapp.util;

import android.util.Base64;

import java.nio.charset.Charset;

/**
 * Created by kushahuja on 6/15/16.
 */
public class MeteorOAuthServices {

    public static String google(String clientId) {

        String token = "YCE4ggWJ0n7h7ZnEOHCxSAoJDjqqCOaAYNq3x5hVpBJpNkZX8bCJYQLu4jZzSjEB";
//		String  httpUrl = getHttpUrl();
//		String redirect = httpUrl+"/_oauth/google";
        String redirect = "https://www.loopcowstudio.com" + "/_oauth/google";

        String state = stateParam(token, redirect);

        String scope = "email+https://www.googleapis.com/auth/calendar";
//        String scope = "email";

        String url = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=22026107675-jfeoijhr5qlpvds0v0dtlvpsv8njsioq.apps.googleusercontent.com";
        url += "&approval_prompt=force";
        url += "&access_type=offline";
        url += "&redirect_uri=" + redirect;
        url += "&scope=" + scope;
        url += "&state=" + state;

        return url;
    }

    private static String stateParam(String credentialToken, String redirectUrl) {


        String objectString = "{\"redirectUrl\":\"" + redirectUrl + "\",\"loginStyle\":\"redirect\",\"isCordova\":\"false\",\"credentialToken\":\"" + credentialToken + "\"}";


        return toBase64(objectString);
    }

    private static String toBase64(String str) {
        byte[] bytesEncoded = Base64.encode(str.getBytes(), Base64.DEFAULT);
        return new String(bytesEncoded, Charset.forName("UTF-8"));

    }


    public static String getHttpUrl() {
        String httpUrl = "https://www.loopcowstudio.com";


        return httpUrl;
    }
}
