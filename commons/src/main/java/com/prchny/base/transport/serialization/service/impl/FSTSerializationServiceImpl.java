/**
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.prchny.base.transport.serialization.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prchny.base.exception.SerializationException;
import com.prchny.base.exception.SerializationException.SerializationErrorCode;
import com.prchny.base.transport.serialization.service.ISerializationService;

/**
 * @version 1.0, Jan 20, 2014
 * @author gaurav
 */
@Service("fstSerializationService")
public class FSTSerializationServiceImpl implements ISerializationService {
  
  private static final FSTConfiguration conf = FSTConfiguration
      .createDefaultConfiguration();
  
  private static final Logger LOG = LoggerFactory
      .getLogger(FSTSerializationServiceImpl.class);
  static {
    conf.setForceSerializable(true);
  }
  
  @Override
  public byte[] doSerialize(final Class<? extends Object> classType,
      final Object obj) throws SerializationException {
  
    final ByteArrayOutputStream byteArrayOutputStream =
        new ByteArrayOutputStream();
    final FSTObjectOutput out = conf.getObjectOutput(byteArrayOutputStream);
    byte[] responseData = null;
    try {
      out.writeObject(obj, Object.class);
      out.flush();
    } catch (final IOException e) {
      LOG.error("IO Exception occured during FST Serialization ", e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    responseData = byteArrayOutputStream.toByteArray();
    try {
      byteArrayOutputStream.close();
    } catch (final IOException e) {
      LOG.error(
          "IO Exception occured when closing byteArrayOutputStream in FST Serialization ",
          e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return responseData;
  }
  
  @Override
  public Object doDeserialize(final byte[] data,
      final Class<? extends Object> classType) throws SerializationException {
  
    final ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(data);
    final FSTObjectInput in = conf.getObjectInput(byteArrayInputStream);
    Object result = null;
    try {
      result = in.readObject(Object.class);
    } catch (final Exception e) {
      LOG.error("Exception occured during FST Deserialization ", e);
      throw new SerializationException(
          SerializationErrorCode.FST_DESERIALIZATION_EXCEPTION, e);
    }
    try {
      byteArrayInputStream.close();
    } catch (final IOException e) {
      LOG.error(
          "IO Exception occured while closing byteArrayInputStream in FST Deserialization ",
          e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return result;
  }
  
  @Override
  public Object doDeserialize(final InputStream inputStream,
      final Class<? extends Object> clazz) throws SerializationException {
  
    final FSTObjectInput in = conf.getObjectInput(inputStream);
    Object result = null;
    try {
      result = in.readObject(Object.class);
    } catch (final Exception e) {
      LOG.error("Exception occured during FST Deserialization ", e);
      throw new SerializationException(
          SerializationErrorCode.FST_DESERIALIZATION_EXCEPTION, e);
    }
    try {
      inputStream.close();
    } catch (final IOException e) {
      LOG.error(
          "IO Exception occured while closing byteArrayInputStream in FST Deserialization ",
          e);
      throw new SerializationException(SerializationErrorCode.IO_EXCEPTION, e);
    }
    return result;
  }
  
  @Override
  public Object doDeSerialize(final String json,
      final Class<? extends Object> classType) throws SerializationException {
  
    return null;
  }
  
  @Override
  public String doSerialize(final Object object) throws SerializationException {
  
    return null;
  }
  
}
