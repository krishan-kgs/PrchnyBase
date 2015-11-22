/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 19, 2010
 *  @author singla
 */

package com.prchny.base.transport.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.prchny.base.constant.PrchnyWSErrorConstants;
import com.prchny.base.model.common.HttpClientConfig;
import com.prchny.base.utils.StringUtils;

/**
 * @author singla
 * @since 1.0
 */
public class HttpSender {
  
  private static final String CONTENT_ENCODING_UTF_8 = "UTF-8";
  
  private static final String LINE_SEPARATOR = System
      .getProperty("line.separator");
  
  private static final Logger LOG = LoggerFactory.getLogger(HttpSender.class);
  
  private HttpClient httpClient;
  
  private boolean clientCached;
  
  /**
   * @param apiCacheName
   */
  public HttpSender(String apiCacheName) {
  
    this.clientCached = true;
    this.httpClient = HttpClientBuilder.createNewHttpClient(apiCacheName);
  }
  
  /**
   * default constructor
   */
  public HttpSender(String apiCacheName, String proxyHost, Integer proxyPort) {
  
    this.clientCached = true;
    this.httpClient = HttpClientBuilder.createNewHttpClient(apiCacheName);
    HttpHost proxy = new HttpHost(proxyHost, proxyPort);
    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
  }
  
  public HttpSender(String apiCacheName, boolean createIfNotInCache) {
  
    this.clientCached = true;
    this.httpClient = HttpClientBuilder.createNewHttpClient(apiCacheName);
    if (createIfNotInCache && this.httpClient == null) {
      this.httpClient = HttpClientBuilder.createNewHttpClient(apiCacheName);
    }
  }
  
  public HttpSender(String apiCacheName, HttpClientConfig clientConfig) {
  
    this.clientCached = true;
    this.httpClient =
        HttpClientBuilder.createNewHttpClient(apiCacheName, clientConfig);
  }
  
  /**
   * default constructor
   */
  public HttpSender() {
  
    this.httpClient = HttpClientBuilder.buildHttpClient();
    httpClient.getParams();
  }
  
