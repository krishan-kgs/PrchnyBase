/*
 * Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved. JASPER
 * INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @version 1.0, 03-May-2012
 * @author rahul
 */

package com.prchny.base.transport.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.prchny.base.aspect.PrchnyServiceRequestContextHolder;
import com.prchny.base.constant.PrchnyWSErrorConstants;
import com.prchny.base.exception.SerializationException;
import com.prchny.base.exception.PrchnyWSException;
import com.prchny.base.exception.TransportException;
import com.prchny.base.exception.TransportException.TransportErrorCode;
import com.prchny.base.model.common.HttpClientConfig;
import com.prchny.base.model.common.ServiceRequest;
import com.prchny.base.model.common.ServiceResponse;
import com.prchny.base.services.request.context.LocalContextProvider;
import com.prchny.base.services.request.context.RequestContextSRO;
import com.prchny.base.services.request.context.RequestContextSROFactory;
import com.prchny.base.transport.http.HttpSender;
import com.prchny.base.transport.http.HttpTransportException;
import com.prchny.base.transport.serialization.service.ISerializationService;
import com.prchny.base.transport.service.ITransportService;
import com.prchny.base.utils.ApplicationPropertyUtils;
import com.prchny.base.utils.Constants;
import com.prchny.base.utils.HttpClientPropertiesUtil;
import com.prchny.base.utils.StringUtils;

@Service("transportService")
public class TransportServiceImpl implements ITransportService {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(TransportServiceImpl.class);
  
  private static final Map<String, HttpClientConfig> cacheUrlToHttpClientConfigMap =
      new HashMap<String, HttpClientConfig>();
  
  private static final Map<String, Protocol> cacheUrlToProtocolMap =
      new HashMap<String, ITransportService.Protocol>();
  
  private static Map<String, String> urlToPropertyNameInitialMap =
      new HashMap<String, String>();
  
  private static final Map<String, Map<String, Object>> apiCacheNameToHttpMethodParamMap =
      new HashMap<String, Map<String, Object>>();
  
  /**
   * Changes for api level http config By Prateek Mathur
   */
  private static final Map<String, HttpSender> httpSenderCache =
      new ConcurrentHashMap<String, HttpSender>();
  
  private static final Map<String, String> HEADERS_MAP =
      new HashMap<String, String>(1);
  
  private static final String X_FORWARDED_FOR_HEADER_NAME = "remote-ip";
  
  private static final String REQUEST_CONTEXT_SRO_PARAM = "requestContextSRO";
  
  private static final String API_SERVICE_APPENDER = ".";
  
  @PostConstruct
  public void init() {
  
    // Initialize X_FORWARDED header parameter
    initHeaders();
  }
  
  private void initHeaders() {
  
    InetAddress ia = null;
    try {
      ia = ApplicationPropertyUtils.getLocalHostLANAddress();
      HEADERS_MAP.put(X_FORWARDED_FOR_HEADER_NAME, ia.getHostAddress());
    } catch (UnknownHostException e) {
      LOG.info("Unable to get ip address", e);
    }
  }
  
  @Override
  public void registerService(String serviceUrl, String propertyNameInitial) {
  
    LOG.info("Registering service : {} with property name initial as : {}",
        serviceUrl, propertyNameInitial);
    urlToPropertyNameInitialMap.put(serviceUrl, propertyNameInitial);
  }
  
  @Override
  public byte[] getResponse(Class<? extends Object> classType, Object obj,
      Protocol protocol) throws TransportException {
  
    try {
      return protocol.getSerializationService().doSerialize(classType, obj);
    } catch (SerializationException e) {
      throw new TransportException(e.getErrorCode().code(), e.getMessage(),
          e.getCause());
    }
  }
  
