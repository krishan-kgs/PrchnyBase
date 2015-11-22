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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.prchny.base.exception.SerializationException;
import com.prchny.base.exception.SerializationException.SerializationErrorCode;
import com.prchny.base.transport.serialization.service.ISerializationService;

@Service("protostuffSerializationService")
public class ProtostuffSerializationServiceImpl implements
  ISerializationService {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(ProtostuffSerializationServiceImpl.class);
  
  @Override
  @SuppressWarnings("unchecked")
  public byte[] doSerialize(final Class<? extends Object> classType,
      final Object obj) {
  
    final LinkedBuffer buffer = LinkedBuffer.allocate(2048);
    byte[] data;
    try {
      @SuppressWarnings("rawtypes")
      final Schema schema = RuntimeSchema.getSchema(classType);
      data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    } finally {
      buffer.clear();
    }
    return data;
  }
  
  @Override
  @SuppressWarnings({
      "rawtypes", "unchecked"
  })
  public Object doDeserialize(final byte[] data,
      final Class<? extends Object> classType) throws SerializationException {
  
    final Schema schema = RuntimeSchema.getSchema(classType);
    Object object = null;
    try {
      object = classType.newInstance();
    } catch (final InstantiationException e) {
      LOG.error(
          "Unable to initialise request/response class : {}. May be zero argument constructor is missing.",
          classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.INSTANTIATION_EXCEPTION, e);
    } catch (final IllegalAccessException e) {
      LOG.error("Error deserializing protostuff for data.", e);
      throw new SerializationException(
          SerializationErrorCode.PROTOSTUFF_DESERIALIZATION_EXCEPTION, e);
    }
    ProtostuffIOUtil.mergeFrom(data, object, schema);
    return object;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object doDeserialize(final InputStream inputStream,
      final Class<? extends Object> classType) throws SerializationException {
  
    @SuppressWarnings("rawtypes")
    final Schema schema = RuntimeSchema.getSchema(classType);
    Object object = null;
    try {
      object = classType.newInstance();
      ProtostuffIOUtil.mergeFrom(inputStream, object, schema);
    } catch (final InstantiationException e) {
      LOG.error(
          "Unable to initialise request/response class : {}. May be zero argument constructor is missing.",
          classType);
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.INSTANTIATION_EXCEPTION, e);
    } catch (final IllegalAccessException e) {
      LOG.error("Error deserializing protostuff for data {}",
          inputStream.toString());
      LOG.error("Stack trace", e);
      throw new SerializationException(
          SerializationErrorCode.PROTOSTUFF_DESERIALIZATION_EXCEPTION, e);
    } catch (final IOException e) {
      LOG.error("Error deserializing protostuff for data {}",
          inputStream.toString());
      LOG.error("Stack trace", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return object;
  }
  
  @Override
  public Object doDeSerialize(final String json,
      final Class<? extends Object> classType) throws SerializationException {
  
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String doSerialize(final Object object) throws SerializationException {
  
    // TODO Auto-generated method stub
    return null;
  }
  
}
