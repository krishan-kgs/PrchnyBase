
package com.prchny.base.audit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.prchny.base.audit.annotation.AuditableClass;
import com.prchny.base.audit.annotation.AuditableField;

/**
 * Manager class which handles auditing tasks
 * 
 * @author ashish.saxena
 * 
 */
@Component
public class AuditingManager {
  
  private static String AUDITING_LOGGER_NAME = "AuditingLogger";
  
  private static Logger AUDITING_LOGGER = LoggerFactory
      .getLogger(AUDITING_LOGGER_NAME);
  
  {
    if (LoggerFactory.getLogger(AUDITING_LOGGER_NAME) == null) {
      // Rely on primary logger, if auditing manager has not been configured
      AUDITING_LOGGER = LoggerFactory.getLogger(AuditingManager.class);
    }
  }
  
  private final ExecutorService executorService = new ThreadPoolExecutor(20,
      50, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
  
  /**
   * The main method to be called to audit an object
   * 
   * @param o
   * @param bindingReferenceValue
   *          - The int value which will be user to sew together the request and
   *          the response of a service.
   */
  public void audit(final Object o) {
  
    executorService.execute(new Auditor(o));
  }
  
  /**
   * Class which wraps around the object to be audited.
   * 
   * @author ashish
   * 
   */
  private class Auditor implements Runnable {
    
    private final Object auditableObject;
    
    Auditor(final Object auditableMainObject) {
    
      auditableObject = auditableMainObject;
    }
    
    @Override
    public void run() {
    
      final long startTime = System.currentTimeMillis();
      
      if (auditableObject == null) {
        return;
      }
      if (auditableObject.getClass().isAnnotationPresent(AuditableClass.class)) {
        try {
          processAuditableObject(auditableObject);
        } catch (final Exception exception) {
          AUDITING_LOGGER.error(
              "Exception encountered while saving auditing data for: "
                  + auditableObject.getClass(), exception);
        }
      }
      
      else {
        return;
      }
      final long endTime = System.currentTimeMillis();
      final long totalTime = endTime - startTime;
      AUDITING_LOGGER.info("RsT: " + totalTime + "ms for auditing!");
      
    }
    
    /**
     * @param objectClazz
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SQLException
     */
    private void processAuditableObject(final Object auditableObject)
        throws IllegalAccessException, InvocationTargetException, SQLException {
    
      final Map<String, String> fieldValueMappings =
          processAuditableFields(auditableObject);
      
      if ((fieldValueMappings == null) || (fieldValueMappings.size() == 0)) {
        AUDITING_LOGGER.warn("Auditable class: " + auditableObject.getClass()
            + " contains no autible fields!");
      } else {
        logAuditData(auditableObject.getClass(), fieldValueMappings);
      }
      
    }
    
    /**
     * 
     * Returns a map containing field name Vs field value mapping.
     * 
     * @return
     */
    private Map<String, String> processAuditableFields(
        final Object auditableTypeObject) {
    
      final Map<String, String> fieldValueMappings =
          new HashMap<String, String>();
      // Cant use getFields() as it would not return private fields
      final Field[] superClassFields =
          auditableTypeObject.getClass().getSuperclass().getDeclaredFields();
      final Field[] fields = auditableTypeObject.getClass().getDeclaredFields();
      
      for (int i = 0; i < superClassFields.length; i++) {
        extractAuditableFIeldNameValue(auditableTypeObject, fieldValueMappings,
            superClassFields, i);
      }
      
      for (int i = 0; i < fields.length; i++) {
        extractAuditableFIeldNameValue(auditableTypeObject, fieldValueMappings,
            fields, i);
      }
      
      return fieldValueMappings;
    }
    
    private void extractAuditableFIeldNameValue(
        final Object auditableTypeObject,
        final Map<String, String> fieldValueMappings, final Field[] fields,
        final int i) {
    
      final Field field = fields[i];
      
      try {
        
        String fieldStringValue = null, fieldNameForAuditing = null;
        
        if (field.isAnnotationPresent(AuditableField.class)) {
          
          final int modifier = field.getModifiers();
          if (Modifier.isStatic(modifier)) {
            // Handling static field
            fieldStringValue = getFieldValue(null, field);
          } else {
            
            fieldStringValue = getFieldValue(auditableTypeObject, field);
            
          }
          
          fieldNameForAuditing = getFieldNameForAuditing(field);
          fieldValueMappings.put(fieldNameForAuditing, fieldStringValue);
          
        }
      } catch (final Exception e) {
        AUDITING_LOGGER.error(
            "Exception while reflecting on field " + field.getName() + " of "
                + auditableTypeObject.getClass().getName(), e);
      }
      
    }
    
    /**
     * AuditableField could be declared with an explicit field name to be used
     * instead of the actual field name! Check for this condition! If not found,
     * proceed with the class field name.
     * 
     * @param field
     * @return
     */
    private String getFieldNameForAuditing(final Field field) {
    
      String fieldNameForAuditing = null;
      
      final String explicitFieldNameForAuditing =
          field.getAnnotation(AuditableField.class).value().trim();
      if ((explicitFieldNameForAuditing == null)
          || (explicitFieldNameForAuditing.length() == 0)) {
        fieldNameForAuditing = field.getName();
        
      } else {
        fieldNameForAuditing = explicitFieldNameForAuditing;
        
      }
      return fieldNameForAuditing;
    }
    
    private String getFieldValue(final Object o, final Field field) {
    
      // if (field == null || o == null) {
      // return null;
      // }
      
      String fieldStringValue = null;
      try {
        field.setAccessible(true);
        
        if (field.isAnnotationPresent(AuditableField.class)) {
          
          if (field.getType().isAnnotationPresent(AuditableClass.class)) {
            fieldStringValue =
                getStringRepresentationSubAuditableType(field, field.get(o));
          } else {
            
            fieldStringValue =
                field.get(o) == null ? null : field.get(o).toString();
          }
          
        }
      } catch (final IllegalArgumentException e) {
        AUDITING_LOGGER.warn("Strange! Illegal argument supplied!", e);
        e.printStackTrace();
      } catch (final IllegalAccessException e) {
        AUDITING_LOGGER.warn("Strange! Missing field!", e);
      }
      return fieldStringValue;
    }
    
    /**
     * Assumes that the field class is @AuditableType. Check this before calling
     * this method!
     * 
     * @param field
     * @return
     */
    private String getStringRepresentationSubAuditableType(final Field field,
        final Object fieldValueObject) {
    
      if ((field == null) || (fieldValueObject == null)) {
        return null;
      }
      final StringBuilder stringBuilder = new StringBuilder();
      final Class<?> fieldClazz = field.getType();
      final Field[] subFields = fieldClazz.getDeclaredFields();
      // Cant use getFields() as it would not return private fields
      final Field[] superClassFields =
          fieldClazz.getSuperclass().getDeclaredFields();
      
      getStringRepOfSubAuditableTyoe(fieldValueObject, stringBuilder,
          superClassFields);
      getStringRepOfSubAuditableTyoe(fieldValueObject, stringBuilder, subFields);
      
      // Remove the last comma and space!
      stringBuilder.delete(stringBuilder.length() - 2,
          stringBuilder.length() - 1);
      
      return stringBuilder.toString();
    }
    
    private void getStringRepOfSubAuditableTyoe(final Object fieldValueObject,
        final StringBuilder stringBuilder, final Field[] subFields) {
    
      String subFieldName;
      if (subFields != null) {
        
        for (final Field subField : subFields) {
          if (subField.isAnnotationPresent(AuditableField.class)) {
            
            subFieldName = getFieldNameForAuditing(subField);
            final String subFieldValue =
                getFieldValue(fieldValueObject, subField);
            stringBuilder.append("\"").append(subFieldName).append("\":\"")
                .append(subFieldValue).append("\", ");
          }
        }
      }
    }
    
    /**
     * @param objectClazz
     * @param fieldValues
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void logAuditData(final Class<?> objectClazz,
        final Map<String, String> fieldValueMappings)
        throws IllegalAccessException, InvocationTargetException {
    
      final StringBuilder objAuditStringRepresentation =
          new StringBuilder().append(objectClazz.getSimpleName()).append(":[");
      
      for (final Entry<String, String> fieldValueMapping : fieldValueMappings
          .entrySet()) {
        
        objAuditStringRepresentation.append("\"")
            .append(fieldValueMapping.getKey()).append("\"").append(": {")
            .append(fieldValueMapping.getValue()).append("}, ");
        
      }
      objAuditStringRepresentation.delete(
          objAuditStringRepresentation.length() - 2,
          objAuditStringRepresentation.length() - 1);
      objAuditStringRepresentation.append("]");
      
      AUDITING_LOGGER.info(objAuditStringRepresentation.toString());
      
    }
  }
  
}
