
package com.prchny.base.entity;

// Generated Aug 20, 2010 8:23:32 PM by Hibernate Tools 3.3.0.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * EmailChannel generated by hbm2java
 */
@Entity
@Table(name = "email_channel", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class EmailChannel implements java.io.Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = -7563035309766065891L;
  
  private int id;
  
  private String name;
  
  private String className;
  
  private boolean enabled;
  
  private Set<EmailChannelParameter> emailChannelParameters =
      new HashSet<EmailChannelParameter>();
  
  public EmailChannel() {
  
  }
  
  public EmailChannel(final int id, final String name, final String className,
      final boolean enabled) {
  
    super();
    this.id = id;
    this.name = name;
    this.className = className;
    this.enabled = enabled;
  }
  
  @Id
  @Column(name = "id", unique = true, nullable = false)
  public int getId() {
  
    return id;
  }
  
  public void setId(final int id) {
  
    this.id = id;
  }
  
  @Column(name = "name", unique = true, nullable = false, length = 45)
  public String getName() {
  
    return name;
  }
  
  public void setName(final String name) {
  
    this.name = name;
  }
  
  @Column(name = "class_name", nullable = false, length = 200)
  public String getClassName() {
  
    return className;
  }
  
  public void setClassName(final String className) {
  
    this.className = className;
  }
  
  @Column(name = "enabled", nullable = false)
  public boolean isEnabled() {
  
    return enabled;
  }
  
  public void setEnabled(final boolean enabled) {
  
    this.enabled = enabled;
  }
  
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "emailChannel")
  public Set<EmailChannelParameter> getEmailChannelParameters() {
  
    return emailChannelParameters;
  }
  
  public void setEmailChannelParameters(
      final Set<EmailChannelParameter> emailChannelParameters) {
  
    this.emailChannelParameters = emailChannelParameters;
  }
  
  @Transient
  public String getParameter(final String name) {
  
    for (final EmailChannelParameter channelParameter : getEmailChannelParameters()) {
      if (channelParameter.getName().equals(name)) {
        return channelParameter.getValue();
      }
    }
    return null;
  }
  
}
