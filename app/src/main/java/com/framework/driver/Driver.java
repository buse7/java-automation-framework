package com.framework.driver;

import org.slf4j.Logger;
import ch.qos.logback.classic.Level;
import com.framework.listener.Log;

// https://debaeloper.tistory.com/35
public abstract class Driver {
  final int width;
  final int height;
  final boolean maxWindowSize;
  final String windowPosition;
  final boolean isRecordVideo;
  final DRIVER_TYPE driverType;
  final String driverName;
  final boolean headless;
  final boolean cameraPermissionByPass;
  final String driverVersion;
  Level level;
  Logger logger;
  Log log;

  abstract static class Builder<T extends Builder<T>> {
    int width;
    int height;
    boolean maxWindowSize;
    String windowPosition;
    boolean isRecordVideo;
    DRIVER_TYPE driverType;
    String driverName;
    boolean headless;
    boolean cameraPermissionByPass;
    String driverVersion;
    Level level;

    public Builder(DRIVER_TYPE driverType) {
      this.driverType = driverType;
    }

    public T width(int _width) {
      width = _width;
      return self();
    }

    public T height(int _height) {
      height = _height;
      return self();
    }

    public T maxWindowSize(boolean _maxWindowSize) {
      maxWindowSize = _maxWindowSize;
      return self();
    }

    public T windowPosition(String _windowPosition) {
      windowPosition = _windowPosition;
      return self();
    }

    public T isRecordVideo(boolean _isRecordVideo) {
      isRecordVideo = _isRecordVideo;
      return self();
    }

    public T driverType(DRIVER_TYPE _driverType) {
      driverType = _driverType;
      return self();
    }

    public T driverName(String _driverName) {
      driverName = _driverName;
      return self();
    }

    public T headless(boolean _headless) {
      headless = _headless;
      return self();
    }

    public T cameraPermissionByPass(boolean _cameraPermissionByPass) {
      cameraPermissionByPass = _cameraPermissionByPass;
      return self();
    }

    public T logLevel(Level _level) {
      level = _level;
      return self();
    }

    public T driverVersion(String _driverVersion) {
      driverVersion = _driverVersion;
      return self();
    }

    abstract Driver build() throws Exception;

    protected abstract T self();

  }
  
  public Driver(Builder<?> builder) {
    width = builder.width;
    height = builder.height;
    maxWindowSize = builder.maxWindowSize;
    windowPosition = builder.windowPosition != null ? builder.windowPosition : null;
    isRecordVideo = builder.isRecordVideo;
    driverType = builder.driverType;
    driverName = builder.driverName;
    headless = builder.headless;
    cameraPermissionByPass = builder.cameraPermissionByPass;
    driverVersion = builder.driverVersion != null ? builder.driverVersion : null;
    level = builder.level != null ? builder.level : Level.ERROR;
    log = Log.getInstance().setLogger(level);
    logger = Log.getInstance().getLogger();
  }

  public abstract void quit();

  public DRIVER_TYPE getDriverType() {
    return driverType;
  }

  public boolean getHeadless() {
    return headless;
  }
  
}
