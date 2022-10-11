package com.bsc_bandarlampungsportcenter.session;

import com.bsc_bandarlampungsportcenter.rest_api.RetroServer;

public class User {

  private static String userId, isAdmin;

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

}
