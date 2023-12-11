package com.framework.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class File {

  public String download(String targetUrl, String fileName) throws MalformedURLException {
    URL url = new URL(targetUrl);

    try (InputStream in = url.openStream();
    ReadableByteChannel readableByteChannel = Channels.newChannel(in);
    FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
      fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
      
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }

    return fileName;
  }

  public Boolean remove(String fileName) {
    java.io.File file = new java.io.File(fileName);

    if (file.exists()) {
      return file.delete();
    } else {
      return false;
    } 
  } 
}
