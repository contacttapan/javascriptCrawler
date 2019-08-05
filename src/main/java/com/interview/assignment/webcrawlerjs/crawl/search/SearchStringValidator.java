package com.interview.assignment.webcrawlerjs.crawl.search;


import javax.validation.ValidationException;

public interface SearchStringValidator<T> {

  public boolean isValid(T field) throws ValidationException;

}


