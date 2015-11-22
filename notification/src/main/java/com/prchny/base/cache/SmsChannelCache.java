package com.prchny.base.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prchny.base.annotations.Cache;
import com.prchny.base.entity.SmsChannel;
import com.prchny.base.notification.sms.ISmsSender;
import com.prchny.base.notification.sms.impl.SmsSenderBuilder;

@Cache(name = "smsChannelCache")
public class SmsChannelCache {
  
  private final Map<String, ISmsSender> nameToChannels =
      new HashMap<String, ISmsSender>();
  
  private final Map<Integer, ISmsSender> idToChannels =
      new HashMap<Integer, ISmsSender>();
  
  private final List<SmsChannel> smsChannels = new ArrayList<SmsChannel>();
  
  public void addChannel(final SmsChannel channel) {
  
    if (!nameToChannels.containsKey(channel.getName())) {
      final ISmsSender sender =
          SmsSenderBuilder.buildSender(channel.getSenderClassName());
      sender.initialize(channel);
      nameToChannels.put(channel.getName(), sender);
      idToChannels.put(channel.getId(), sender);
      smsChannels.add(channel);
    }
  }
  
  public ISmsSender getSmsSenderByChannelName(final String name) {
  
    final ISmsSender sender = nameToChannels.get(name);
    if (sender == null) {
      throw new IllegalStateException("channel not loaded:" + name);
    } else {
      return sender;
    }
  }
  
  public ISmsSender getSmsSenderByChannelId(final Integer channelId) {
  
    final ISmsSender sender = idToChannels.get(channelId);
    if (sender == null) {
      throw new IllegalStateException("channel not loaded:" + channelId);
    } else {
      return sender;
    }
  }
  
  public List<SmsChannel> getSmsChannels() {
  
    return smsChannels;
  }
  
}
