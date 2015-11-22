package com.prchny.base.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prchny.base.annotations.Cache;
import com.prchny.base.entity.EmailChannel;
import com.prchny.base.notification.email.IEmailSender;

@Cache(name = "emailChannelCache")
public class EmailChannelCache {
  
  private final Map<String, IEmailSender> nameToEmailSenders =
      new HashMap<String, IEmailSender>();
  
  private final Map<Integer, IEmailSender> idToEmailSenders =
      new HashMap<Integer, IEmailSender>();
  
  private final List<EmailChannel> emailChannels =
      new ArrayList<EmailChannel>();
  
  @SuppressWarnings("unchecked")
  public void addChannel(final EmailChannel channel)
      throws InstantiationException, IllegalAccessException,
      ClassNotFoundException {
  
    if (!nameToEmailSenders.containsKey(channel.getName())) {
      final Class<IEmailSender> channelClass =
          (Class<IEmailSender>) Class.forName(channel.getClassName());
      final IEmailSender sender = channelClass.newInstance();
      sender.initialize(channel);
      nameToEmailSenders.put(channel.getName(), sender);
      idToEmailSenders.put(channel.getId(), sender);
      emailChannels.add(channel);
    }
  }
  
  public IEmailSender getEmailSenderByChannelName(final String name) {
  
    final IEmailSender sender = nameToEmailSenders.get(name);
    if (sender == null) {
      throw new IllegalStateException("channel not loaded:" + name);
    } else {
      return sender;
    }
  }
  
  public IEmailSender getEmailSenderByChannelId(final int emailChannelId) {
  
    final IEmailSender sender = idToEmailSenders.get(emailChannelId);
    if (sender == null) {
      throw new IllegalStateException("channel not loaded:" + emailChannelId);
    } else {
      return sender;
    }
  }
  
  public List<EmailChannel> getEmailChannels() {
  
    return emailChannels;
  }
}
