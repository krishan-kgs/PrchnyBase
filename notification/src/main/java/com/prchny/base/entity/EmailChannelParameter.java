/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Mar 15, 2011
 *  @author rahul
 */

package com.prchny.base.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "email_channel_parameter")
public class EmailChannelParameter implements Serializable {
  
  private static final long serialVersionUID = 1031570707282643768L;
  
  private Integer id;
  
  private EmailChannel emailChannel;
  
  private String name;
  
  private String value;
  
  public EmailChannelParameter() {
  
  }
  
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  public Integer getId() {
  
    return id;
  }
  
  public void setId(final Integer id) {
  
    this.id = id;
  }
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "email_channel_id", nullable = false)
  public EmailChannel getEmailChannel() {
  
    return emailChannel;
  }
  
  public void setEmailChannel(final EmailChannel emailChannel) {
  
    this.emailChannel = emailChannel;
  }
  
  @Column(name = "name", nullable = false, length = 50)
  public String getName() {
  
    return name;
  }
  
  public void setName(final String name) {
  
    this.name = name;
  }
  
  @Column(name = "value", length = 200)
  public String getValue() {
  
    return value;
  }
  
  public void setValue(final String value) {
  
    this.value = value;
  }
  
}
