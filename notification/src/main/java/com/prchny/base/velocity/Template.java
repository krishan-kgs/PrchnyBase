/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 21, 2010
 *  @author singla
 */

package com.prchny.base.velocity;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.prchny.base.cache.CacheManager;
import com.prchny.base.utils.DateUtils;
import com.prchny.base.utils.StringUtils;

public class Template implements Serializable {
  
  private static final long serialVersionUID = 4399479027409081738L;
  
  private final String template;
  
  private Template(final String sTemplate) {
  
    template = sTemplate;
  }
  
  public static Template compile(final String sTemplate) {
  
    return new Template(sTemplate);
  }
  
  public String evaluate(final Map<String, Object> params) {
  
    final VelocityContext context = new VelocityContext();
    context.put("dateutils", new DateUtils());
    context.put("stringutils", new StringUtils());
    context.put("cache", CacheManager.getInstance());
    context.put("StringEscapeUtils", new StringEscapeUtils());
    context.put("templatePathResolver",
        TemplatePathResolver.getTemplatePathResolver());
    for (final Map.Entry<String, Object> entry : params.entrySet()) {
      context.put(entry.getKey(), entry.getValue());
    }
    final StringWriter outWriter = new StringWriter();
    try {
      Velocity.evaluate(context, outWriter, "", template);
    } catch (final ParseErrorException e) {
      throw new RuntimeException(e);
    } catch (final MethodInvocationException e) {
      throw new RuntimeException(e);
    } catch (final ResourceNotFoundException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return outWriter.toString();
  }
  
  public String callbackFunction(final String functionName, final String value) {
  
    final StringBuilder outBuilder = new StringBuilder();
    return outBuilder.append(functionName).append("(").append(value)
        .append(")").toString();
  }
  
  public static void main(final String[] args) throws ParseErrorException,
      MethodInvocationException, ResourceNotFoundException, IOException {
  
    final Template t =
        new Template(
            "${reponse.voucher == null ? 'INVALID' : (response.voucher.consumed ? 'REDEEMED' : (response.valid ? 'VALID' : 'INVALID'))}");
    final Map<String, Object> params = new HashMap<String, Object>();
    System.out.println(t.evaluate(params));
  }
}
