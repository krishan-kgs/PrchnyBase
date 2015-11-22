/*
 *  Copyright 2013 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Jun 4, 2013
 *  @author venu
 */

package com.prchny.base.transport.serialization.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prchny.base.exception.SerializationException;
import com.prchny.base.transport.serialization.service.ISerializationService;

@Service("xmlSerializationService")
public class XmlSerializationServiceImpl implements ISerializationService {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(XmlSerializationServiceImpl.class);
  
  @Override
  public byte[] doSerialize(final Class<? extends Object> classType,
      final Object obj) throws SerializationException {
  
    JAXBContext context = null;
    Marshaller marshaller = null;
    final StringWriter StringWriter = new StringWriter();
    try {
      context = JAXBContext.newInstance(classType);
      marshaller = context.createMarshaller();
      marshaller.marshal(obj, StringWriter);
    } catch (final JAXBException e) {
      LOG.error("error while marshalling the XML", e);
    }
    return StringWriter.toString().getBytes();
  }
  
  @Override
  public Object doDeserialize(final byte[] data,
      final Class<? extends Object> classType) throws SerializationException {
  
    JAXBContext context = null;
    Unmarshaller unmarshaller = null;
    Object object = null;
    try {
      context = JAXBContext.newInstance(classType);
      unmarshaller = context.createUnmarshaller();
      object = unmarshaller.unmarshal(new ByteArrayInputStream(data));
    } catch (final JAXBException e) {
      LOG.error("error while marshalling the XML", e);
    }
    return object;
  }
  
  @Override
  public Object doDeserialize(final InputStream inputStream,
      final Class<? extends Object> clazz) throws SerializationException {
  
    JAXBContext context = null;
    Unmarshaller unmarshaller = null;
    Object object = null;
    try {
      context = JAXBContext.newInstance(clazz);
      unmarshaller = context.createUnmarshaller();
      object = unmarshaller.unmarshal(inputStream);
    } catch (final JAXBException e) {
      LOG.error("error while marshalling the XML", e);
    }
    
    return object;
  }
  
  @Override
  public Object doDeSerialize(final String xml,
      final Class<? extends Object> classType) throws SerializationException {
  
    JAXBContext context = null;
    Unmarshaller unmarshaller = null;
    Object object = null;
    try {
      context = JAXBContext.newInstance(classType);
      unmarshaller = context.createUnmarshaller();
      object = unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
    } catch (final JAXBException e) {
      LOG.error("error while marshalling the XML", e);
    }
    
    return object;
  }
  
  @Override
  public String doSerialize(final Object object) throws SerializationException {
  
    JAXBContext context = null;
    Marshaller marshaller = null;
    final StringWriter StringWriter = new StringWriter();
    try {
      context = JAXBContext.newInstance(object.getClass());
      marshaller = context.createMarshaller();
      marshaller.marshal(object, StringWriter);
    } catch (final JAXBException e) {
      LOG.error("error while marshalling the XML", e);
    }
    return StringWriter.toString();
  }
  
}
