
package com.prchny.base.transport.serialization.service.impl;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.prchny.base.transport.service.ITransportService.Protocol;


public class ProtocolAdapter extends XmlAdapter<String, Protocol> {
  
  @Override
  public String marshal(final Protocol protocol) throws Exception {
  
    return protocol.name();
  }
  
  @Override
  public Protocol unmarshal(final String string) throws Exception {
  
    try {
      return Protocol.valueOf(string.trim());
    } catch (final Exception e) {
      throw new JAXBException(e);
    }
  }
  
}
