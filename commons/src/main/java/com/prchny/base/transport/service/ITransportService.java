/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 *  @version     1.0, 03-May-2012
 *  @author rahul
 */

package com.prchny.base.transport.service;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.prchny.base.exception.PrchnyWSException;
import com.prchny.base.exception.TransportException;
import com.prchny.base.model.common.ServiceRequest;
import com.prchny.base.model.common.ServiceResponse;
import com.prchny.base.transport.serialization.service.ISerializationService;
import com.prchny.base.transport.serialization.service.impl.FSTSerializationServiceImpl;
import com.prchny.base.transport.serialization.service.impl.JsonSerializationServiceImpl;
import com.prchny.base.transport.serialization.service.impl.ProtocolAdapter;
import com.prchny.base.transport.serialization.service.impl.ProtostuffSerializationServiceImpl;
import com.prchny.base.transport.serialization.service.impl.XmlSerializationServiceImpl;

public interface ITransportService {
  
  @XmlJavaTypeAdapter(ProtocolAdapter.class)
  public enum Protocol {
    PROTOCOL_JSON(
        "application/json") {
      
      @Override
      public ISerializationService getSerializationService() {
      
        return JSON_SERIALIZATION_SERVICE_IMPL;
      }
      
    },
    PROTOCOL_PROTOSTUFF(
        "application/x-protobuf") {
      
      @Override
      public ISerializationService getSerializationService() {
      
        return PROTOSTUFF_SERIALIZATION_SERVICE_IMPL;
      }
      
    },
    PROTOCOL_XML(
        "application/xml") {
      
      @Override
      public ISerializationService getSerializationService() {
      
        return XML_SERIALIZATION_SERVICE_IMPL;
      }
      
    },
    PROTOCOL_FST(
        "application/octet-stream") {
      
      @Override
      public ISerializationService getSerializationService() {
      
        return FST_SERIALIZATION_SERVICE_IMPL;
      }
      
    };
    
    private String mimeType;
    
    private Protocol(String mimeType) {
    
      this.mimeType = mimeType;
    }
    
    private static final JsonSerializationServiceImpl JSON_SERIALIZATION_SERVICE_IMPL =
        new JsonSerializationServiceImpl();
    
    private static final ProtostuffSerializationServiceImpl PROTOSTUFF_SERIALIZATION_SERVICE_IMPL =
        new ProtostuffSerializationServiceImpl();
    
    private static final XmlSerializationServiceImpl XML_SERIALIZATION_SERVICE_IMPL =
        new XmlSerializationServiceImpl();
    
    private static final FSTSerializationServiceImpl FST_SERIALIZATION_SERVICE_IMPL =
        new FSTSerializationServiceImpl();
    
    public final String getMimeType() {
    
      return mimeType;
    }
    
    public abstract ISerializationService getSerializationService();
    
    public static final Protocol from(String alias) {
    
      if ("pbuf".equalsIgnoreCase(alias)) {
        return PROTOCOL_PROTOSTUFF;
      } else if ("json".equalsIgnoreCase(alias)) {
        return PROTOCOL_JSON;
      } else if ("fst".equalsIgnoreCase(alias)) {
        return PROTOCOL_FST;
      } else if ("xml".equalsIgnoreCase(alias)) {
        return PROTOCOL_XML;
      }
      
      throw new IllegalArgumentException(String.format(
          "unknow protocol alias=[%s] use one of {pbuf|json|fst|xml}", alias));
    }
    
  }
  
  public byte[] getResponse(Class<? extends Object> classType, Object obj,
      Protocol protocol) throws TransportException;
  
  public ServiceResponse executeRequest(String serviceUrl,
      ServiceRequest request, Map<String, String> headersMap,
      Class<? extends Object> classType) throws TransportException;
  
  public void registerService(String serviceUrl, String propertyNameInitial);
  
  public ServiceResponse executeGetRequest(String serviceUrl,
      Map<String, String> params, Map<String, String> headersMap,
      Class<? extends Object> classType) throws TransportException;
  
  public <T extends ServiceResponse> T executePutRequest(String serviceUrl,
      ServiceRequest request, Map<String, String> headersMap, Class<T> classType)
      throws PrchnyWSException;
  
  public <T extends ServiceResponse> T executeDeleteRequest(String serviceUrl,
      ServiceRequest request, Map<String, String> headersMap, Class<T> classType)
      throws PrchnyWSException;
}