  @Override
  public ServiceResponse executeRequest(String serviceUrl,
      ServiceRequest request, Map<String, String> headersMap,
      Class<? extends Object> classType) throws TransportException {
  
    if (StringUtils.isEmpty(serviceUrl)) {
      throw new IllegalArgumentException("Service URL not set");
    }
    if (request == null) {
      throw new IllegalArgumentException("ServiceRequest not set");
    }
    String cacheName = getCacheName(serviceUrl);
    updateUserTrackingId(request);
    
    HttpSender httpSender = getCachedHttpSender(cacheName);
    
    if (request.getResponseProtocol() == null) {
      request.setResponseProtocol(getProtocol(cacheName));
    }
    
    byte[] responseData = null;
    if (headersMap == null) {
      headersMap = new HashMap<String, String>();
    }
    headersMap.put("Content-Type", request.getRequestProtocol().getMimeType());
    ISerializationService serializationService =
        request.getResponseProtocol().getSerializationService();
    ServiceResponse serviceResponse = null;
    
    // Add context to request
    request.setContextSRO(createRequestContext());
    
    /**
     * Changes for api level http config starts By Prateek Mathur
     */
    String apiCacheName = getAPICacheName(serviceUrl, cacheName);
    Map<String, Object> apiParameterMap = getAPIParamaters(apiCacheName);
    
    try {
      responseData =
          httpSender.executePostContentAndReturnBytes(serviceUrl, null,
              headersMap,
              request.getRequestProtocol().getSerializationService()
                  .doSerialize(request.getClass(), request), apiParameterMap);
    } catch (HttpTransportException e) {
      throw new TransportException(TransportErrorCode.HTTP_TRANSPORT_EXCEPTION,
          e.getCode(), e);
    } catch (SerializationException e) {
      throw new TransportException(TransportErrorCode.HTTP_TRANSPORT_EXCEPTION,
          PrchnyWSErrorConstants.SERIALIZATION_ERROR, e);
    }
    try {
      serviceResponse =
          (ServiceResponse) serializationService.doDeserialize(responseData,
              classType);
    } catch (SerializationException e) {
      throw new TransportException(e.getErrorCode().code(), e.getMessage(),
          PrchnyWSErrorConstants.DESERIALIZATION_ERROR, e);
    }
    return serviceResponse;
  }
  
  private void updateUserTrackingId(ServiceRequest request) {
  
    request.setUserTrackingId(MDC.get(Constants.REQUEST_USER_TRACKING_KEY));
  }
  
  private RequestContextSRO createRequestContext() {
  
    LocalContextProvider context =
        PrchnyServiceRequestContextHolder.getRequestContext();
    return RequestContextSROFactory.createRequestContextSRO(context);
  }
  
  private Protocol getProtocol(String cacheName) {
  
    Protocol protocol = cacheUrlToProtocolMap.get(cacheName);
    if (protocol == null) {
      String propertyNameInitial = urlToPropertyNameInitialMap.get(cacheName);
      if (propertyNameInitial == null) {
        LOG.info("property name initial for service: {} not configured",
            cacheName);
        throw new RuntimeException(
            "property name initial not cnfigured. Please register your service with transport service before using it.");
      }
      String protocolProperty =
          HttpClientPropertiesUtil.getProperty(propertyNameInitial
              + HttpClientPropertiesUtil.PROPERTY_PROTOCOL);
      protocol = Protocol.from(protocolProperty);
          
      cacheUrlToProtocolMap.put(cacheName, protocol);
    }
    return protocol;
  }
  
  private HttpSender getCachedHttpSender(String cacheName) {
  
    HttpSender httpSender = httpSenderCache.get(cacheName);
    if (httpSender == null) {
      synchronized (httpSenderCache) {
        httpSender = httpSenderCache.get(cacheName);
        if (httpSender == null) {
          
          HttpClientConfig clientConfig =
              cacheUrlToHttpClientConfigMap.get(cacheName);
          if (clientConfig == null) {
            clientConfig = createHttpClientConfig(cacheName);
            cacheUrlToHttpClientConfigMap.put(cacheName, clientConfig);
          }
          
          httpSender = new HttpSender(cacheName, clientConfig);
          httpSenderCache.put(cacheName, httpSender);
        }
      }
    }
    return httpSender;
  }
  
