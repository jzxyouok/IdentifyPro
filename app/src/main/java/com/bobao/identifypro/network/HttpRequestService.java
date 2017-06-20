package com.bobao.identifypro.network;

import android.content.Context;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;
import com.bobao.identifypro.utils.LogForTest;
import com.bobao.identifypro.utils.StringUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequestService {
    private static final String TAG = HttpRequestService.class.getSimpleName();

    private Context mContext;

    public HttpRequestService(Context context) {
        mContext = context;
    }

    /**
     * @param url
     * @return
     * @throws HttpException //     * @throws HttpNoAvailableException
     */
    public byte[] doPost(String url) throws HttpException {
        return doPost(url, null, null);
    }

    /**
     * @param url
     * @param body
     * @return
     * @throws HttpException //     * @throws HttpNoAvailableException
     */
    public byte[] doPost(String url, byte[] body) throws HttpException {
        return doPost(url, null, body);
    }

    /**
     * @param url
     * @param headers
     * @param body
     * @return
     * @throws HttpException //     * @throws HttpNoAvailableException
     */
    @SuppressWarnings("deprecation")
    public byte[] doPost(String url, HashMap<String, String> headers, byte[] body) throws HttpException {
        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
            throw new HttpException("Http NoAvailable");
        }
        HttpClient httpClient = null;
        try {
            HttpPost hp = new HttpPost(url);
            HttpParams params = new BasicHttpParams();
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
                        try {
                            proxyPort = Integer.parseInt(proxyPortStr);
                        } catch (Exception e) {

                        }
                    }
                } else {
                    proxyHost = Proxy.getHost(mContext);
                    proxyPort = Proxy.getPort(mContext);
                }
                if (proxyHost != null) {
                    HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                    params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                }
            }
            HttpConnectionParams.setConnectionTimeout(params,
                    GlobalConfig.HTTP_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params,
                    GlobalConfig.HTTP_SO_TIMEOUT);
            HttpConnectionParams.setSocketBufferSize(params,
                    GlobalConfig.HTTP_SOCKET_BUFFER_SIZE);
            HttpClientParams.setRedirecting(params, false);
            httpClient = new DefaultHttpClient(params);
            // 头部处理
            if (headers != null && headers.size() > 0) {
                Iterator<String> iters = headers.keySet().iterator();
                while (iters.hasNext()) {
                    String key = iters.next();
                    String value = headers.get(key);
                    hp.addHeader(key, value);
                }
            }
            // 包体处理
            if (body != null) {
                hp.setEntity(new ByteArrayEntity(body));
            }
            HttpContext httpContext = new BasicHttpContext();
            HttpResponse response = httpClient.execute(hp, httpContext);
            if (response == null) {
                return null;
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toByteArray(entity);
                }
            } else if (response.getStatusLine().getStatusCode() == 302
                    || response.getStatusLine().getStatusCode() == 301) {
                String redirectURL = response.getHeaders("location")[0]
                        .getValue();
                return doPost(redirectURL, headers, body);
            }
        } catch (SocketTimeoutException e) {
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    /**
     * @param url
     * @return
     * @throws HttpException //     * @throws HttpNoAvailableException
     */
    public static byte[] doGet(String url, Context ctx) throws HttpException {
        return doGet(url, null, ctx);
    }

    /**
     * @param url
     * @param headers
     * @return
     * @throws HttpException //     * @throws HttpNoAvailableException
     */
    @SuppressWarnings("deprecation")
    public static byte[] doGet(String url, Map<String, String> headers, Context ctx)
            throws HttpException {
//        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
//            throw new HttpException("Http NoAvailable");
//        }
        HttpClient httpClient = null;
        try {
            HttpParams params = new BasicHttpParams();
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
                        try {
                            proxyPort = Integer.parseInt(proxyPortStr);
                        } catch (Exception e) {

                        }
                    }
                } else {
                    proxyHost = Proxy.getHost(ctx);
                    proxyPort = Proxy.getPort(ctx);
                }
                if (proxyHost != null) {
                    HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                    params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                }
            }
            HttpConnectionParams.setConnectionTimeout(params,
                    GlobalConfig.HTTP_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params,
                    GlobalConfig.HTTP_SO_TIMEOUT);
            HttpConnectionParams.setSocketBufferSize(params,
                    GlobalConfig.HTTP_SOCKET_BUFFER_SIZE);
            HttpClientParams.setRedirecting(params, false);
            httpClient = new DefaultHttpClient(params);
            HttpGet hg = new HttpGet(url);
            // 头部处理
            if (headers != null && headers.size() > 0) {
                Iterator<String> iters = headers.keySet().iterator();
                while (iters.hasNext()) {
                    String key = iters.next();
                    String value = headers.get(key);
                    hg.addHeader(key, value);
                }
            }

            HttpContext httpContext = new BasicHttpContext();
            HttpResponse response = httpClient.execute(hg, httpContext);
            if (response == null) {
                return null;
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toByteArray(entity);
                }
            } else if (response.getStatusLine().getStatusCode() == 302
                    || response.getStatusLine().getStatusCode() == 301) {
                String redirectURL = response.getHeaders("location")[0]
                        .getValue();
                return doGet(redirectURL, headers, ctx);
            }
        } catch (SocketTimeoutException e) {
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void downFile(String url, HashMap<String, String> headers,
                         OnFileDownload message, boolean isGet) throws HttpException {
        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
            throw new HttpException("Http NoAvailable");
        }
        HttpClient httpClient = null;
        try {
            HttpParams params = new BasicHttpParams();
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
                        try {
                            proxyPort = Integer.parseInt(proxyPortStr);
                        } catch (Exception e) {

                        }
                    }
                } else {
                    proxyHost = Proxy.getHost(mContext);
                    proxyPort = Proxy.getPort(mContext);
                }
                if (proxyHost != null) {
                    HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                    params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                }
            }
            HttpConnectionParams.setConnectionTimeout(params,
                    GlobalConfig.HTTP_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params,
                    GlobalConfig.HTTP_SO_TIMEOUT);
            HttpConnectionParams.setSocketBufferSize(params,
                    GlobalConfig.HTTP_SOCKET_BUFFER_SIZE);
            HttpClientParams.setRedirecting(params, false);
            httpClient = new DefaultHttpClient(params);
            HttpResponse response = null;
            HttpContext httpContext = new BasicHttpContext();
            if (isGet) {
                HttpGet hg = new HttpGet(url);
                // 头部处理
                if (headers != null && headers.size() > 0) {
                    Iterator<String> iters = headers.keySet().iterator();
                    while (iters.hasNext()) {
                        String key = iters.next();
                        String value = headers.get(key);
                        hg.addHeader(key, value);
                    }
                }
                response = httpClient.execute(hg, httpContext);
            } else {
                HttpPost hp = new HttpPost(url);
                if (headers != null && headers.size() > 0) {
                    Iterator<String> iters = headers.keySet().iterator();
                    while (iters.hasNext()) {
                        String key = iters.next();
                        String value = headers.get(key);
                        hp.addHeader(key, value);
                    }
                }
                response = httpClient.execute(hp, httpContext);
            }

            if (response.getStatusLine().getStatusCode() == 200
                    || response.getStatusLine().getStatusCode() == 206) {
                HttpEntity entity = response.getEntity();
                message.sendMessage(entity.getContent(),
                        entity.getContentLength(), entity.getContentEncoding(),
                        null);
            } else if (response.getStatusLine().getStatusCode() == 301
                    || response.getStatusLine().getStatusCode() == 302) {
                String redirectURL = response.getHeaders("location")[0]
                        .getValue();
                message.sendMessage(null, -1, null, redirectURL);
                downFile(redirectURL, headers, message, isGet);
            }
        } catch (SocketTimeoutException e) {
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        } catch (Exception e) {
            throw new HttpException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * @param url
     * @return
     * @throws HttpException //     * @throws HttpNoAvailableException
     */
    @SuppressWarnings("deprecation")
    public String getContentByURL(Context context, String url)
            throws HttpException {
        GlobalConfig.CURRENT_NETWORK_STATE_TYPE = NetworkTools
                .checkNetworkStatus(context);
        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
            return null;
        }
        StringBuffer res = new StringBuffer();
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL console = new URL(url);
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                // 设置wap代理
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
                        try {
                            proxyPort = Integer.parseInt(proxyPortStr);
                        } catch (Exception e) {

                        }
                    }
                } else {
                    proxyHost = Proxy.getHost(context);
                    proxyPort = Proxy.getPort(context);
                }
                if (proxyHost != null) {
                    SocketAddress sa = new InetSocketAddress(proxyHost,
                            proxyPort);
                    java.net.Proxy proxy = new java.net.Proxy(
                            java.net.Proxy.Type.HTTP, sa);
                    conn = (HttpURLConnection) console.openConnection(proxy);
                } else {
                    conn = (HttpURLConnection) console.openConnection();
                }
            } else {
                conn = (HttpURLConnection) console.openConnection();
            }
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(false); // 不使用缓存
            conn.setInstanceFollowRedirects(false); // 是否自动重定向

            conn.setConnectTimeout(30 * 1000); // 链接超时时间
            conn.setReadTimeout(300 * 1000); // 读取服务器返回数据超时时间
            conn.connect();
            // 获取所有的头部
            Map<String, List<String>> map = conn.getHeaderFields();
            Iterator<String> iters = map.keySet().iterator();
            while (iters.hasNext()) {
                String key = iters.next();
                @SuppressWarnings("unused")
                List<String> values = map.get(key);
            }
            // 流处理
            byte[] buffer = new byte[2 * 1024];
            is = conn.getInputStream();
            int readsize = -1;
            while ((readsize = is.read(buffer)) != -1) {
                byte[] temp = new byte[readsize];
                System.arraycopy(buffer, 0, temp, 0, readsize);
                LogForTest.logW("data: " + new String(temp));
                res.append(new String(temp));
            }
        } catch (SocketTimeoutException e) {
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        }
        return res.toString();
    }

    public interface OnFileDownload {
        public void sendMessage(InputStream stream, long totalSize,
                                Header encoding, String newURL);
    }

    public void downFile1(String url, long downloadedBytes,
                          OnFileDownload message) throws HttpException {
        HttpClient httpclient = null;
        try {
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            httpclient = new DefaultHttpClient(httpParams);
            HttpGet get = new HttpGet(url);
            // 添加http头信息
            get.addHeader("Range", "bytes=" + downloadedBytes + "-");
            get.addHeader("User-Agent", "NetFox");
            HttpResponse response = httpclient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200
                    || response.getStatusLine().getStatusCode() == 206) {
                HttpEntity entity = response.getEntity();
                message.sendMessage(entity.getContent(),
                        entity.getContentLength(), entity.getContentEncoding(),
                        null);
            } else if (response.getStatusLine().getStatusCode() == 301
                    || response.getStatusLine().getStatusCode() == 302) {
                String redirectURL = response.getHeaders("location")[0]
                        .getValue();
                message.sendMessage(null, -1, null, redirectURL);
                downFile1(redirectURL, downloadedBytes, message);
            }
        } catch (ClientProtocolException e) {
            throw new HttpException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new HttpException(e.getMessage());
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        } finally {
            if (httpclient != null) {
                httpclient.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param postUrl POST address.
     * @param params  request parameters.
     */
    @SuppressWarnings("deprecation")
    public static String GetContextByUrlpost(Context context, String postUrl,
                                             Map<String, String> params) {
        GlobalConfig.CURRENT_NETWORK_STATE_TYPE = NetworkTools
                .checkNetworkStatus(context);
        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
            return null;
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(postUrl);
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                // 设置wap代理
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
                        try {
                            proxyPort = Integer.parseInt(proxyPortStr);
                        } catch (Exception e) {

                        }
                    }
                } else {
                    proxyHost = Proxy.getHost(context);
                    proxyPort = Proxy.getPort(context);
                }
                if (proxyHost != null) {
                    SocketAddress sa = new InetSocketAddress(proxyHost,
                            proxyPort);
                    java.net.Proxy proxy = new java.net.Proxy(
                            java.net.Proxy.Type.HTTP, sa);
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            StringBuilder bodyBuilder = new StringBuilder();
            Iterator<Entry<String, String>> iterator = params.entrySet()
                    .iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Entry<String, String> param = iterator.next();
                bodyBuilder.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    bodyBuilder.append('&');
                }
            }
            String body = bodyBuilder.toString();
