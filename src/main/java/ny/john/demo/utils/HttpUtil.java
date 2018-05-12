package ny.john.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/12.
 */
public class HttpUtil {

    public static String get(String uri, Map<String, String> params) throws IOException {
        String tmp = uri + buildParamsStr(params);
        URL           url  = new URL(tmp);
        URLConnection conn = url.openConnection();
        conn.connect();

        BufferedReader in        = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer   inputLine = new StringBuffer();
        String         line;

        while ((line = in.readLine()) != null)
            inputLine.append(line);

        return inputLine.toString();
    }

    private static String buildParamsStr(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();

        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