  private HttpClientConfig createHttpClientConfig(String cacheName) {
  
    HttpClientConfig clientConfig = new HttpClientConfig();
    String propertyNameInitial = urlToPropertyNameInitialMap.get(cacheName);
    if (propertyNameInitial == null) {
      LOG.info("property name initial for service: {} not configured",
          cacheName);
      throw new RuntimeException(
          "property name initial not cnfigured. Please register your service with transport service before using it.");
    }
    String property = null;
    try {
      property =
          propertyNameInitial
              + HttpClientPropertiesUtil.PROPERTY_DEFAULT_MAX_PER_ROUTE;
      Integer defaultMaxPerRoute =
          Integer.parseInt(HttpClientPropertiesUtil.getProperty(property));
      clientConfig.setDefaultMaxPerRoute(defaultMaxPerRoute);
    } catch (NumberFormatException e) {
      LOG.info("Unable to find property {}", property);
    }
    try {
      property =
          propertyNameInitial
              + HttpClientPropertiesUtil.PROPERTY_MAX_TOTAL_CONNECTIONS;
      Integer maxTotalConnections =
          Integer.parseInt(HttpClientPropertiesUtil.getProperty(property));
      clientConfig.setMaxTotalConnections(maxTotalConnections);
    } catch (NumberFormatException e) {
      LOG.info("Unable to find property {}", property);
    }
    try {
      property =
          propertyNameInitial + HttpClientPropertiesUtil.PROPERTY_SO_TIMEOUT;
      Integer soTimeout =
          Integer.parseInt(HttpClientPropertiesUtil.getProperty(property));
      clientConfig.setSoTimeout(soTimeout);
    } catch (NumberFormatException e) {
      LOG.info("Unable to find property {}", property);
    }
    try {
      property =
          propertyNameInitial + HttpClientPropertiesUtil.PROPERTY_KEEP_ALIVE;
      Long keepAlive =
          Long.parseLong(HttpClientPropertiesUtil.getProperty(property));
      clientConfig.setKeepAlive(keepAlive);
    } catch (NumberFormatException e) {
      LOG.info("Unable to find property {}", property);
    }
    
    property =
        propertyNameInitial
            + HttpClientPropertiesUtil.PROPERTY_IDLE_CONNECTION_MONITOR_ON;
    clientConfig.setIdleConnectionMonitorOn(StringUtils.getNotNullValue(
        HttpClientPropertiesUtil.getProperty(property))
        .equalsIgnoreCase("true"));
    LOG.info("HttpClient Properties Recorded for cacheName: {} = {}",
        cacheName, clientConfig);
    return clientConfig;
  }
  
  private String getCacheName(String serviceUrl) {
  
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
  
    int index = serviceUrl.indexOf("/api");
    if (index < 0) {
      return null;
    }
    
    serviceUrl = serviceUrl.substring(index);
    
    String[] splits = serviceUrl.split("/");
    if (splits == null || splits.length < 4) {
      return null;
    }
    return new StringBuilder("/").append(splits[1]).append("/")
        .append(splits[2]).append("/").append(splits[3]).append("/").toString();
  }
  
  @Override
  public ServiceResponse executeGetRequest(String serviceUrl,
      Map<String, String> params, Map<String, String> headersMap,
      Class<? extends Object> classType) throws TransportException {
  
    String cacheName = getCacheNameForGetRequest(serviceUrl);
    HttpSender httpSender = getCachedHttpSender(cacheName);
    /*
     * if (request.getResponseProtocol() == null) {
     * request.setResponseProtocol(getProtocol(cacheName)); }
     */
    
    /**
     * Changes for api level http config starts
     */
    String apiCacheName = getAPICacheName(serviceUrl, cacheName);
    Map<String, Object> apiParameterMap = getAPIParamaters(apiCacheName);
    
    byte[] responseData = null;
    if (headersMap == null) {
      headersMap = new HashMap<String, String>(HEADERS_MAP);
    } else {
      headersMap.putAll(HEADERS_MAP);
    }
    
    // Add context as a json-encoded parameter
    RequestContextSRO sro = createRequestContext();
    try {
      params.put(REQUEST_CONTEXT_SRO_PARAM, Protocol.PROTOCOL_JSON
          .getSerializationService().doSerialize(sro));
    } catch (SerializationException e1) {
      LOG.error("Unable to serialize request sro : {}", sro);
      LOG.debug("Exception : ", e1);
    }
    
    try {
      // headersMap.put("Accept",
      // protocolToMediaTypeMap.get(request.getRequestProtocol()));
      /*
       * responseData = httpSender.executePostContentAndReturnBytes(serviceUrl,
       * null, headersMap,
       * protocolToSerializationServiceMap.get(request.getRequestProtocol
       * ()).doSerialize(request.getClass(), request));
       */
      responseData =
          httpSender.executeGetAndReturnBytes(serviceUrl, params, headersMap,
              apiParameterMap);
    } catch (HttpTransportException e) {
      throw new TransportException(TransportErrorCode.HTTP_TRANSPORT_EXCEPTION,
          e.getCode(), e);
    }
    ISerializationService serializationService =
        Protocol.valueOf(headersMap.get("Accept")).getSerializationService();
    
    ServiceResponse serviceResponse;
    try {
      serviceResponse =
          (ServiceResponse) serializationService.doDeserialize(responseData,
              classType);
    } catch (SerializationException e) {
      throw new TransportException(e.getErrorCode().code(), e.getMessage(),
          PrchnyWSErrorConstants.DESERIALIZATION_ERROR, e.getCause());
    }
    return serviceResponse;
  }
  
