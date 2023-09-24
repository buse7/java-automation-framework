package com.framework.action;

public enum KeyCode {
  KEYCODE_0("KEYCODE_0", "0 클릭"),
  KEYCODE_1("KEYCODE_1", "1 클릭"),
  KEYCODE_2("KEYCODE_2", "2 클릭"),
  KEYCODE_3("KEYCODE_3", "3 클릭"),
  KEYCODE_4("KEYCODE_4", "4 클릭"),
  KEYCODE_5("KEYCODE_5", "5 클릭"),
  KEYCODE_6("KEYCODE_6", "6 클릭"),
  KEYCODE_7("KEYCODE_7", "7 클릭"),
  KEYCODE_8("KEYCODE_8", "8 클릭"),
  KEYCODE_9("KEYCODE_9", "9 클릭"),
  KEYCODE_A("KEYCODE_A", "A 클릭"),
  KEYCODE_B("KEYCODE_B", "B 클릭"),
  KEYCODE_C("KEYCODE_C", "C 클릭"),
  KEYCODE_D("KEYCODE_D", "D 클릭"),
  KEYCODE_E("KEYCODE_E", "E 클릭"),
  KEYCODE_F("KEYCODE_F", "F 클릭"),
  KEYCODE_G("KEYCODE_G", "G 클릭"),
  KEYCODE_H("KEYCODE_H", "H 클릭"),
  KEYCODE_I("KEYCODE_I", "I 클릭"),
  KEYCODE_J("KEYCODE_J", "J 클릭"),
  KEYCODE_K("KEYCODE_K", "K 클릭"),
  KEYCODE_L("KEYCODE_L", "L 클릭"),
  KEYCODE_M("KEYCODE_M", "M 클릭"),
  KEYCODE_N("KEYCODE_N", "N 클릭"),
  KEYCODE_O("KEYCODE_O", "O 클릭"),
  KEYCODE_P("KEYCODE_P", "P 클릭"),
  KEYCODE_Q("KEYCODE_Q", "Q 클릭"),
  KEYCODE_R("KEYCODE_R", "R 클릭"),
  KEYCODE_S("KEYCODE_S", "S 클릭"),
  KEYCODE_T("KEYCODE_T", "T 클릭"),
  KEYCODE_U("KEYCODE_U", "U 클릭"),
  KEYCODE_V("KEYCODE_V", "V 클릭"),
  KEYCODE_W("KEYCODE_W", "W 클릭"),
  KEYCODE_X("KEYCODE_X", "X 클릭"),
  KEYCODE_Y("KEYCODE_Y", "Y 클릭"),
  KEYCODE_Z("KEYCODE_Z", "Z 클릭"),
  KEYCODE_APP_SWITCH("KEYCODE_APP_SWITCH", "사용한 앱 버튼 클릭"),
  KEYCODE_BACK("KEYCODE_BACK", "뒤로가기 버튼 클릭"),
  KEYCODE_HOME("KEYCODE_HOME", "홈 버튼 클릭"),
  KEYCODE_BRIGHTNESS_DOWN("KEYCODE_BRIGHTNESS_DOWN", "밝기 줄이기"),
  KEYCODE_BRIGHTNESS_UP("KEYCODE_BRIGHTNESS_UP", "밝기 올리기"),
  KEYCODE_ENTER("KEYCODE_ENTER", "Enter 클릭"),
  KEYCODE_SLEEP("KEYCODE_SLEEP", "Screen off"),
  KEYCODE_WAKEUP("KEYCODE_WAKEUP", "Screen on"),
  KEYCODE_TOGGLE("KEYCODE_TOGGLE", "Screen on/off toggle"),
  KEYCODE_DPAD_DOWN("KEYCODE_DPAD_DOWN", "사용중인 앱 선택"),
  KEYCODE_DEL("DEL", "사용중인 앱 삭제"),
  KEYCODE_SCREEN_ON("KEYCODE_WAKEUP", "Screen on"),
  KEYCODE_SCREEN_OFF("KEYCODE_POWER", "Screen off");

  private final String code;

  KeyCode(String code, String description) { this.code = code; }

  public String get() {
    return code;
  }
}
