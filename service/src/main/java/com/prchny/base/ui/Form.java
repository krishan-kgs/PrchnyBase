/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 28, 2010
 *  @author singla
 */

package com.prchny.base.ui;

import java.util.ArrayList;
import java.util.List;

public class Form {
  
  private String url;
  
  private MethodType method = MethodType.POST;
  
  private List<Parameter> parameters = new ArrayList<Parameter>();
  
  public enum MethodType {
    GET,
    POST
  }
  
  public String getUrl() {
  
    return url;
  }
  
  public void setUrl(final String url) {
  
    this.url = url;
  }
  
  public List<Parameter> getParameters() {
  
    return parameters;
  }
  
  public String getParameter(final String name) {
  
    for (final Parameter parameter : parameters) {
      if (parameter.getName().equals(name)) {
        return parameter.getValue();
      }
    }
    return null;
  }
  
  public void setParameters(final List<Parameter> parameters) {
  
    this.parameters = parameters;
  }
  
  public void addParameter(final Parameter parameter) {
  
    parameters.add(parameter);
  }
  
  public void addParameter(final String name, final String value,
      final String displayName, final boolean mandatory) {
  
    final Parameter parameter =
        new Parameter(name, value, displayName, mandatory);
    parameters.add(parameter);
  }
  
  public void addParameter(final String name, final String value) {
  
    addParameter(name, value, null, true);
  }
  
  public void removeParameter(final String name) {
  
    for (final Parameter param : parameters) {
      if (name.equalsIgnoreCase(param.getName())) {
        parameters.remove(param);
        break;
      }
    }
  }
  
  public void setMethod(final MethodType method) {
  
    this.method = method;
  }
  
  public MethodType getMethod() {
  
    return method;
  }
  
  public static class Parameter {
    
    private String name;
    
    private String value;
    
    private String displayName;
    
    private boolean mandatory;
    
    public Parameter(final String name, final String value,
        final String displayName, final boolean mandatory) {
    
      this.name = name;
      this.value = value;
      this.mandatory = mandatory;
      this.displayName = displayName;
    }
    
    public Parameter(final String name, final String value) {
    
      this(name, value, null, true);
    }
    
    public Parameter(final String name) {
    
      this(name, null, null, true);
    }
    
    public Parameter(final String name, final boolean mandatory) {
    
      this(name, null, null, mandatory);
    }
    
    public Parameter() {
    
    }
    
    public String getName() {
    
      return name;
    }
    
    public void setName(final String name) {
    
      this.name = name;
    }
    
    public String getValue() {
    
      return value;
    }
    
    public void setValue(final String value) {
    
      this.value = value;
    }
    
    public boolean isMandatory() {
    
      return mandatory;
    }
    
    public void setMandatory(final boolean mandatory) {
    
      this.mandatory = mandatory;
    }
    
    public void setDisplayName(final String displayName) {
    
      this.displayName = displayName;
    }
    
    public String getDisplayName() {
    
      return displayName;
    }
    
    @Override
    public String toString() {
    
      return "Parameter [name=" + name + ", value=" + value + "]";
    }
  }
}