//            LogUtils.i("Posting '" + body + "' to " + postUrl);
            byte[] bytes = body.getBytes();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(30 * 1000); // 链接超时时间
            conn.setReadTimeout(300 * 1000); // 读取服务器返回数据超时时间
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                LogForTest.logW("result...error");
            } else {
                // 流处理
                byte[] buffer = new byte[2 * 1024];
                is = conn.getInputStream();
                int readsize = -1;
                while ((readsize = is.read(buffer)) != -1) {
                    byte[] temp = new byte[readsize];
                    System.arraycopy(buffer, 0, temp, 0, readsize);
                    LogForTest.logW("http test data: " + new String(temp));
                    return new String(temp);
                }


            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            LogForTest.logW("http test SocketTimeoutException " + e);
        } catch (IOException e) {
            e.printStackTrace();
            LogForTest.logW("http test IOException " + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }

    /**
     * Issue a POST request to the server.
     *
     * @param postUrl POST address.
     * @param params  request parameters.
     */
    @SuppressWarnings("deprecation")
    public static String startPostRequest(Context context, String postUrl,
                                          Map<String, String> params) throws IOException {
        GlobalConfig.CURRENT_NETWORK_STATE_TYPE = NetworkTools
                .checkNetworkStatus(context);
        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
            return null;
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(postUrl);
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                // 设置wap代理
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
//                        try{
                        proxyPort = Integer.parseInt(proxyPortStr);
//                        }catch (Exception e){
//
//                        }
                    }
                } else {
                    proxyHost = Proxy.getHost(context);
                    proxyPort = Proxy.getPort(context);
                }
                if (proxyHost != null) {
                    SocketAddress sa = new InetSocketAddress(proxyHost,
                            proxyPort);
                    java.net.Proxy proxy = new java.net.Proxy(
                            java.net.Proxy.Type.HTTP, sa);
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            StringBuilder bodyBuilder = new StringBuilder();
            Iterator<Entry<String, String>> iterator = params.entrySet()
                    .iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Entry<String, String> param = iterator.next();
                bodyBuilder.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    bodyBuilder.append('&');
                }
            }
            String body = bodyBuilder.toString();
