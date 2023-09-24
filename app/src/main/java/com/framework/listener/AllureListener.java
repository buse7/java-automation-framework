package com.framework.listener;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import io.qameta.allure.Allure;


public class AllureListener implements TestLifecycleListener {
  private ATUTestRecorder recorder;
  private String fileName;
  private String filePath;
  private String loggerName;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  DateFormat yearFormat = new SimpleDateFormat("yyyy");
  DateFormat dateFormat = new SimpleDateFormat("MM-dd");
  DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
  Date date = new Date();

  @Override
  public void beforeTestStart(TestResult result) {
    MDC.put("tid", getTestMethodName(result));
    logger.info("{} test is starting.", getTestMethodName(result));
    loggerName = getTestMethodName(result) + "_" + dateFormat.format(date) + ".log";

    if (System.getProperty("headless") != null && !Boolean.parseBoolean(System.getProperty("headless"))) {
      this.startRecording(result);
    } else if (System.getProperty("headless") == null) {
      this.startRecording(result);
    }
  }

  @Override
  public void beforeTestStop(TestResult result) {
    if (result.getStatus() == Status.PASSED) {
      logger.info("{} test is succeed.", getTestMethodName(result));
      if (System.getProperty("headless") != null && !Boolean.parseBoolean(System.getProperty("headless"))) {
        this.stopRecording(result);
      } else if (System.getProperty("headless") == null) {
        this.stopRecording(result);
      }
    } else if (result.getStatus() == Status.FAILED || result.getStatus() == Status.BROKEN) {
      logger.info("{} test is failed.", getTestMethodName(result));
      if (System.getProperty("headless") != null && !Boolean.parseBoolean(System.getProperty("headless"))) {
        this.stopRecording(result);
      } else if (System.getProperty("headless") == null) {
        this.stopRecording(result);
      }
    }
    attachTestLog(loggerName);
  }

  @Override
  public void afterTestStop(TestResult result) {
    MDC.clear();
  }
  
  public void converterMovToMp4(String path) {
    File source = new File(path + ".mov");
    File target = new File(path + ".mp4");

    if (source.exists()) {
      try {
        // Audio Attrivute set
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("limp3lame");
        audio.setBitRate(800000);
        audio.setChannels(1);
        audio.setSamplingRate(44100);

        // Vidio Attribute set
        VideoAttributes video = new VideoAttributes();
        video.setCodec("libx264");
        video.setBitRate(3200000);
        video.setFrameRate(15);

        // Encoder Attribute set
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();

        // convert mov to mp4
        encoder.encode(new MultimediaObject(source), target, attrs);
      } catch (EncoderException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    } else {
      logger.error("비디오 원본 파일이 경로에 존재하지 않음");
    }
  }
  
  public void removeFile(String path) {
    File file = new File(path);
    if (file.exists()) {
      if (file.delete()) {
        logger.debug("{} - 파일 삭제 완료", file.getName());
      } else {
        logger.error("{} - 파일 삭제 실패", file.getName());
      }
    } else {
      logger.error("{} - 삭제할 파일이 경로에 존재하지 않음", file.getName());
    }
  }
  
  public static String getTestMethodName(TestResult result) {
    return result.getFullName().substring(result.getFullName().lastIndexOf(".") + 1);
  }

  public void startRecording(TestResult result) {
    try {
      File folder = new File(System.getProperty("user.dir") + "/video/" + dateFormat.format(date));
      if (!folder.exists()) {
        folder.mkdirs();
      }

      fileName = getTestMethodName(result) + "_" + timeFormat.format(date);
      filePath = System.getProperty("user.dir") + "/video/" + dateFormat.format(date) + "/" + fileName;
      // recorder init
      recorder = new ATUTestRecorder("." + "/video/" + dateFormat.format(date), fileName, false);
      // recorder start
      recorder.start();
    } catch (ATUTestRecorderException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
      // TODO: handle exception
    }
  }

  public void stopRecording(TestResult result) {
    try {
      if (result.getStatus() == Status.PASSED) {
        // recorder stop
        recorder.stop();
        // remove success test video
        removeFile(filePath + ".mov");
      } else if (result.getStatus() == Status.FAILED || result.getStatus() == Status.BROKEN) {
        // recorder stop
        recorder.stop();
        // Attach video
        attachTestExecuteVideo(filePath);
      }
    } catch (ATUTestRecorderException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
      // TODO: handle exception
    }
  }

  public void attachTestExecuteVideo(String path) {
    try {
      // mov -> mp4
      converterMovToMp4(path);
      // mov delete
      removeFile(filePath + ".mov");
      File source = new File(path + ".mp4");
      if (source.exists()) {
        byte[] byteArr = IOUtils.toByteArray(new FileInputStream(path + ".mp4"));
        Allure.addAttachment("Video of failed step", "video/mp4", new ByteArrayInputStream(byteArr), "mp4");
      } else {
        logger.error("테스트 녹화 파일 없음");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
      // TODO: handle exception
    }
  }

  public void attachTestScreenshot(WebDriver driver) {
    Allure.addAttachment("Screenshot of failed step",
        new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
  }
  
  public void attachTestLog(String logFile) {
    try {
      File file = new File(System.getProperty("user.dir") + "/logs" + yearFormat.format(date) + "/"
          + dateFormat.format(date) + "/" + logFile);
      if (file.exists()) {
        String content = Files.readString(Paths.get(System.getProperty("user.dir") + "/logs/" + yearFormat.format(date)
            + "/" + dateFormat.format(date) + "/" + logFile));
        Allure.addAttachment("Console Log", "text/plain", content);
        removeFile(System.getProperty("user.dir") + "/logs/" + yearFormat.format(date) + "/" + dateFormat.format(date) + "/" + logFile);
      } else {
        logger.error("테스트 로그 파일이 존재하지 않음");
      }
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
