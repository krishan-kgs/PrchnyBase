
package com.prchny.wsclient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.prchny.base.aspect.PrchnyServiceRequestContextHolder;
import com.prchny.base.exception.SerializationException;
import com.prchny.base.exception.PrchnyWSException;
import com.prchny.base.exception.TransportException;
import com.prchny.base.model.common.HttpClientConfig;
import com.prchny.base.model.common.ServiceRequest;
import com.prchny.base.model.common.ServiceResponse;
import com.prchny.base.services.request.context.RequestContextSRO;
import com.prchny.base.services.request.context.RequestContextSROFactory;
import com.prchny.base.transport.http.HttpTransportException;
import com.prchny.base.transport.serialization.service.ISerializationService;
import com.prchny.base.transport.service.ITransportService;
import com.prchny.base.utils.Constants;
import com.prchny.base.utils.HttpClientPropertiesUtil;

public class SimpleTransportService implements ITransportService {
  
  private static final String ACCEPT = "Accept";
  
  private static final String CONTENT_TYPE = "Content-Type";
  
  private static final String REQUEST_CONTEXT_SRO_PARAM = "requestContextSRO";
  
  private static final String API_SERVICE_APPENDER = ".";
  
  private static final Map<String, String> urlToPropertyNameInitialMap =
      new HashMap<String, String>();
  
  private static final Map<String, Map<String, Object>> apiCacheNameToHttpMethodParamMap =
      new HashMap<String, Map<String, Object>>();
  
  private static final Logger LOG = LoggerFactory
      .getLogger(SimpleTransportService.class);
  
  private HttpClient delegate;
  
  public SimpleTransportService() {
  
    this(new HttpClientConfig());
  }
  
  public SimpleTransportService(HttpClientConfig config) {
  
    delegate = new PerHostHttpClient(config);
  }
  
  @Override
  public void registerService(final String serviceUrl,
      final String propertyNameInitial) {
  
    throw new UnsupportedOperationException();
  }
  
  @Override
  public ServiceResponse executeRequest(final String serviceUrl,
      final ServiceRequest sr, final Map<String, String> headersMap,
      final Class<? extends Object> classType) throws TransportException {
  
    try {
      final RequestBuilder rb = RequestBuilder.post(serviceUrl);
      
      final HttpUriRequest request =
          createRequest(serviceUrl, sr, headersMap, classType, rb);
      
      final ResponseHandler<? extends ServiceResponse> rh =
          new SimpleResponseHandler(classType, sr.getResponseProtocol()
              .getSerializationService());
      
      return execute(request, rh);
    } catch (final Exception e) {
      throw new IllegalStateException("failed to execute", e);
    }
  }
  
