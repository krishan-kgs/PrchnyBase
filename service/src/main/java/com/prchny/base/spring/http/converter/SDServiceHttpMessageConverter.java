/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 18-May-2012
 *  @author rahul
 */

package com.prchny.base.spring.http.converter;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import com.prchny.base.exception.SerializationException;
import com.prchny.base.exception.SerializationException.SerializationErrorCode;
import com.prchny.base.model.common.ServiceRequest;
import com.prchny.base.model.common.ServiceResponse;
import com.prchny.base.transport.serialization.service.ISerializationService;
import com.prchny.base.transport.service.ITransportService.Protocol;

public class SDServiceHttpMessageConverter extends
    AbstractHttpMessageConverter<Object> {
  
  public static String LS = System.getProperty("line.separator");
  
  public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
  
  public static final MediaType SD_SERVICE = new MediaType("application",
      "sd-service", DEFAULT_CHARSET);
  
  public static final MediaType PROTOBUF = new MediaType("application",
      "x-protobuf", DEFAULT_CHARSET);
  
  public static final MediaType XML = new MediaType("application", "xml",
      DEFAULT_CHARSET);
  
  public static final MediaType FST_OCTET_STREAM = new MediaType("application",
      "octet-stream", DEFAULT_CHARSET);
  
  private static final Logger LOG = LoggerFactory
      .getLogger(SDServiceHttpMessageConverter.class);
  
  @Autowired
  @Qualifier("protostuffSerializationService")
  protected ISerializationService pbuffSerializationService;
  
  @Autowired
  @Qualifier("jsonSerializationService")
  protected ISerializationService jsonSerializationService;
  
  @Autowired
  @Qualifier("xmlSerializationService")
  protected ISerializationService xmlSerializationService;
  
  @Autowired
  @Qualifier("fstSerializationService")
  protected ISerializationService fstSerializationService;
  
  public SDServiceHttpMessageConverter() {
  
    super(PROTOBUF, SD_SERVICE, XML, FST_OCTET_STREAM);
  }
  
  @Override
  protected boolean supports(final Class<?> clazz) {
  
    // should not be called, since we override canRead/Write instead
    throw new UnsupportedOperationException();
  }
  
  // reads only protobuff request. for json request we are using already
  // provided impl : MappingJacksonHttpMessageConverter
  @Override
  protected Object readInternal(final Class<? extends Object> clazz,
      final HttpInputMessage inputMessage) throws IOException,
      HttpMessageNotReadableException {
  
    ServiceRequest request = null;
    
    final ISerializationService deserializationService =
        getDeserializer(inputMessage);
    if (deserializationService == null) {
      throw new RuntimeException(new SerializationException(
          SerializationErrorCode.UNSUPPORTED_MEDIA_TYPE));
    }
    try {
      request =
          (ServiceRequest) deserializationService.doDeserialize(
              inputMessage.getBody(), clazz);
    } catch (final SerializationException e) {
      LOG.error(
          "error while de serializing request using deserializationService : {}",
          deserializationService, e);
    } catch (final ClassCastException castException) {
      throw new RuntimeException(
          "Class cast Exception. Request body type parameter of the service method / controller method should extend ServiceRequest");
    }
    return request;
  }
  
  // writes all the type of requests. protobuff, json .... going fwd this method
  // will handle xml and other writes as well
  @Override
  protected void writeInternal(final Object o,
      final HttpOutputMessage outputMessage) throws IOException,
      HttpMessageNotWritableException {
  
    ServiceResponse serviceResponse = null;
    
    try {
      serviceResponse = (ServiceResponse) o;
    } catch (final ClassCastException castException) {
      throw new RuntimeException(
          "Class cast Exception. Return type of the service method / controller method should extend ServiceResponse");
    }
    
    final ISerializationService serializationService =
        getSerializer(serviceResponse.getProtocol());
    if (serializationService == null) {
      throw new RuntimeException("Protocol Not Supported: "
          + serviceResponse.getProtocol()
          + " or protocol not set for ServiceResponse");
    } else {
      try {
        FileCopyUtils.copy(serializationService.doSerialize(o.getClass(), o),
            outputMessage.getBody());
      } catch (final SerializationException e) {
        LOG.error("error while serializing the response object", e);
        throw new RuntimeException(
            "error while serializing the response object", e);
      }
    }
  }
  
  protected ISerializationService getSerializer(final Protocol protocol) {
  
    if (Protocol.PROTOCOL_PROTOSTUFF.equals(protocol)) {
      return pbuffSerializationService;
    } else if (Protocol.PROTOCOL_JSON.equals(protocol)) {
      return jsonSerializationService;
    } else if (Protocol.PROTOCOL_XML.equals(protocol)) {
      return xmlSerializationService;
    } else if (Protocol.PROTOCOL_FST.equals(protocol)) {
      return fstSerializationService;
    }
    return null;
  }
  
  protected ISerializationService getDeserializer(
      final HttpInputMessage inputMessage) {
  
    final MediaType mediaType = inputMessage.getHeaders().getContentType();
    if (PROTOBUF.getSubtype().equals(mediaType.getSubtype())) {
      return pbuffSerializationService;
    } else if (FST_OCTET_STREAM.getSubtype().equals(mediaType.getSubtype())) {
      return fstSerializationService;
    }
    return null;
  }
  
  @Override
  public boolean canRead(final Class<?> clazz, final MediaType mediaType) {
  
    return PROTOBUF.isCompatibleWith(mediaType)
        || FST_OCTET_STREAM.isCompatibleWith(mediaType);
  }
  
  @Override
  public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
  
    return SD_SERVICE.isCompatibleWith(mediaType);
  }
  
}
