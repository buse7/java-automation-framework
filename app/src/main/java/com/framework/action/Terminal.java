package com.framework.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.slf4j.Logger;

import com.framework.exception.AdbException;
import com.framework.action.Permission;
import com.framework.listener.Log;

public class Terminal {
  public static Adb adb = new Adb();
  static Logger logger = Log.getInstance().getLogger();

  private Terminal() {}
  
  public static Terminal getInstance() {
    return Holder.instance;
  }

  private static class Holder {
    public static final Terminal instance = new Terminal();
  }

  public synchronized static String run(String command, int timeout) {
    try {
      StringBuilder result = new StringBuilder();
      Process process = Runtime.getRuntime().exec(command);
      logger.debug("Execute command, command : {}", command);

      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      String currentLine;
      while ((currentLine = input.readLine()) != null) {
        result.append(currentLine).append("\n");
      }

      process.getInputStream().close();
      process.getErrorStream().close();

      input.close();
      error.close();

      process.waitFor(timeout, TimeUnit.SECONDS);
      process.destroy();

      return result.toString();
    } catch (Exception e) {
      // TODO: handle exception
      logger.error("Command line error, Command : {}", command);
      e.printStackTrace();
      return null;
    }
  }
  
  public static class Adb {
    private static String targetDevice;
    static List<String> targetDevices;

    private static Boolean adbSetting = false;

    public Adb start() {
      run("adb -s " + getTarget() + " start-server", 10);
      return this;
    }

    public Adb kill() {
      run("adb -s " + getTarget() + " kill-server", 10);
      return this;
    }

    /**
     * @param isRecovery true : recovery mode restart
     *                   false : 단말 재실행  
     *
     */
    public Adb rebootDevice(Boolean isRecovery) {
      String command = isRecovery ? "adb - s" + getTarget() + " reboot recovery" : "adb -s " + getTarget() + " reboot";
      run(command, 10);
      return this;
    }

    public String getDeviceAndroidVersion() {
      return Objects.requireNonNull(run("adb -s " + getTarget() + " shell getprop ro.build.version release", 10))
          .replace("\n", "");
    }

    public String getDeviceSdkVersion() {
      return run("adb -s " + getTarget() + " shell getprop ro.build.version.sdk", 10);
    }

    public String getDeviceResolution() {
      return run("adb -s " + getTarget() + " shell dumpsys window | grep DisplayWidth", 10);
    }

    public String takeScreenshot(String fileName) {
      run("adb -s " + getTarget() + " shell am screencap /sdcard/" + fileName + ".png", 10);
      return "/sdcard/" + fileName + ".png";
    }

    public Adb pull(String locate) {
      run("adb -s " + getTarget() + " pull" + locate, 10);
      return this;
    }

    public void setTarget() {
      if (!checkAdbSettings()) {
        throw new AdbException("Adb 명령어를 사용할 수 없습니다. Adb Setting을 다시 한 번 확인해주세요.");
      }

      List<String> devices = Arrays.asList(Objects.requireNonNull(run("adb devices", 10)).split("\n"));

      if (devices.size() == 0) {
        throw new AdbException("Device 정보를 가져올 수 없습니다. 단말기 연결 상태를 확인해주세요.");
      }

      devices = devices.stream()
          .filter(device -> !(device.contains("unauthorized") || device.contains("List of devices attached")))
          .map(device -> device.split("\t")[0]).collect(Collectors.toList());

      try {
        targetDevice = devices.get(0);
        targetDevices = devices;
      } catch (IndexOutOfBoundsException e) {
        // TODO: handle exception
        logger.error("No connected device. Please connect the emulator or real device.");
        e.printStackTrace();
      }
    }

    public void setTarget(String id) {
      targetDevice = id;
    }

    public String getTarget() {
      if (targetDevice == null) {
        setTarget();
      }

      return targetDevice;
    }

    public List<String> getTargetList() {
      if (targetDevice == null || targetDevices.size() == 0) {
        setTarget();
      }
      return targetDevices;
    }

    public Boolean checkAdbSettings() {
      if (adbSetting || !Objects.requireNonNull(run("adb", 10)).contains("command not found")) {
        adbSetting = true;
        return true;
      }

      return false;
    }

    public Boolean isInstall(String packageName) {
      String packages = run("adb -s " + getTarget() + " shell pm list packages -f", 10);
      return Objects.requireNonNull(packages).contains(packageName);
    }

    public Boolean install(String locate) {
      String result = run("adb -s " + getTarget() + " install " + locate, 10);
      if (Objects.requireNonNull(result).contains("failed to")) {
        throw new AdbException(result);
      }
      return true;
    }

    public Boolean reInstall(String locate) {
      String result = run("adb -s " + getTarget() + " install -r " + locate, 10);
      if (Objects.requireNonNull(result).contains("failed to")) {
        throw new AdbException(result);
      }
      return true;
    }

    public Adb unInstall(String packageName) {
      if (!isInstall(packageName)) {
        return this;
      }

      run("adb -s " + getTarget() + " uninstall " + packageName, 10);
      return this;
    }

    public void launch(String targActivity) {
      run("adb -s " + getTarget() + " shell am start -n " + targActivity, 10);
    }

    public Adb killProcess(String packageName) {
      run("adb -s " + getTarget() + " shell am force-stop " + packageName, 10);
      return this;
    }

