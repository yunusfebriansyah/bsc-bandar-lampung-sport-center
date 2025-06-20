package com.bsc_bandarlampungsportcenter.session;

public class User {

  private static String userId, isAdmin;
  private static boolean isDeniedYesterday = false;

  public static boolean getIsDeniedYesterday() {
    return isDeniedYesterday;
  }

  public static void setIsDeniedYesterday(boolean isDeniedYesterday) {
    User.isDeniedYesterday = isDeniedYesterday;
  }

  public static String getIsAdmin() {
    return isAdmin;
  }

  public static void setIsAdmin(String isAdmin) {
    User.isAdmin = isAdmin;
  }

  public static String getUserId() {
    return userId;
  }

  public static void setUserId(String userId) {
    User.userId = userId;
  }

  public static boolean isAdmin() {
    return isAdmin.equalsIgnoreCase("1");
  }

}