  /**
   * @param timeout
   * @param timeunit
   */
  public void setTimeout(int timeout, TimeUnit timeunit) {
  
    httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
        (int) timeunit.toMillis(timeout));
  }
  
  /**
   * @param url
   * @param params
   * @return
   * @throws HttpTransportException
   */
  public String executeGet(String url, Map<String, String> params)
      throws HttpTransportException {
  
    return executeGet(url, params, null);
  }
  
  /**
   * @param url
   * @param params
   * @param headers
   * @return
   * @throws HttpTransportException
   */
  public String executeGet(String url, Map<String, String> params,
      Map<String, String> headers) throws HttpTransportException {
  
    HttpGet httpGet = new HttpGet(createURL(url, params));
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpGet.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    try {
      HttpResponse response = httpClient.execute(httpGet);
      return getContent(response.getEntity());
    } catch (Exception e) {
      LOG.debug("http get failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http get", e);
    }
  }
  
  /**
   * @param url
   * @param params
   * @param headers
   * @return
   * @throws HttpTransportException
   */
  public String executeGetAndReturnNullFor404(String url,
      Map<String, String> params, Map<String, String> headers)
      throws HttpTransportException {
  
    HttpGet httpGet = new HttpGet(createURL(url, params));
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpGet.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    try {
      HttpResponse response = httpClient.execute(httpGet);
      if (response.getStatusLine().getStatusCode() == 404) {
        return null;
      }
      return getContent(response.getEntity());
    } catch (Exception e) {
      LOG.debug("http get failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http get", e);
    }
  }
  
  public static String createURL(String url, Map<String, String> params) {
  
    if (params != null) {
      StringBuilder builder = new StringBuilder();
      builder.append(url);
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        nvps.add(new BasicNameValuePair(pEntry.getKey(), pEntry.getValue()));
      }
      builder.append('?').append(
          URLEncodedUtils.format(nvps, CONTENT_ENCODING_UTF_8));
      return builder.toString();
    } else {
      return url;
    }
  }
  
  /**
   * @param url
   * @param params
   * @param headers
   * @return
   * @throws HttpTransportException
   */
  public String executePost(String url, Map<String, String> params,
      Map<String, String> headers) throws HttpTransportException {
  
    HttpPost httpPost = new HttpPost(url);
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpPost.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    if (params != null) {
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        nvps.add(new BasicNameValuePair(pEntry.getKey(), pEntry.getValue()));
      }
      try {
        httpPost.setEntity(new UrlEncodedFormEntity(nvps,
            CONTENT_ENCODING_UTF_8));
      } catch (UnsupportedEncodingException e) {
        LOG.debug("unable to encode params:'{}' ", params);
        throw new HttpTransportException("invalid character encoding", e);
      }
    }
    try {
      HttpResponse response = httpClient.execute(httpPost);
      return getContent(response.getEntity());
    } catch (Exception e) {
      LOG.debug("http post failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http post", e);
    }
  }
  
  public String executePostJSon(String url, Map<String, String> params)
      throws HttpTransportException {
  
    HttpPost httpPost = new HttpPost(url);
    JsonObject data = new JsonObject();
    if (params != null) {
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        try {
          data.addProperty(pEntry.getKey(), pEntry.getValue());
        } catch (Exception e) {
          throw new HttpTransportException("Unable to execute http post", e);
        }
      }
    }
    
    // StringEntity se = null;
    try {
      // se = new StringEntity(data.toString());
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      nvps.add(new BasicNameValuePair("JsonData", data.toString()));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, "ISO-8859-1"));
      
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      throw new HttpTransportException("Unable to execute http post", e);
    }
    
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-type", "application/json");
    
    HttpResponse response;
    try {
      response = httpClient.execute(httpPost);
      return getContent(response.getEntity());
    } catch (Exception e) {
      LOG.debug("http post failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http post", e);
    }
  }
  
  /**
   * @param url
   * @param params
   * @return
   * @throws HttpTransportException
   */
  public String executePost(String url, Map<String, String> params)
      throws HttpTransportException {
  
    return executePost(url, params, null);
  }
  
  public String executePostContent(String url, Map<String, String> params,
      Map<String, String> headers, String body) throws HttpTransportException {
  
    StringBuilder sb = new StringBuilder(url);
    if (params != null) {
      if (!sb.toString().contains("?")) {
        sb.append('?');
      }
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        sb.append(pEntry.getKey()).append('=').append(pEntry.getValue())
            .append('&');
      }
    }
    
    HttpPost httpPost = new HttpPost(sb.toString());
    
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpPost.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    httpPost.setEntity(new StringEntity(body, CONTENT_ENCODING_UTF_8));
    try {
      HttpResponse response = httpClient.execute(httpPost);
      return getContent(response.getEntity());
    } catch (Exception e) {
      LOG.debug("http post failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http post", e);
    }
  }
  
  public byte[] executePostContentAndReturnBytes(String url,
      Map<String, String> params, Map<String, String> headers, String body,
      Map<String, Object> httpMethodParams) throws HttpTransportException {
  
    StringBuilder sb = new StringBuilder(url);
    if (params != null) {
      if (!sb.toString().contains("?")) {
        sb.append('?');
      }
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        sb.append(pEntry.getKey()).append('=').append(pEntry.getValue())
            .append('&');
      }
    }
    
    HttpPost httpPost = new HttpPost(sb.toString());
    
    /**
     * Changes for api level http config starts This will set the
     * http.socket.timeout and other parameters to http post if specified in
     * httpclient.properties
     */
    setHttpMethodParams(httpMethodParams, httpPost);
    
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpPost.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    httpPost.setEntity(new StringEntity(body, CONTENT_ENCODING_UTF_8));
    try {
      HttpResponse response = httpClient.execute(httpPost);
      byte[] data = EntityUtils.toByteArray(response.getEntity());
      EntityUtils.consume(response.getEntity());
      return data;
    } catch (Exception e) {
      LOG.debug("http post failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http post", e);
    }
  }
  
  public byte[] executePostContentAndReturnBytes(String url,
      Map<String, String> params, Map<String, String> headers, byte[] body,
      Map<String, Object> httpMethodParams) throws HttpTransportException {
  
    StringBuilder sb = new StringBuilder(url);
    if (params != null) {
      if (!sb.toString().contains("?")) {
        sb.append('?');
      }
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        sb.append(pEntry.getKey()).append('=').append(pEntry.getValue())
            .append('&');
      }
    }
    HttpPost httpPost = new HttpPost(sb.toString());
    
    /**
     * Changes for api level http config starts This will set the
     * http.socket.timeout and other parameters to http post if specified in
     * httpclient.properties
     */
    setHttpMethodParams(httpMethodParams, httpPost);
    
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpPost.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    httpPost.setEntity(new ByteArrayEntity(body));
    HttpResponse response;
    byte[] data;
    try {
      response = httpClient.execute(httpPost);
      data = EntityUtils.toByteArray(response.getEntity());
      EntityUtils.consume(response.getEntity());
    } catch (IOException e) {
      LOG.error("Error http to : {}", url, e);
      throw new HttpTransportException(e, PrchnyWSErrorConstants.IO_ERROR);
    }
    int responseCode = response.getStatusLine().getStatusCode();
    if (responseCode != 200) {
      if (data != null) {
        throw new HttpTransportException(new String(data), responseCode);
      } else {
        throw new HttpTransportException("No data received from server",
            responseCode);
      }
    }
    return data;
  }
  
  /**
   * @param url
   * @param requestXml
   * @param headers
   * @return
   * @throws HttpTransportException
   */
  public String executePost(String url, String requestXml,
      Map<String, String> headers) throws HttpTransportException {
  
    HttpPost httpPost = new HttpPost(url);
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpPost.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    try {
      httpPost.setEntity(new StringEntity(requestXml, "text/xml",
          CONTENT_ENCODING_UTF_8));
    } catch (UnsupportedEncodingException e) {
      throw new HttpTransportException("invalid character encoding", e);
    }
    try {
      HttpResponse response = httpClient.execute(httpPost);
      return getContent(response.getEntity());
    } catch (Exception e) {
      LOG.debug("http post failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http post", e);
    }
  }
  
  public String executeMultipartRequest(String url, Map<String, String> params,
      File file, String fileParamName) throws ClientProtocolException,
      IOException {
  
    HttpPost post = new HttpPost(url);
    MultipartEntity entity =
        new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    entity.addPart("xlsFile", new FileBody(file));
    for (String key : params.keySet()) {
      entity.addPart(key, new StringBody(params.get(key)));
    }
    
    post.setEntity(entity);
    
    return getContent(httpClient.execute(post).getEntity());
  }
  
  public String executeMultipartPost(String url, Map<String, String> params,
      Map<String, File> files) throws IOException {
  
    HttpPost post = new HttpPost(url);
    MultipartEntity entity =
        new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    for (Map.Entry<String, File> entry : files.entrySet()) {
      entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
    }
    if (params != null) {
      for (Map.Entry<String, String> entry : params.entrySet()) {
        entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
      }
    }
    post.setEntity(entity);
    return getContent(httpClient.execute(post).getEntity());
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#finalize()
   */
  @Override
  protected void finalize() throws Throwable {
  
    super.finalize();
    if (!clientCached) {
      httpClient.getConnectionManager().shutdown();
    }
  }
  
  /**
   * @param entity
   * @return
   * @throws IOException
   */
  private static String getContent(HttpEntity entity) throws IOException {
  
    String charset = EntityUtils.getContentCharSet(entity);
    InputStreamReader inputStreamReader = null;
    BufferedReader br = null;
    StringBuilder response = new StringBuilder();
    try {
      if (StringUtils.isNotEmpty(charset)) {
        inputStreamReader = new InputStreamReader(entity.getContent(), charset);
      } else {
        inputStreamReader = new InputStreamReader(entity.getContent());
      }
      br = new BufferedReader(inputStreamReader);
      String line = null;
      while ((line = br.readLine()) != null) {
        response.append(line).append(LINE_SEPARATOR);
      }
      // This is to ensure the resources are released properly
      EntityUtils.consume(entity);
    } finally {
      if (inputStreamReader != null) {
        inputStreamReader.close();
      }
      if (br != null) {
        br.close();
      }
    }
    return response.toString();
  }
  
  public byte[] executeGetAndReturnBytes(String url,
      Map<String, String> params, Map<String, String> headers,
      Map<String, Object> httpMethodParams) throws HttpTransportException {
  
    HttpGet httpGet = new HttpGet(createURL(url, params));
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpGet.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    
    /**
     * Changes for api level http config starts This will set the
     * http.socket.timeout and other parameters to http post if specified in
     * httpclient.properties
     */
    setHttpMethodParams(httpMethodParams, httpGet);
    
    try {
      HttpResponse response = httpClient.execute(httpGet);
      byte[] data = EntityUtils.toByteArray(response.getEntity());
      EntityUtils.consume(response.getEntity());
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != 200) {
        LOG.error(
            "Invalid response received. Response code : {} and  data : {} ",
            responseCode, data);
        String errorMessage =
            "Unknown Error at destination. Response Code : " + responseCode;
        switch (responseCode) {
          case 404:
            errorMessage =
                "Requested URL Does Not exist at destination. Response Code : 404";
            break;
          case 500:
            errorMessage =
                "Internal Server Error at destination. Response Code : 500";
            break;
          case 403:
          case 401:
            errorMessage =
                "Request unauthorized. Response Code : " + responseCode;
            break;
        }
        throw new HttpTransportException(errorMessage, responseCode);
      }
      return data;
    } catch (IOException e) {
      throw new HttpTransportException(e, -101);
    } catch (HttpTransportException e) {
      throw e;
    } catch (Exception e) {
      LOG.debug("http get failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http get", e);
    }
  }
  
  private static byte[] getContentAsBytes(HttpEntity entity) throws IOException {
  
    InputStream is = entity.getContent();
    byte[] result = new byte[(int) entity.getContentLength()];
    is.read(result);
    return result;
  }
  
  /**
   * @param url
   * @param params
   * @param headers
   * @return
   * @throws HttpTransportException
   */
  public void executeGetVoidReturn(String url, Map<String, String> params,
      Map<String, String> headers) throws HttpTransportException {
  
    HttpGet httpGet = new HttpGet(createURL(url, params));
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpGet.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    try {
      httpClient.execute(httpGet);
    } catch (Exception e) {
      LOG.debug("http get failed for url:'{}' ", url);
      throw new HttpTransportException("Unable to execute http get", e);
    }
  }
  
  public boolean isHttpClientCreated() {
  
    return httpClient != null;
  }
  
  private void setHttpMethodParams(Map<String, Object> httpMethodParams,
      HttpRequestBase httpMethod) {
  
    if (httpMethodParams != null && !httpMethodParams.isEmpty()) {
      for (Map.Entry<String, Object> parameter : httpMethodParams.entrySet()) {
        if (StringUtils.isNotEmpty(parameter.getKey())
            && null != parameter.getValue()) {
          httpMethod.getParams().setParameter(parameter.getKey(),
              parameter.getValue());
        }
      }
    }
  }
  
  public byte[] executePutContentAndReturnBytes(String url,
      Map<String, String> params, Map<String, String> headers, byte[] body,
      Map<String, Object> httpMethodParams) throws HttpTransportException {
  
    StringBuilder sb = new StringBuilder(url);
    if (params != null) {
      if (!sb.toString().contains("?")) {
        sb.append('?');
      }
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        sb.append(pEntry.getKey()).append('=').append(pEntry.getValue())
            .append('&');
      }
    }
    HttpPut httpPut = new HttpPut(sb.toString());
    /**
     * 
     * Changes for api level http config starts This will set the
     * http.socket.timeout and other parameters to http
     * 
     * put if specified in httpclient.properties
     */
    setHttpMethodParams(httpMethodParams, httpPut);
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpPut.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    httpPut.setEntity(new ByteArrayEntity(body));
    HttpResponse response;
    byte[] data;
    try {
      response = httpClient.execute(httpPut);
      data = EntityUtils.toByteArray(response.getEntity());
      EntityUtils.consume(response.getEntity());
    } catch (IOException e) {
      LOG.error("Error http to : {}", url, e);
      throw new HttpTransportException(e, PrchnyWSErrorConstants.IO_ERROR);
    }
    int responseCode = response.getStatusLine().getStatusCode();
    throwExceptionOn4xx_5xxStatuses(data, responseCode);
    return data;
  }
  
  public byte[] executeDeleteAndReturnBytes(String url,
      Map<String, String> params, Map<String, String> headers,
      Map<String, Object> httpMethodParams) throws HttpTransportException {
  
    StringBuilder sb = new StringBuilder(url);
    if (params != null) {
      if (!sb.toString().contains("?")) {
        sb.append('?');
      }
      for (Map.Entry<String, String> pEntry : params.entrySet()) {
        sb.append(pEntry.getKey()).append('=').append(pEntry.getValue())
            .append('&');
      }
    }
    HttpDelete httpDelete = new HttpDelete(sb.toString());
    
    /**
     * 
     * Changes for api level http config starts This will set the
     * http.socket.timeout and other parameters to http
     * 
     * delete if specified in httpclient.properties
     */
    setHttpMethodParams(httpMethodParams, httpDelete);
    if (headers != null) {
      for (Map.Entry<String, String> hEntry : headers.entrySet()) {
        httpDelete.addHeader(hEntry.getKey(), hEntry.getValue());
      }
    }
    HttpResponse response;
    byte[] data;
    try {
      response = httpClient.execute(httpDelete);
      data = EntityUtils.toByteArray(response.getEntity());
      EntityUtils.consume(response.getEntity());
    } catch (IOException e) {
      LOG.error("Error http to : {}", url, e);
      throw new HttpTransportException(e, PrchnyWSErrorConstants.IO_ERROR);
    }
    int responseCode = response.getStatusLine().getStatusCode();
    // If the returned status code is either 4xx or 5xx series
    throwExceptionOn4xx_5xxStatuses(data, responseCode);
    return data;
  }
  
  private void throwExceptionOn4xx_5xxStatuses(byte[] data, int responseCode)
      throws HttpTransportException {
  
    if (responseCode / 100 == 4 || responseCode / 100 == 5) {
      if (data != null) {
        throw new HttpTransportException(new String(data), responseCode);
      } else {
        throw new HttpTransportException("No data received from server",
            responseCode);
      }
    }
  }
}
