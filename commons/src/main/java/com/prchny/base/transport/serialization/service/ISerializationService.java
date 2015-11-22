/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 03-May-2012
 *  @author rahul
 */

package com.prchny.base.transport.serialization.service;

import java.io.InputStream;

import com.prchny.base.exception.SerializationException;

public interface ISerializationService {
  
  public byte[] doSerialize(Class<? extends Object> classType, Object obj)
      throws SerializationException;
  
  public Object doDeserialize(byte[] data, Class<? extends Object> classType)
      throws SerializationException;
  
  public Object doDeserialize(InputStream inputStream,
      Class<? extends Object> clazz) throws SerializationException;
  
  Object doDeSerialize(String json, Class<? extends Object> classType)
      throws SerializationException;
  
  String doSerialize(Object object) throws SerializationException;
}