    public Adb removeData(String packageName) {
      run("adb -s " + getTarget() + " shell pm clear " + packageName, 10);
      return this;
    }

    public Adb grantPermission(String packageName, Permission permission) {
      run("adb -s " + getTarget() + " pm grant " + packageName + " " + permission.get(), 10);
      return this;
    }

    public Adb grantPermission(String packageName, String permission) {
      run("adb -s " + getTarget() + " pm grant " + packageName + " " + permission, 10);
      return this;
    }

    public Adb revokePermission(String packageName, Permission permission) {
      run("adb -s " + getTarget() + " pm revoke " + packageName + " " + permission.get(), 10);
      return this;
    }

    public Adb revokePermission(String packageName, String permission) {
      run("adb -s " + getTarget() + " pm revoke " + packageName + " " + permission, 10);
      return this;
    }

    public Adb key(KeyCode keyCode) {
      run("adb -s " + getTarget() + " shell input keyevent " + keyCode.get(), 10);
      return this;
    }

    public Adb key(int code) {
      run("adb -s " + getTarget() + " shell input keyevent " + code, 10);
      return this;
    }

    private Adb key(String keyCode) {
      run("adb -s " + getTarget() + " shell input keyevent " + keyCode, 10);
      return this;
    }

    public String getLog(String key) {
      return run("adb -s " + getTarget() + " logcat -s " + key + " -d", 10);
    }

    public Adb deleteLog() {
      run("adb -s " + getTarget() + " logcat -c", 10);
      return this;
    }

    public Adb removeRunningApp() {
      KeyCode[] keyCodes = { KeyCode.KEYCODE_HOME, KeyCode.KEYCODE_APP_SWITCH, KeyCode.KEYCODE_DPAD_DOWN,
          KeyCode.KEYCODE_DEL, KeyCode.KEYCODE_BACK };
      Arrays.stream(keyCodes)
          .forEach(keyCode -> run("adb -s " + getTarget() + " shell input keyevent " + keyCode.get(), 10));
      return this;
    }

    public Adb screen(Boolean flag) {
      try {
        if (flag) {
          run("adb -s " + getTarget() + " shell input keyevent " + KeyCode.KEYCODE_SCREEN_ON.get(), 10);
        } else {
          String currentStatus = run(
              "adb -s " + getTarget() + " shell dumpsys window policy | grep screenState=SCREEN_STATE_ | cut -c 32-",
              10);
          if (Objects.requireNonNull(currentStatus).contains("ON")) {
            run("adb -s " + getTarget() + " shell input keyevent " + KeyCode.KEYCODE_SCREEN_OFF.get(), 10);
          }
        }

        Thread.sleep(500L);
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
        ;
      }
      return this;
    }
    
    public Adb wifi(Boolean flag) {
      String command = "adb -s " + getTarget() + " shell svc wifi " + (flag ? "enable" : "disable");
      run(command, 10);
      return this;
    }

    public Adb batterySaver(Boolean flag) {
      String command = "adb -s " + getTarget() + " settings put global low_power " + (flag ? "1" : "0");
      run(command, 10);
      return this;
    }

    public Adb doze(Boolean flag) {
      if (flag) {
        run("adb -s " + getTarget() + " shell dumpsys battery unplug", 10);
        String step;
        do {
          screen(false);
          step = run("adb -s " + getTarget() + " shell dumpsys deviceidle step", 10);
        } while (!(Objects.requireNonNull(step).contains("Stepeed to deep: IDLE")
            && !step.contains("Stepped to deep: IDLE_PENDING")));
      } else {
        run("adb -s " + getTarget() + " shell dumpsys battery reset", 10);
      }
      return this;
    }
    
    public Adb rotateSettings(Boolean flag) {
      String command = "adb -s " + getTarget() + " settings put system accelerometer_rotation " + (flag ? "1" : "0");
      run(command, 10);
      return this;
    }

    public String getPhoneNumber(int info) {
      AtomicReference<String> phoneNumber = new AtomicReference<>("");
      String[] parcelArr = Terminal.run("adb -s " + adb.getTarget() + " shell service call iphonesubinfo " + info, 10)
          .split("'");
      StringBuilder data = new StringBuilder();

      for (int i = 1; i < parcelArr.length; i = i + 2) {
        data.append(parcelArr[i]);
      }

      Arrays.stream(Objects.requireNonNull(data.toString()).split("\\.")).filter(s -> s.length() == 1)
          .forEachOrdered(s -> phoneNumber.set(phoneNumber.get() + s));

      if (phoneNumber.get().startsWith("+82")) {
        phoneNumber.set(phoneNumber.get().replace("+82", "0"));
      } else if (phoneNumber.get().startsWith("82")) {
        phoneNumber.set(phoneNumber.get().replace("82", "0"));
      }

      logger.info("Device Phone Number is {}", phoneNumber.get());

      if (phoneNumber.get().length() != 11) {
        throw new AdbException("ino 정보가 잘못되어 휴대 전화 번호를 반환하지 않습니다. 다시 확인해주세요.");
      }

      return phoneNumber.get();
    }
    
    public String getAppVersion(String packageName) {
      String result = run("adb -s " + adb.getTarget() + " shell dumpsys package " + packageName + " | grep versionName",
          10);
      return Objects.requireNonNull(result).substring(result.lastIndexOf("=") + 1).trim();
    } 
  }
}
