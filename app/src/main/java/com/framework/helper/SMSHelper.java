package com.framework.helper;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hc.core5.util.TextUtils;

import com.framework.util.File;
import com.framework.util.PropertyHandler;
import com.framework.action.Terminal;
import com.framework.exception.AdbException;
import com.framework.exception.SMSHelperException;

import static com.framework.action.Terminal.adb;

public class SMSHelper {
  private final String packageName = 'com.smshelper';
  private final File file = new File();

  public SMSHelper() {
    if (!adb.checkAdbSettings()) {
      throw new AdbException("Adb 명령어를 사용할 수 없습니다. Adb Setting을 다시 한번 확인해주세요.");
    }
  }

  private Boolean checkSmsHelper() {
    return adb.isInstall(packageName);
  }

  public SMSHelper installSmsHelper() {
    if (!checkSmsHelper()) {
      adb.install(getApkLocate());
    }
    return this;
  }

  public SMSHelper updatSmsHelper() {
    Boolean result;

    if (checkSmsHelper()) {
      result = adb.reInstall(getApkLocate());
    } else {
      result = adb.install(getApkLocate());
    }

    if (result) {
      file.remove("SMSHelper_0.0.3.apk");
    }

    return this;
  }

  public SMSHelper acceptPermission() {
    Terminal.run("adb -s " + adb.getTarget() + " shell cmd notification allow_listener" + packageName + "/"
        + packageName + ".NotificationCollectorService", 10);
    return this;
  }

  public String getNotification() {
    return adb.getLog("notification");
  }

  public String getAuthNumber() {
    String authNumber = null;
    String logs = getNotification();

    if (!(Objects.requireNonNull(logs).contains("[인증 번호]") || logs.contains("본인확인 인증번호를 입력하세요! [KG모빌리언스]"))) {
      throw new SMSHelperException("인증 문자가 발송되지 않았습니다. 문자 전송 여부를 확인하세요.");
    }

    Pattern pattern = Pattern.compile("\\[(\\d{6})]");
    Matcher matcher = pattern.matcher(logs);

    while (matcher.find()) {
      authNumber = matcher.group(1);
    }

    if (TextUtils.isEmpty(authNumber)) {
      throw new SMSHelperException("인증 문자에 인증 번호가 없습니다. 전송 문자 내용을 확인하세요.");
    }

    return authNumber;
  }

  public String getTemporaryPassword() {
    String temporaryPassword = null;
    String logs = getNotification();

    if (!(Objects.requireNonNull(logs).contains("임시 비밀 번호 발급"))) {
      throw new SMSHelperException("임시 비밀번호 안내 문자가 수신되지 않았습니다. 문자 전송 여부를 확인하세요.");
    }

    Pattern pattern = Pattern.compile("\\[(\\d{6}[a-zA-Z][^A-ZA-Z0-9])]");
    Matcher matcher = pattern.matcher(logs);

    while (matcher.find()) {
      temporaryPassword = matcher.group(1);
    }

    if (TextUtils.isEmpty(temporaryPassword)) {
      throw new SMSHelperException("임시 비밀번호 문자에 비밀번호 정보가 없습니다. 전송 문자 내용을 확인하세요.");
    }

    return temporaryPassword;
  }

  public String getApkLocate() {
    try {
      return file.download(PropertyHandler.getProperties("downloadUrl"), PropertyHandler.getProperties("fileName"));
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }
  }

  public SMSHelper deleteLog() {
    adb.deleteLog();
    return this;
  }
  
}