//            LogUtils.i("Posting '" + body + "' to " + postUrl);
            byte[] bytes = body.getBytes();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(30 * 1000); // 链接超时时间
            conn.setReadTimeout(300 * 1000); // 读取服务器返回数据超时时间
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status == 200) {
                // 流处理
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len = -1;
                byte buf[] = new byte[1024 * 10];
                while ((len = in.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                return bos.toString();
            } else {
                LogForTest.logW("result...error");
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }


    public static String GetContextByUrlpostFile(Context context, String postUrl,
                                                 Map<String, String> params) {
        GlobalConfig.CURRENT_NETWORK_STATE_TYPE = NetworkTools
                .checkNetworkStatus(context);
        if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_IDLE) {
            return null;
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(postUrl);
            if (GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CMWAP
                    || GlobalConfig.CURRENT_NETWORK_STATE_TYPE == GlobalConfig.NETWORK_STATE_CTWAP) {
                // 设置wap代理
                String proxyHost = null;
                int proxyPort = 80;
                if (Build.VERSION.SDK_INT >= 13) {
                    proxyHost = System.getProperties().getProperty(
                            "http.proxyHost");
                    String proxyPortStr = System.getProperties()
                            .getProperty("http.proxyPort");
                    if (!TextUtils.isEmpty(proxyPortStr)) {
                        try {
                            proxyPort = Integer.parseInt(proxyPortStr);
                        } catch (Exception e) {

                        }
                    }

                } else {
                    proxyHost = Proxy.getHost(context);
                    proxyPort = Proxy.getPort(context);
                }
                if (proxyHost != null) {
                    SocketAddress sa = new InetSocketAddress(proxyHost,
                            proxyPort);
                    java.net.Proxy proxy = new java.net.Proxy(
                            java.net.Proxy.Type.HTTP, sa);
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            StringBuilder bodyBuilder = new StringBuilder();
            Iterator<Entry<String, String>> iterator = params.entrySet()
                    .iterator();
            // constructs the POST body using the parameters
            while (iterator.hasNext()) {
                Entry<String, String> param = iterator.next();
                bodyBuilder.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    bodyBuilder.append('&');
                }
            }
            String body = bodyBuilder.toString();
//            LogUtils.i("Posting '" + body + "' to " + postUrl);
            byte[] bytes = body.getBytes();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(30 * 1000); // 链接超时时间
            conn.setReadTimeout(300 * 1000); // 读取服务器返回数据超时时间
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("connection", "keep-alive");
            //conn.setRequestProperty("Charsert" ,"UTF-8");


            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "*");
            conn.setRequestProperty("Accept-Encoding", "*");
//            conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                LogForTest.logW("result...error");
            } else {
                // 流处理
                byte[] buffer = new byte[2 * 1024];
                is = conn.getInputStream();
                int readsize = -1;
                while ((readsize = is.read(buffer)) != -1) {
                    byte[] temp = new byte[readsize];
                    System.arraycopy(buffer, 0, temp, 0, readsize);
                    LogForTest.logW("http test data: " + new String(temp));
                    return new String(temp);
                }


            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            LogForTest.logW("http test SocketTimeoutException " + e);
        } catch (IOException e) {
            e.printStackTrace();
            LogForTest.logW("http test IOException " + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }


    public static int postFile(String actionUrl, File file
    ) throws IOException {

        if (actionUrl == null || file == null ||
                actionUrl.isEmpty())
            return -1;

        BufferedReader in = null;
        String result = "";

        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 3000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", "application/octet-stream");


        DataOutputStream outStream = new DataOutputStream(conn
                .getOutputStream());

        LogForTest.logW("back up file " + file.toString());

        // 发送文件数据
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        is.close();

        outStream.flush();

        // 得到响应码
        int res = conn.getResponseCode();

        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }

        LogForTest.logW("backup result " + result);

        outStream.close();
        conn.disconnect();
        return res;

    }


    public static int postFile(String actionUrl, String file
    ) throws IOException {

        if (actionUrl == null || file == null ||
                actionUrl.isEmpty())
            return -1;

        BufferedReader in = null;
        String result = "";

        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 3000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", "application/octet-stream");


        DataOutputStream outStream = new DataOutputStream(conn
                .getOutputStream());

        LogForTest.logW("back up file " + file);

        // 发送文件数据
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }

        is.close();

        outStream.flush();

        // 得到响应码
        int res = conn.getResponseCode();

        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }

        LogForTest.logW("backup result " + result);

        outStream.close();
        conn.disconnect();
        return res;

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static int sendPostGetFile(String url, String param, String path) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {

            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(5 * 3000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            //conn.setUseCaches(false);
            conn.setRequestMethod("POST"); // Post方式
            conn.setRequestProperty("connection", "keep-alive");
            //conn.setRequestProperty("Charsert" ,"UTF-8");


            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "*");
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //conn.setRequestProperty("Content-Type", "application/octet-stream");

            //byte[] bytes = param.getBytes();

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数

            out.print(param);
//            out.write(b);

            // flush输出流的缓冲
            out.flush();


            //获取所下载文件的InputStream对象
            InputStream inputStream = conn.getInputStream();
            //返回下载文件的InputStream对象
            //writeFile(inputStream , path);

            // 创建文件对象
            File f = new File(path);
            // 创建文件路径
            if (!f.getParentFile().exists())
                f.getParentFile().mkdirs();


            writeFile(inputStream, path);
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(inputStream));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }


            int res = conn.getResponseCode();
            LogForTest.logW("响应码：" + res);

            return res;

        } catch (Exception e) {
            LogForTest.logW("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                LogForTest.logW("发送 POST 请求出现异常！" + ex);
                ex.printStackTrace();
            }
        }
        return 0;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
