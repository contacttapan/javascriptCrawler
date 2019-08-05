package com.interview.assignment.webcrawlerjs.domain;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JavaScriptDetails {
  private final String javaScriptSourceUrl;

  private final String downloadedFileName;

  private final String fileHash;

  public JavaScriptDetails(String javaScriptSourceUrl, String downloadedFileName,
      String fileHash) {
    this.javaScriptSourceUrl = javaScriptSourceUrl;
    this.downloadedFileName = downloadedFileName;
    this.fileHash = fileHash;
  }

  @Override
  public String toString() {
    return "JavaScriptDetails{" +
        "javaScriptSourceUrl='" + javaScriptSourceUrl + '\'' +
        ", downloadedFileName='" + downloadedFileName + '\'' +
        ", fileHash='" + fileHash + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JavaScriptDetails that = (JavaScriptDetails) o;
    return Objects.equal(getFileHash(), that.getFileHash());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getFileHash());
  }
}
