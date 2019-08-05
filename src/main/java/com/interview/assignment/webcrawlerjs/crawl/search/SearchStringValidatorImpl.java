package com.interview.assignment.webcrawlerjs.crawl.search;

import java.util.Objects;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchStringValidatorImpl implements
    SearchStringValidator<String> {

  @Value("${crawer.maxSerachStringAllowed:200}")
  public int allowedStringLenth;

  public SearchStringValidatorImpl() {
  }

  public SearchStringValidatorImpl(int allowedStringLenth) {
    this.allowedStringLenth = allowedStringLenth;
  }


  @Override
  public boolean isValid(String field) throws ValidationException {
    if (Objects.isNull(field)) {
      throw new ValidationException("Search String is null ");
    }
    if (Objects.equals("", field)) {
      throw new ValidationException("BLANK Search String  is not allowed length ");
    }
    if (field.length() > allowedStringLenth) {
      throw new ValidationException("Search String lenth is exceeding the allowed length ");
    }
    return true;

  }
}
