/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 03-May-2012
 *  @author rahul
 */

package com.prchny.base.transport.serialization.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prchny.base.exception.SerializationException;
import com.prchny.base.exception.SerializationException.SerializationErrorCode;
import com.prchny.base.transport.serialization.service.ISerializationService;

@Service("jsonSerializationService")
public class JsonSerializationServiceImpl implements ISerializationService {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(JsonSerializationServiceImpl.class);
  
  @Override
  public Object doDeserialize(final byte[] data,
      final Class<? extends Object> classType) throws SerializationException {
  
    final ObjectMapper mapper = new ObjectMapper();
    Object object = null;
    try {
      object = mapper.readValue(data, 0, data.length, classType);
    } catch (final JsonParseException e) {
      LOG.error("Error deserializing json for class type {}", classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_DESERIALIZATION_EXCEPTION, e);
    } catch (final JsonMappingException e) {
      LOG.error("Error deserializing json for class type {}", classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_DESERIALIZATION_EXCEPTION, e);
    } catch (final IOException e) {
      LOG.error("Error deserializing json for class type {}", classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return object;
  }
  
  @Override
  public Object doDeSerialize(final String json,
      final Class<? extends Object> classType) throws SerializationException {
  
    final ObjectMapper mapper = new ObjectMapper();
    Object object = null;
    try {
      object = mapper.readValue(json, classType);
    } catch (final JsonParseException e) {
      LOG.error("Error deserializing json for class type {}", classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_DESERIALIZATION_EXCEPTION, e);
    } catch (final JsonMappingException e) {
      LOG.error("Error deserializing json for class type {}", classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_DESERIALIZATION_EXCEPTION, e);
    } catch (final IOException e) {
      LOG.error("Error deserializing json for class type {}", classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return object;
  }
  
  @Override
  public String doSerialize(final Object object) throws SerializationException {
  
    final ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(object);
    } catch (final JsonGenerationException e) {
      LOG.error("Error serializing json for data {}", object);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_SERIALIZATION_EXCEPTION, e);
    } catch (final JsonMappingException e) {
      LOG.error("Error serializing json for data {}", object);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_SERIALIZATION_EXCEPTION, e);
    } catch (final IOException e) {
      LOG.error("Error serializing json for data {}", object);
      LOG.error("Stack trace", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
  }
  
  @Override
  public byte[] doSerialize(final Class<? extends Object> classType,
      final Object obj) throws SerializationException {
  
    final ObjectMapper mapper = new ObjectMapper();
    byte[] data = null;
    try {
      data = mapper.writeValueAsBytes(obj);
    } catch (final JsonParseException e) {
      LOG.error("Error serializing json for data {}", obj);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_SERIALIZATION_EXCEPTION, e);
    } catch (final JsonMappingException e) {
      LOG.error("Error serializing json for data {}", obj);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_SERIALIZATION_EXCEPTION, e);
    } catch (final IOException e) {
      LOG.error("Error serializing json for data {}", obj);
      LOG.error("Stack trace", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return data;
  }
  
  @Override
  public Object doDeserialize(final InputStream inputStream,
      final Class<? extends Object> clazz) throws SerializationException {
  
    final ObjectMapper mapper = new ObjectMapper();
    byte[] data = null;
    try {
      data = mapper.writeValueAsBytes(inputStream);
    } catch (final JsonParseException e) {
      LOG.error("Error deserializing json for class type {}", clazz);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_SERIALIZATION_EXCEPTION, e);
    } catch (final JsonMappingException e) {
      LOG.error("Error deserializing json for class type {}", clazz);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.JSON_SERIALIZATION_EXCEPTION, e);
    } catch (final IOException e) {
      LOG.error("Error deserializing json for class type {}", clazz);
      LOG.error("Stack trace", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return data;
  }
}