  @Override
  public ServiceResponse executeGetRequest(final String serviceUrl,
      final Map<String, String> params, final Map<String, String> headersMap,
      final Class<? extends Object> classType) throws TransportException {
  
    try {
      final RequestBuilder rb = RequestBuilder.get(serviceUrl);
      for (final Entry<String, String> entry : params.entrySet()) {
        rb.addParameter(entry.getKey(), entry.getValue());
      }
      
      for (final Entry<String, String> entry : headersMap.entrySet()) {
        rb.addHeader(entry.getKey(), entry.getValue());
      }
      
      final Protocol responseProtocol =
          Protocol.valueOf(headersMap.get(ACCEPT));
      final ResponseHandler<? extends ServiceResponse> rh =
          new SimpleResponseHandler(classType,
              responseProtocol.getSerializationService());
      
      rb.addHeader(ACCEPT, responseProtocol.getMimeType());
      // FIXME:
      // sr.setUserTrackingId(MDC.get(Constants.REQUEST_USER_TRACKING_KEY));
      final RequestContextSRO requestContextSRO =
          RequestContextSROFactory
              .createRequestContextSRO(PrchnyServiceRequestContextHolder
                  .getRequestContext());
      rb.addParameter(REQUEST_CONTEXT_SRO_PARAM, responseProtocol
          .getSerializationService().doSerialize(requestContextSRO));
      
      return execute(rb.build(), rh);
    } catch (final Exception e) {
      throw new IllegalStateException("failed to execute", e);
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServiceResponse> T executePutRequest(
      final String serviceUrl, final ServiceRequest sr,
      final Map<String, String> headersMap, final Class<T> classType)
      throws PrchnyWSException {
  
    try {
      final RequestBuilder rb = RequestBuilder.put(serviceUrl);
      
      final HttpUriRequest request =
          createRequest(serviceUrl, sr, headersMap, classType, rb);
      
      final ResponseHandler<? extends ServiceResponse> rh =
          new SimpleResponseHandler(classType, sr.getResponseProtocol()
              .getSerializationService());
      return (T) execute(request, rh);
    } catch (final Exception e) {
      throw new IllegalStateException("failed to execute", e);
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServiceResponse> T executeDeleteRequest(
      final String serviceUrl, final ServiceRequest sr,
      final Map<String, String> headersMap, final Class<T> classType)
      throws PrchnyWSException {
  
    final RequestBuilder rb = RequestBuilder.delete(serviceUrl);
    
    final HttpUriRequest request =
        createRequest(serviceUrl, sr, headersMap, classType, rb);
    
    final ResponseHandler<? extends ServiceResponse> rh =
        new SimpleResponseHandler(classType, sr.getResponseProtocol()
            .getSerializationService());
    
    try {
      return (T) execute(request, rh);
    } catch (Exception e) {
      throw new IllegalStateException("failed to execute", e);
    }
    
  }
  
  @Override
  public byte[] getResponse(final Class<? extends Object> classType,
      final Object obj, final Protocol protocol) throws TransportException {
  
    throw new UnsupportedOperationException();
  }
  
  private HttpUriRequest createRequest(final String serviceUrl,
      final ServiceRequest sr, final Map<String, String> headersMap,
      final Class<? extends Object> classType, final RequestBuilder rb) {
  
    addCommonMetadata(sr, rb);
    
    for (final Entry<String, Object> entry : getApiParameters(serviceUrl)
        .entrySet()) {
      rb.addParameter(entry.getKey(), entry.getValue().toString());
    }
    
    for (final Entry<String, String> entry : headersMap.entrySet()) {
      rb.addHeader(entry.getKey(), entry.getValue());
    }
    
    final Protocol requestProtocol = sr.getRequestProtocol();
    try {
      rb.setEntity(new ByteArrayEntity(requestProtocol
          .getSerializationService().doSerialize(classType, sr), ContentType
          .create(requestProtocol.getMimeType())));
    } catch (SerializationException e) {
      throw new IllegalStateException("failed to serialize", e);
    }
    
    return rb.build();
  }
  
  private Map<String, Object> getApiParameters(final String serviceUrl) {
  
    return getAPIParamaters(getAPICacheName(serviceUrl, getCacheName(serviceUrl)));
  }
  
  private void addCommonMetadata(final ServiceRequest sr,
      final RequestBuilder rb) {
  
    rb.addHeader(CONTENT_TYPE, sr.getRequestProtocol().getMimeType());
    rb.addHeader(ACCEPT, sr.getResponseProtocol().getMimeType());
    
    sr.setUserTrackingId(MDC.get(Constants.REQUEST_USER_TRACKING_KEY));
    sr.setContextSRO(RequestContextSROFactory
        .createRequestContextSRO(PrchnyServiceRequestContextHolder
            .getRequestContext()));
  }
  
  private String getCacheName(final String serviceUrl) {
  
    if (serviceUrl.contains("/api/")) {
      return getCacheNameForGetRequest(serviceUrl);
    }
    return serviceUrl.substring(serviceUrl.indexOf("/service"),
        serviceUrl.lastIndexOf('/') + 1);
  }
  
  /**
   * Assumption: We will have substring starting from /api till 3 '/' for cache
   * eg: for a service url http://hostName:8080/api/foo/baar/boo/faar cache name
   * will be api/foo/baar/ eg: in coms a cash name is
   * 
   * @param serviceUrl
   * @return
   */
  private String getCacheNameForGetRequest(String serviceUrl) {
  
    final int index = serviceUrl.indexOf("/api");
    if (index < 0) {
      return null;
    }
    
    serviceUrl = serviceUrl.substring(index);
    
    final String[] splits = serviceUrl.split("/");
    if (splits == null || splits.length < 4) {
      return null;
    }
    return new StringBuilder("/").append(splits[1]).append("/")
        .append(splits[2]).append("/").append(splits[3]).append("/").toString();
  }
  
  private Map<String, Object> getAPIParamaters(final String apiCacheName) {
  
    Map<String, Object> parameterMap =
        apiCacheNameToHttpMethodParamMap.get(apiCacheName);
    if (parameterMap == null) {
      final String property =
          apiCacheName + HttpClientPropertiesUtil.HTTP_SOCKET_TIMEOUT;
      try {
        final Integer soTimeout =
            Integer.parseInt(HttpClientPropertiesUtil.getProperty(property));
        parameterMap =
            Collections.<String, Object> singletonMap(
                HttpClientPropertiesUtil.HTTP_SOCKET_TIMEOUT, soTimeout);
        apiCacheNameToHttpMethodParamMap.put(apiCacheName, parameterMap);
      } catch (final NumberFormatException e) {
        LOG.info("Unable to find property {}", property);
        /**
         * This is done because on every api call this method will be executed
         * and a number format exception will be thrown if timeout is not
         * overriden for that api. Throwing NFE on every call is a heavy
         * operation hence I am setting an empty map if timeout configuration is
         * not found. By Prateek Mathur
         */
        apiCacheNameToHttpMethodParamMap.put(apiCacheName,
            Collections.<String, Object> emptyMap());
      }
      
    }
    return parameterMap;
  }
  
  private String getAPICacheName(final String serviceUrl, final String cacheName) {
  
    final String serviceUrlString =
        serviceUrl.substring(serviceUrl.lastIndexOf('/') + 1,
            serviceUrl.length());
    final String clientName = urlToPropertyNameInitialMap.get(cacheName);
    return clientName + serviceUrlString + API_SERVICE_APPENDER;
  }
  
  private ServiceResponse execute(final HttpUriRequest httpUriRequest,
      final ResponseHandler<? extends ServiceResponse> rh)
      throws ClientProtocolException, IOException {
  
    return delegate.execute(httpUriRequest, rh);
  }
  
  private static final class SimpleResponseHandler implements
    ResponseHandler<ServiceResponse> {
    
    private final Class<? extends Object> classType;
    
    private final ISerializationService serializationService;
    
    private SimpleResponseHandler(final Class<? extends Object> classType,
        final ISerializationService serializationService) {
    
      super();
      this.classType = classType;
      this.serializationService = serializationService;
    }
    
    @Override
    public ServiceResponse handleResponse(final HttpResponse response)
        throws ClientProtocolException, IOException {
    
      try {
        final byte[] byteArray = EntityUtils.toByteArray(response.getEntity());
        
        validate(byteArray, response.getStatusLine().getStatusCode());
        
        return (ServiceResponse) serializationService.doDeserialize(byteArray,
            classType);
        
      } catch (final Exception e) {
        throw new IllegalStateException("failed to process response", e);
      }
    }
    
    private void validate(final byte[] data, final int responseCode)
        throws HttpTransportException {
    
      if (((responseCode / 100) == 4) || ((responseCode / 100) == 5)) {
        if (data == null) {
          throw new HttpTransportException("No data", responseCode);
        }
        throw new HttpTransportException(new String(data), responseCode);
      }
      
    }
  }
  
}