//    public static boolean sendPost(Context context, String url, String param) {
//        PrintWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//        try {
//
//            URL uri = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
//            conn.setReadTimeout(5 * 3000);
//            conn.setDoInput(true);// 允许输入
//            conn.setDoOutput(true);// 允许输出
//            //conn.setUseCaches(false);
//            conn.setRequestMethod("POST"); // Post方式
//            conn.setRequestProperty("connection", "keep-alive");
//            //conn.setRequestProperty("Charsert" ,"UTF-8");
//
//
//            conn.setRequestProperty("Accept", "*/*");
//            conn.setRequestProperty("Accept-Charset", "*");
////            conn.setRequestProperty("Accept-Encoding", "gzip");
////            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//            //conn.setRequestProperty("Content-Type", "application/octet-stream");
//
//            //byte[] bytes = param.getBytes();
//
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
////            LogForTest.logW("参数：" + param);
//            out.print(param);
////            out.write(b);
//
//            // flush输出流的缓冲
//            out.flush();
//
//
//            //获取所下载文件的InputStream对象
//            InputStream inputStream = conn.getInputStream();
//            //返回下载文件的InputStream对象
//            //writeFile(inputStream , path);
//
//
//            // 定义BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(
//                    new InputStreamReader(inputStream));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
////            LogForTest.logW("post result：" + result);
////            LogForTest.logW("解密：" + RSAUtils.decrypt(result));
//
//            int res = conn.getResponseCode();
////            LogForTest.logW("响应码：" + res);
////            LogForTest.logW("sendPost result ：" + result);
//
//            //关闭输出流、输入流
//            try {
//                if (out != null) {
//                    out.close();
//                    out = null;
//                }
//                if (in != null) {
//                    in.close();
//                    in = null;
//                }
//            } catch (IOException ex) {
////                LogForTest.logW("发送 POST 请求出现异常！" + ex);
////                ex.printStackTrace();
//            }
//
//            if (res == HttpStatus.SC_OK) {
//
//                if (result.equals("succ")) {
//                    return true;
//                } else {
//                    String deResult = RSAUtils.decryptDictionary(result);
//                    if (deResult != null) {
//                        UserBinaryDictionary.stringToUserDictionary(context, LatinIME.mLatinIME.getLocale(), deResult);
//                        return true;
//                    }
//                }
//            }
//
//
//        } catch (Exception e) {
////            LogForTest.logW("发送 POST 请求出现异常！" + e);
////            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                LogForTest.logW("发送 POST 请求出现异常！" + ex);
//                ex.printStackTrace();
//            }
//        }
//        return false;
//    }

    public static String sendPostForString(Context context, String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {

            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(5 * 3000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setRequestMethod("POST"); // Post方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Charset", "*");

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();


            //获取所下载文件的InputStream对象
            InputStream inputStream = conn.getInputStream();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
//            int res = conn.getResponseCode();

//            Log.e("res",result);
            //关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException ex) {
            }

//            if (res == HttpStatus.SC_OK) {
//
//                return result;
//            }

        } catch (Exception e) {
            result = "";
        }
//使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                LogForTest.logW("发送 POST 请求出现异常！" + ex);
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 写入文件
     *
     * @param inputStream 下载文件的字节流对象
     * @param path        文件的存放目录
     */
    public static void writeFile(InputStream inputStream, String path) {

        OutputStream output = null;
        try {
            //在指定目录创建一个空文件并获取文件对象
            File file = new File(path);
            //获取一个写入文件流对象
            output = new FileOutputStream(file);
            //创建一个4*1024大小的字节数组，作为循环读取字节流的临时存储空

            byte buffer[] = new byte[4 * 1024];
            //循环读取下载的文件到buffer对象数组中
            while ((inputStream.read(buffer)) != -1) {
                //把文件写入到文件
                output.write(buffer);
            }
        } catch (Exception e) {
            // TODO: handleexception
            e.printStackTrace();
            LogForTest.logE("writeFile " + e);
        } finally {
            try {
                //关闭写入流
                output.close();
            } catch (Exception e) {
                // TODO:handle exception
                e.printStackTrace();
                LogForTest.logE("writeFile " + e);
            }
        }
    }


    /**
     * 获取下载文件的InputStream对象
     *
     * @param urlStr 下载文件的地址
     * @return 返回InputStream文件对象
     * @throws MalformedURLException
     * @throws IOException
     */
    public InputStream GetinputStream(String urlStr)
            throws MalformedURLException, IOException {
        //创建按一个URL实例
        URL url = new URL(urlStr);
        //创建一个HttpURLConnection的链接对象
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        //获取所下载文件的InputStream对象
        InputStream inputStream = httpConn.getInputStream();
        //返回下载文件的InputStream对象
        return inputStream;
    }

    public static void upFile() {
        try {
            URL url = new URL("");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(1024 * 1024);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            String fname = "h://hibernate-distribution-3.3.2.GA-dist.zip";
            File file = new File(fname);
            conn.setRequestProperty("Content-Type", "multipart/form-data;file=" + file.getName());
            conn.setRequestProperty("filename", file.getName());
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }


    /**
     * 下载文件保存到本地
     *
     * @param path 文件保存位置
     * @param url  文件地址
     * @throws IOException
     */
//    public static String downloadFile(String url, String path) throws IOException {
//        LogForTest.logW("path:" + path);
//        LogForTest.logW("url:" + url);
//        HttpClient client = null;
//        try {
//            // 创建HttpClient对象
//            client = new DefaultHttpClient();
//            // 获得HttpGet对象
//            HttpPost httpPost = new HttpPost(url);
//            // 发送请求获得返回结果
//            HttpResponse response = client.execute(httpPost);
//            // 如果成功
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                byte[] result = EntityUtils.toByteArray(response.getEntity());
//                String srt2 = new String(result, "UTF-8");
//                LogForTest.logW("downloadFile :" + srt2);
//                BufferedOutputStream bw = null;
//
//
//                try {
//                    // 创建文件对象
//                    File f = new File(path);
//                    // 创建文件路径
//                    if (!f.getParentFile().exists())
//                        f.getParentFile().mkdirs();
//                    // 写入文件
//                    bw = new BufferedOutputStream(new FileOutputStream(path));
//                    bw.write(result);
//
//                    return UserPopupWindow.login_succeed;
//                } catch (Exception e) {
//                    LogForTest.logW("保存文件错误,path=" + path + ",url=" + url + "error:" + e);
//                } finally {
//                    try {
//                        if (bw != null)
//                            bw.close();
//                    } catch (Exception e) {
//                        LogForTest.logW(
//                                "finally BufferedOutputStream shutdown close" + " error:" +
//                                        e);
//                    }
//                }
//            }
//            // 如果失败
//            else {
//                StringBuffer errorMsg = new StringBuffer();
//                errorMsg.append("httpStatus:");
//                errorMsg.append(response.getStatusLine().getStatusCode());
//                errorMsg.append(response.getStatusLine().getReasonPhrase());
//                errorMsg.append(", Header: ");
//                Header[] headers = response.getAllHeaders();
//                for (Header header : headers) {
//                    errorMsg.append(header.getName());
//                    errorMsg.append(":");
//                    errorMsg.append(header.getValue());
//                }
//                LogForTest.logW("HttpResonse Error:" + errorMsg);
//            }
//        } catch (ClientProtocolException e) {
//            LogForTest.logW("下载文件保存到本地,http连接异常,path=" + path + ",url=" + url + "error:" + e);
////            throw e;
//        } catch (IOException e) {
//            LogForTest.logW("下载文件保存到本地,文件操作异常,path=" + path + ",url=" + url + "error:" + e);
////            throw e;
//        } finally {
//            try {
//                client.getConnectionManager().shutdown();
//            } catch (Exception e) {
//
//                LogForTest.logW("finally HttpClient shutdown error" + e);
//
//            }
//        }
//
//
//        return UserPopupWindow.login_failed;
//    }

//    public static final String sendGet() throws Exception{
//
//       // System.out.println(message);
//        String path ="http://54.175.48.3/ad.php";
//        BufferedReader in = null;
//        String result = "";
//        URL url =new URL(path);
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setConnectTimeout(5*1000);
//        conn.setRequestMethod("GET");
//        InputStream inStream = conn.getInputStream();
//       // byte[] data = StreamTool.readInputStream(inStream);
//
//
//        InputStream inputStream = conn.getInputStream();
//        // 定义BufferedReader输入流来读取URL的响应
//        in = new BufferedReader(new InputStreamReader(inputStream));
//        String line;
//        while ((line = in.readLine()) != null) {
//            result += line;
//        }
//        return result;
//        //System.out.println(result);
//    }

    /**
     * 程序中访问http数据接口
     */
    public static String getURLContent(String urlStr) {
        /** 网络的url地址 */
        URL url = null;
        /** http连接 */
        HttpURLConnection httpConn = null;
            /**//** 输入流 */
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str = null;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ex) {
            LogForTest.logW("Exception getURLContent " + ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                LogForTest.logW("Exception getURLContent " + ex);
            }
        }
        String result = sb.toString();
        System.out.println(result);
        return result;
    }


    /**
     * 向指定URL发送GET方法的请求
     * huyongsheng 2015-03-18
     *
     * @param url    发送请求的URL
     * @param params 请求的参数
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(Context context, String url, HashMap<String, String> params) {
        if (params != null) {
            Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
            if (iterator.hasNext()) {
                url = StringUtils.getString(url, "?");
            }
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                url = StringUtils.getString(url, entry.getKey(), "=", entry.getValue());
                if (iterator.hasNext()) {
                    url = StringUtils.getString(url, "&");
                }
            }
        }
        return sendGet(context, url);
    }

    /**
     * 向指定URL发送GET方法的请求
     * huyongsheng 2015-03-18
     *
     * @param url 发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(Context context, String url) {
        String result = "";
        BufferedReader bufferedReader = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result = StringUtils.getString(result, line);
            }
        } catch (Exception e) {
            LogForTest.logW("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}