  private Map<String, Object> getAPIParamaters(String apiCacheName) {
  
    Map<String, Object> parameterMap =
        apiCacheNameToHttpMethodParamMap.get(apiCacheName);
    if (parameterMap == null) {
      String property =
          apiCacheName + HttpClientPropertiesUtil.HTTP_SOCKET_TIMEOUT;
      try {
        Integer soTimeout =
            Integer.parseInt(HttpClientPropertiesUtil.getProperty(property));
        parameterMap =
            Collections.<String, Object> singletonMap(
                HttpClientPropertiesUtil.HTTP_SOCKET_TIMEOUT, soTimeout);
        apiCacheNameToHttpMethodParamMap.put(apiCacheName, parameterMap);
      } catch (NumberFormatException e) {
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
  
  private String getAPICacheName(String serviceUrl, String cacheName) {
  
    String serviceUrlString =
        serviceUrl.substring(serviceUrl.lastIndexOf('/') + 1,
            serviceUrl.length());
    String clientName = urlToPropertyNameInitialMap.get(cacheName);
    return clientName + serviceUrlString + API_SERVICE_APPENDER;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServiceResponse> T executePutRequest(String serviceUrl,
      ServiceRequest request, Map<String, String> headersMap, Class<T> classType)
      throws PrchnyWSException {
  
    if (StringUtils.isEmpty(serviceUrl)) {
      throw new IllegalArgumentException("Service URL not set");
    }
    if (request == null) {
      throw new IllegalArgumentException("ServiceRequest not set");
    }
    String cacheName = getCacheName(serviceUrl);
    updateUserTrackingId(request);
    HttpSender httpSender = getCachedHttpSender(cacheName);
    if (request.getResponseProtocol() == null) {
      request.setResponseProtocol(getProtocol(cacheName));
    }
    byte[] responseData = null;
    if (headersMap == null) {
      headersMap = new HashMap<String, String>();
    } else {
      headersMap
          .put("Content-Type", request.getRequestProtocol().getMimeType());
    }
    ISerializationService serializationService =
        request.getResponseProtocol().getSerializationService();
    T serviceResponse = null;
    // Add context to request
    request.setContextSRO(createRequestContext());
    /**
     * 
     * Changes for api level http config starts By Prateek Mathur
     */
    String apiCacheName = getAPICacheName(serviceUrl, cacheName);
    Map<String, Object> apiParameterMap = getAPIParamaters(apiCacheName);
    try {
      responseData =
          httpSender.executePutContentAndReturnBytes(serviceUrl, null,
              headersMap,
              request.getRequestProtocol().getSerializationService()
                  .doSerialize(request.getClass(), request), apiParameterMap);
    } catch (HttpTransportException e) {
      throw new PrchnyWSException(e.getCode(), e.getMessage(), e);
    } catch (SerializationException e) {
      throw new PrchnyWSException(e.getMessage(), e);
    }
    try {
      serviceResponse =
          (T) serializationService.doDeserialize(responseData, classType);
    } catch (SerializationException e) {
      throw new PrchnyWSException(e.getMessage(), e);
    }
    return serviceResponse;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T extends ServiceResponse> T executeDeleteRequest(String serviceUrl,
      ServiceRequest request, Map<String, String> headersMap, Class<T> classType)
      throws PrchnyWSException {
  
    if (StringUtils.isEmpty(serviceUrl)) {
      throw new IllegalArgumentException("Service URL not set");
    }
    if (request == null) {
      throw new IllegalArgumentException("ServiceRequest not set");
    }
    String cacheName = getCacheName(serviceUrl);
    updateUserTrackingId(request);
    HttpSender httpSender = getCachedHttpSender(cacheName);
    if (request.getResponseProtocol() == null) {
      request.setResponseProtocol(getProtocol(cacheName));
    }
    byte[] responseData = null;
    if (headersMap == null) {
      headersMap = new HashMap<String, String>();
    } else {
      headersMap
          .put("Content-Type", request.getRequestProtocol().getMimeType());
    }
    ISerializationService serializationService =
        request.getResponseProtocol().getSerializationService();
    T serviceResponse = null;
    // Add context to request
    request.setContextSRO(createRequestContext());
    String apiCacheName = getAPICacheName(serviceUrl, cacheName);
    Map<String, Object> apiParameterMap = getAPIParamaters(apiCacheName);
    try {
      responseData =
          httpSender.executeDeleteAndReturnBytes(serviceUrl, null, headersMap,
              apiParameterMap);
    } catch (HttpTransportException e) {
      throw new PrchnyWSException(e.getCode(), e.getMessage(), e);
    }
    try {
      serviceResponse =
          (T) serializationService.doDeserialize(responseData, classType);
    } catch (SerializationException e) {
      throw new PrchnyWSException(e.getMessage(), e);
    }
    return serviceResponse;
  }
}
