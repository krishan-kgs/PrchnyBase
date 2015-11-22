
package com.prchny.wsclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import com.prchny.base.model.common.HttpClientConfig;

/**
 * Maintains an instance of {@link HttpClient} for each {@link HttpHost}
 */
public class PerHostHttpClient extends AbstractHttpClientDecorator {
  
  private final ConcurrentMap<HttpHost, HttpClient> clients =
      new ConcurrentHashMap<HttpHost, HttpClient>();
  
  private final HttpClientConfig config;
  
  /**
   * Uses default values for {@link HttpClientConfig}
   */
  public PerHostHttpClient() {
  
    this(new HttpClientConfig());
  }
  
  public PerHostHttpClient(final HttpClientConfig config) {
  
    super();
    this.config = config;
  }
  
  @Override
  public HttpResponse execute(final HttpHost target, final HttpRequest request,
      final HttpContext context) throws IOException, ClientProtocolException {
  
    final HttpHost host = getCanonicalHost(target);
    
    if (!clients.containsKey(host)) {
      
      final SocketConfig socketConfig =
          SocketConfig.custom().setTcpNoDelay(true)
              .setSoTimeout(config.getSoTimeout()).setSoKeepAlive(true).build();
      
      final HttpClient client =
          HttpClients.custom().setConnectionManagerShared(false)
              .setConnectionTimeToLive(config.getKeepAlive(), TimeUnit.MINUTES)
              .setMaxConnTotal(config.getMaxTotalConnections())
              .setMaxConnPerRoute(config.getDefaultMaxPerRoute())
              .setDefaultSocketConfig(socketConfig).build();
      
      clients.putIfAbsent(host, client);
    }
    
    return clients.get(host).execute(target, request, context);
  }
  
  private HttpHost getCanonicalHost(final HttpHost host) {
  
    URI uri;
    try {
      uri = new URI(host.toURI());
    } catch (final URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    final String hostname = uri.getHost();
    int port = uri.getPort();
    final String scheme = uri.getScheme();
    final boolean isHttps = "HTTPS".equalsIgnoreCase(scheme);
    final String schemePart = isHttps ? (scheme + "://") : "";
    if (port == -1) {
      port = isHttps ? 443 : 80;
    }
    return new HttpHost(schemePart + hostname + ":" + port);
  }
}
