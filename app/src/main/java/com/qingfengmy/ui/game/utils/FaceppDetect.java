package com.qingfengmy.ui.game.utils;

import android.graphics.Bitmap;

import com.qingfengmy.ui.utils.Constants;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by Administrator on 2015/5/14.
 */
public class FaceppDetect {

    private static String webSite = "http://apicn.faceplusplus.com/v2/";

    public interface CallBack {
        void success(JSONObject jsonObject);
        void error(FaceppParseException exception);
    }

    public static void detect(final Bitmap bm, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bmSmall = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] arrays = stream.toByteArray();

                    JSONObject jsonObject = request(arrays);
                    if (callBack != null)
                        callBack.success(jsonObject);
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    if (callBack != null)
                        callBack.error(e);
                }
            }
        }).start();
    }


    private static int httpTimeOut = 30000;
    public static JSONObject request(byte[] data) throws FaceppParseException {
        HttpURLConnection urlConn = null;

        JSONObject var10;
        try {
            URL url = new URL(webSite + "detection" + "/" + "detect");
            urlConn = (HttpURLConnection)url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(httpTimeOut);
            urlConn.setReadTimeout(httpTimeOut);
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("connection", "keep-alive");
            String boundary = getBoundary();
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            MultipartEntity e = new MultipartEntity(HttpMultipartMode.STRICT, boundary, Charset.forName("UTF-8"));
            e.addPart("img", new ByteArrayBody(data, "NoName"));
            e.addPart("api_key", new StringBody(Constants.APIKEY));
            e.addPart("api_secret", new StringBody(Constants.APISECRET));
            e.writeTo(urlConn.getOutputStream());
            String resultString = null;
            if(urlConn.getResponseCode() == 200) {
                resultString = readString(urlConn.getInputStream());
            } else {
                resultString = readString(urlConn.getErrorStream());
            }

            JSONObject result = new JSONObject(resultString);
            if(result.has("error")) {
                if(result.getString("error").equals("API not found")) {
                    throw new FaceppParseException("API not found");
                }

                throw new FaceppParseException("API error.", result.getInt("error_code"), result.getString("error"), urlConn.getResponseCode());
            }

            result.put("response_code", urlConn.getResponseCode());
            urlConn.getInputStream().close();
            var10 = result;
        } catch (Exception var13) {
            throw new FaceppParseException("error :" + var13.toString());
        } finally {
            if(urlConn != null) {
                urlConn.disconnect();
            }

        }

        return var10;
    }


    private static String readString(InputStream is) {
        StringBuffer rst = new StringBuffer();
        byte[] buffer = new byte[1048576];
        boolean len = false;

        int var6;
        try {
            while((var6 = is.read(buffer)) > 0) {
                for(int e = 0; e < var6; ++e) {
                    rst.append((char)buffer[e]);
                }
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return rst.toString();
    }


    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }

        return sb.toString();
    }
}
