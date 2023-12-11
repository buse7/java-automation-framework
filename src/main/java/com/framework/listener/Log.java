package com.framework.listener;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ch.qos.logback.classic.Level;

public class Log {
  private Logger logger;

  private Log() {
  }
  
  public static Log getInstance() {
    return Log.Holder.instance;
  }
    
  private static class Holder {
    public static final Log instance = new Log();
  }

  public Log setLogger(Level level) {
    logger = LoggerFactory.getLogger(this.getClass());
    final ch.qos.logback.classic.Logger logger2 = (ch.qos.logback.classic.Logger) logger;
    logger2.setLevel(level);

    return this;
  }

  public Logger getLogger() {
    if (logger == null) {
      setLogger(Level.WARN);
    }
    return logger;
  }
}