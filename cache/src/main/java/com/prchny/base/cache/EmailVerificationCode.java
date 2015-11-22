package com.prchny.base.cache;

public class EmailVerificationCode implements Cachable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1748761634199229468L;
  
  private String code = "";
  
  private String source = "";
  
  private String targetUrl = "";
  
  public String getCode() {
  
    return code;
  }
  
  public void setCode(final String code) {
  
    this.code = code;
  }
  
  public String getSource() {
  
    return source;
  }
  
  public void setSource(final String source) {
  
    this.source = source;
  }
  
  public String getTargetUrl() {
  
    return targetUrl;
  }
  
  public void setTargetUrl(final String targetUrl) {
  
    this.targetUrl = targetUrl;
  }
  
  @Override
  public String toMemString() {
  
    return new StringBuilder(code).append(",").append(source).append(",")
        .append(targetUrl).toString();
  }
  
  @Override
  public Cachable loadFromMemString(final String str) {
  
    final EmailVerificationCode emailVerificationCode =
        new EmailVerificationCode();
    final String[] verificationCode = str.split(",");
    if (verificationCode.length > 0) {
      emailVerificationCode.setCode(verificationCode[0]);
    }
    if (verificationCode.length > 1) {
      emailVerificationCode.setSource(verificationCode[1]);
    }
    if (verificationCode.length > 2) {
      emailVerificationCode.setTargetUrl(verificationCode[2]);
    }
    return emailVerificationCode;
  }
}
