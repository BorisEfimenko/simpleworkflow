package org.simpleworkflow.converter;


import org.dozer.DozerGlobalConverter;
import org.dozer.Mapper;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
@Component
public class LocalDateConverter extends DozerGlobalConverter<LocalDate, LocalDate>  {
Mapper mapper;
  public LocalDateConverter() {
      super(LocalDate.class, LocalDate.class);
  }

  @Override
  public LocalDate convertTo(LocalDate source, LocalDate destination) {
      return convertLocalDate(source);
  }

  @Override
  public LocalDate convertFrom(LocalDate source, LocalDate destination) {
      return convertLocalDate(source);
  }
  
  /**
   * Converts a <code>LocalDate</code> to a new instance.
   */
  private LocalDate convertLocalDate(LocalDate source) {
      LocalDate result = null;
      
      if (source != null) {
          result = new LocalDate(source);
      }

      return result;        
  }

}