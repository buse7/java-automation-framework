package com.framework.action;

public enum Permission {
  INTERNET("android.permission.internet"),
  READ_EXTERNAL("android.permission.read_external_storage"),
  WRITE_EXTERNAL("android.permission.write_external_storage"),
  WAKE_LOCK("android.permission.wake_lock"),
  COARSE_LOCATION("android.permission.ACCESS_COARSE_LOCATION"),
  FINE_LOCATION("android.permission.ACCESS_FINE_LOCATION"),
  BACKGROUND_LOCATION("android.permission.ACCESS_BACKGROUND_LOCATION"),
  READ_PTHONE_STATE("android.permission.READ_PHONE_STATE"),
  READ_SMS("android.permission.READ_SMS"),
  RECEVE_SMS("android.permission.RECEIVE_SMS"),
  ACTION_CALL("android.permission.ACTION_CALL");

  private final String permission;
  
  Permission(String permission) { this.permission = permission; }

  public String get() { return permission; }  
}
