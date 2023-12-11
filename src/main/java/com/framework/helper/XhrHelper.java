package com.framework.helper;

import java.util.Map;
import java.util.Optional;

import java.util.Arrays;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.devtools.v114.network.model.Headers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.driver.Driver;
import com.framework.driver.WebCommonDriver;

public class XhrHelper {

  private DevTools devTools;
  private Driver driver;

  public XhrHelper(Driver driver) {
    this.driver = driver;
    this.devTools = ((ChromeDriver) ((WebCommonDriver) driver).get()).getDevTools();
  }

  public Object[] getRequestHeader(String requestUrl, String method) {
    final String[] url = new String[1];
    final Headers[] header = new Headers[1];

    devTools.createSession();
    devTools.send(Network.enable(Optional.of(100000000), Optional.empty(), Optional.empty()));
    devTools.addListener(Network.requestWillBeSent(), req -> {
      if (req.getRequest().getUrl().contains(requestUrl) && req.getRequest().getMethod().equals(method)) {
        url[0] = req.getRequest().getUrl();
        header[0] = req.getRequest().getHeaders();
      }
    });
    return new Object[] { url, header };
  }

  public Object[] getRequestHeaderWithRegex(String regex, String method) {
    final String[] url = new String[1];
    final Headers[] header = new Headers[1];

    devTools.createSession();
    devTools.send(Network.enable(Optional.of(100000000), Optional.empty(), Optional.empty()));
    devTools.addListener(Network.requestWillBeSent(), req -> {
      if (req.getRequest().getUrl().matches(regex) && req.getRequest().getMethod().equals(method)) {
        url[0] = req.getRequest().getUrl();
        header[0] = req.getRequest().getHeaders();
      }
    });
    return new Object[] { url, header };
  }

  public Object getResponseBody(Object[] request) {
    Object response = ((JavascriptExecutor) ((WebCommonDriver) driver).get())
        .executeScript(("var xhr = new XMLHttpRequest();" +
            "xhr.open('GET', arguments[0], false);" +
            "Object.entries(arguments[1][0]).forEach(([key, value]) => {xhr.setRequestHeader(key, value); });" +
            "return JSON.parse(xhr.reponse);"), request[0], request[1]);
    return response != null ? response : null;
  }
  
  public Object getValue(Object obj, String... keys) {
    ObjectMapper objectMapper = new ObjectMapper();
    Map result = objectMapper.convertValue(obj, Map.class);
    if (keys.length > 1) {
      return result.containsKey(keys[0]) ? this.getValue(result.get(keys[0]), Arrays.copyOfRange(keys, 1, keys.length))
          : null;
    } else {
      return result.containsKey(keys[0]) ? result.get(keys[0]) : null;
    }
  }
}
