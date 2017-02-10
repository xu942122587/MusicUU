package com.qtfreet.musicuu.musicApi.MusicService;

import android.util.Base64;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by qtfreet00 on 2017/2/5.
 */
public class NetUtil {
    final static private String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7" +
            "b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280" +
            "104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932" +
            "575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b" +
            "3ece0462db0a22b8e7";
    final static private String nonce = "0CoJUm6Qyw8W8jud";
    final static private String pubKey = "010001";

    public static String GetEncHtml(String url, String text, boolean needCookie) {
        try {
            String param = encryptedRequest(text);
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            if (needCookie) {
                request.addHeader("Cookie", "__remember_me=true; MUSIC_U=5f9d910d66cb2440037d1c68e6972ebb9f15308b56bfeaa4545d34fbabf71e0f36b9357ab7f474595690d369e01fbb9741049cea1c6bb9b6; __csrf=8ea789fbbf78b50e6b64b5ebbb786176; os=uwp; osver=10.0.10586.318; appver=1.2.1; deviceId=0e4f13d2d2ccbbf31806327bd4724043");
            }
            request.setDefineRequestBody(param, "application/x-www-form-urlencoded");
            Response execute;

            execute = NoHttp.startRequestSync(request);
            if (execute.isSucceed()) {
                return execute.get().toString();
            }
        } catch (Exception e) {

        }

        return "";
    }

    public static String GetHtmlContent(String url) {
        return GetHtmlContent(url, false);
    }

    public static String GetHtmlContent(String url, boolean needCookie) {
        try {
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            if (needCookie) {
                request.addHeader("Cookie", "__remember_me=true; MUSIC_U=5f9d910d66cb2440037d1c68e6972ebb9f15308b56bfeaa4545d34fbabf71e0f36b9357ab7f474595690d369e01fbb9741049cea1c6bb9b6; __csrf=8ea789fbbf78b50e6b64b5ebbb786176; os=uwp; osver=10.0.10586.318; appver=1.2.1; deviceId=0e4f13d2d2ccbbf31806327bd4724043");
            }
            Response execute = NoHttp.startRequestSync(request);
            if (execute.isSucceed()) {
                return execute.get().toString();
            }
        } catch (Exception e) {

        }
        return "";
    }


    public static String GetHtmlWithRefer(String url, String refer) {
        try {
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

            request.addHeader("Referer", refer);

            Response execute = NoHttp.startRequestSync(request);
            if (execute.isSucceed()) {
                return execute.get().toString();
            }
        } catch (Exception e) {

        }
        return "";
    }

    public static String PostData(String url, HashMap<String, String> params) {

        try {
            Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            int len = params.size();
            if (len <= 0) {
                return "";
            }
            request.add(params);

            Response execute = NoHttp.startRequestSync(request);
            if (execute.isSucceed()) {
                return execute.get().toString();
            }
        } catch (Exception e) {

        }
        return "";
    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    static String encryptedRequest(String text) {
        String secKey = createSecretKey(16);
        String encText = aesEncrypt(aesEncrypt(text, nonce), secKey);
        String encSecKey = rsaEncrypt(secKey, pubKey, modulus);
        try {
            assert encText != null;
            return "params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey=" + URLEncoder.encode(encSecKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
            return null;
        }
    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    private static String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return Base64.encodeToString(encrypted, 0);
        } catch (Exception ex) {
            //ignore
            return null;
        }
    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    private static String rsaEncrypt(String text, String pubKey, String modulus) {
        text = new StringBuilder(text).reverse().toString();
        BigInteger rs = new BigInteger(String.format("%x", new BigInteger(1, text.getBytes())), 16)
                .modPow(new BigInteger(pubKey, 16), new BigInteger(modulus, 16));
        String r = rs.toString(16);
        if (r.length() >= 256) {
            return r.substring(r.length() - 256, r.length());
        } else {
            while (r.length() < 256) {
                r = 0 + r;
            }
            return r;
        }
    }

    //based on [darknessomi/musicbox](https://github.com/darknessomi/musicbox)
    private static String createSecretKey(int i) {
        return getRandomString(16);
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private static String string = "0123456789abcde";

    private static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }

}
