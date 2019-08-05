package com.interview.assignment.webcrawlerjs;

import com.interview.assignment.webcrawlerjs.crawl.search.SearchStringValidator;
import com.interview.assignment.webcrawlerjs.crawl.search.SearchStringValidatorImpl;
import javax.xml.bind.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


public class SearchStringValidatorShould {


  SearchStringValidator<String> validator = new SearchStringValidatorImpl(10);

  @Test
  public void validateAllowedSearchString(){
    Assert.assertEquals("Validator should allow", true, validator.isValid("TEST"));

  }

  @Test(expected = javax.validation.ValidationException.class)
  public void validateBlankSearchString(){
    validator.isValid("");

  }

  @Test(expected = javax.validation.ValidationException.class)
  public void validateNullSearchString(){
    validator.isValid(null);

  }
}
