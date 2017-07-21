package com.qtfreet.musicuu.musicApi.MusicService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qtfreet on 2017/2/6.
 */
public class Util {
    //将秒数转为时间
    public static String secTotime(int seconds) {

        int temp;
        StringBuilder sb = new StringBuilder();
        temp = seconds / 3600;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = seconds % 3600 / 60;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = seconds % 3600 % 60;
        sb.append((temp < 10) ? "0" + temp : "" + temp);
        return sb.toString();
    }

    public static String UrlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    public static String RegexString(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static boolean isNumber(String text) {
        return Pattern.matches("^\\d+$", text);
    }


    public static String getMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return result;
    }

    public static String getXiaMp3Url(String raw) {
        String url = "";
        try {
            int num = Integer.parseInt(raw.substring(0, 1));
            String str = raw.substring(1);
            int num2 = str.length() % num;
            int length = (int) Math.ceil(((double) str.length()) / ((double) num));
            String[] strArray = new String[num];

            int startIndex = 0;
            for (int i = 0; i < num; i++) {
                if (i < num2) {
                    strArray[i] = str.substring(startIndex, startIndex + length);
                    startIndex = startIndex + length;
                } else if (num2 == 0) {
                    strArray[i] = str.substring(startIndex, startIndex + length);
                    startIndex = startIndex + length;
                } else {
                    strArray[i] = str.substring(startIndex, startIndex + length - 1);
                    startIndex = startIndex + length - 1;
                }
            }

            StringBuilder builder = new StringBuilder();
            if (num2 == 0) {
                for (int j = 0; j < length; j++) {
                    for (int k = 0; k < num; k++) {
                        builder.append(strArray[k].substring(j, j + 1));
                    }
                }
            } else {
                for (int m = 0; m < length; m++) {
                    if (m == (length - 1)) {
                        for (int n = 0; n < num2; n++) {
                            builder.append(strArray[n].substring(m, m + 1));
                        }
                    } else {
                        for (int num10 = 0; num10 < num; num10++) {
                            builder.append(strArray[num10].substring(m, m + 1));
                        }
                    }
                }
            }

            String input = URLDecoder.decode(builder.toString(),"UTF-8");
            if (input != null) {
                String one = input.replaceAll("\\^", "0");
                String two = one.replaceAll("\\+", " ");
                return two.replaceAll("\\.mp$", ".mp3");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
