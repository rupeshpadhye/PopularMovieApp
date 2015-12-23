//--------------------------------------------------------------------------------------------------
package com.example.android.app.popularmovies.network;
//--------------------------------------------------------------------------------------------------
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//--------------------------------------------------------------------------------------------------
/**
 * This class is helper class to do remote API calls
 * @Author Rupesh Padhye
 */
//--------------------------------------------------------------------------------------------------
public final class RemoteCaller {

    public static String doRemoteCall(URL url,String requestMethod) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result=null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            result= buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
        return result;
    }
}
//--------------------------------------------------------------------------------------------------
//
//--------------------------------------------------------------------------------------------------
