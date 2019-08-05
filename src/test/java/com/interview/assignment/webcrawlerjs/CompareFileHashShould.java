package com.interview.assignment.webcrawlerjs;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class CompareFileHashShould {

  private String sourcejsFile_one="sample/testJs1.js";
  private String sourcejsFile_two="sample/testJs2.js";
  private String sourcejsFile_three="sample/testJs3.js";



  @Test
  public void campareSameFiles() {
    try {
      Assert.assertEquals("Both js Hash is equal",
          Files.hash(new File(sourcejsFile_one), Hashing.sha1()).toString(),
          Files.hash(new File(sourcejsFile_two), Hashing.sha1()).toString());

    }catch (Exception e){
      e.printStackTrace();
    }
  }
  @Test
  public void campareDifferentFiles() {
    try {
      Assert.assertNotEquals("Both js Hash is not equal",
          Files.hash(new File(sourcejsFile_one), Hashing.sha1()).toString(),
          Files.hash(new File(sourcejsFile_three), Hashing.sha1()).toString());

    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
