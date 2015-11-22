/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 19, 2010
 *  @author singla
 */

package com.prchny.base.transport.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prchny.base.model.common.HttpClientConfig;

/**
 * This is factory class for apache http client instances. Http clients are
 * initialized with thread safe connection manager and with a custom retry
 * handler. Also it picks the random IP addresses available. Use of
 * getCachedHttpClient is recommended as it will cache the httpclient instances
 * and will return the cached instance.
 * 
 * @author singla
 */
public class HttpClientBuilder {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(HttpClientBuilder.class);
  
  public static HttpClient createNewHttpClient(final String apiCacheName) {
  
    return createNewHttpClient(apiCacheName, null);
  }
  
  public static HttpClient createNewHttpClient(final String apiCacheName,
      final HttpClientConfig clientConfig) {
  
    final HttpClient httpClient = buildHttpClient(clientConfig, apiCacheName);
    if ((clientConfig != null) && clientConfig.isIdleConnectionMonitorOn()) {
      LOG.info("Configuring IdleConnectionMonitor for connection manager : {}",
          apiCacheName);
      new IdleConnectionMonitorThread(httpClient.getConnectionManager())
          .start();
    }
    return httpClient;
  }
  
  private static HttpClient buildHttpClient(HttpClientConfig clientConfig,
      final String apiCacheName) {
  
    if (clientConfig == null) {
      clientConfig = new HttpClientConfig();
    }
    final HttpParams params = new BasicHttpParams();
    HttpConnectionParams.setSoTimeout(params, clientConfig.getSoTimeout());
    ConnRouteParams.setLocalAddress(params, clientConfig.getLocalAddress());
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    SSLSocketFactory sf = null;
    if ("lyrisEmailSystem".equals(apiCacheName)) { // Temporary workaround for
      // Lyris SSL certificate
      // issue
      SSLContext ctx = null;
      try {
        ctx = SSLContext.getInstance("TLS");
      } catch (final NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
      final X509TrustManager tm = new X509TrustManager() {
        
        @Override
        public void checkClientTrusted(final X509Certificate[] xcs,
            final String string) {
        
        }
        
        @Override
        public void checkServerTrusted(final X509Certificate[] xcs,
            final String string) {
        
        }
        
        @Override
        public X509Certificate[] getAcceptedIssuers() {
        
          return null;
        }
      };
      try {
        ctx.init(null, new TrustManager[] {
          tm
        }, null);
      } catch (final KeyManagementException e) {
        e.printStackTrace();
      }
      sf = new SSLSocketFactory(ctx);
    } else {
      sf = SSLSocketFactory.getSocketFactory();
    }
    
    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    final SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
        .getSocketFactory()));
    schemeRegistry.register(new Scheme("https", 443, sf));
    
    final ThreadSafeClientConnManager cm =
        new ThreadSafeClientConnManager(schemeRegistry);
    cm.setDefaultMaxPerRoute(clientConfig.getDefaultMaxPerRoute());
    cm.setMaxTotal(clientConfig.getMaxTotalConnections());
    final DefaultHttpClient client = new ContentEncodingHttpClient(cm, params);
    final HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
      
      @Override
      public boolean retryRequest(final IOException exception,
          final int executionCount, final HttpContext context) {
      
        if (executionCount >= 2) {
          // Do not retry if over max retry count
          return false;
        }
        if (exception instanceof NoHttpResponseException) {
          // Retry if the server dropped connection on us
          return true;
        }
        if (exception instanceof SSLHandshakeException) {
          // Do not retry on SSL handshake exception
          return false;
        }
        final HttpRequest request =
            (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        final boolean idempotent =
            !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
          // Retry if the request is considered idempotent
          return true;
        }
        return false;
      }
    };
    client.setHttpRequestRetryHandler(retryHandler);
    
    // change connection keep alive strategy
    final long keepAlive = clientConfig.getKeepAlive();
    client.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
      
      @Override
      public long getKeepAliveDuration(final HttpResponse response,
          final HttpContext context) {
      
        return keepAlive;
      }
    });
    
    return client;
  }
  
  public static HttpClient buildHttpClient() {
  
    return buildHttpClient(null, "");
  }
}
