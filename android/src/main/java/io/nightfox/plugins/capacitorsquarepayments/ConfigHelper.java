package io.nightfox.plugins.capacitorsquarepayments;

import android.util.Log;

//import com.squareup.moshi.Moshi;

import java.util.UUID;

import retrofit2.Retrofit;
//import retrofit2.converter.moshi.MoshiConverterFactory;

public class ConfigHelper {

  public static final String GOOGLE_PAY_MERCHANT_ID = "REPLACE_ME";
  private static final String CHARGE_SERVER_HOST = "REPLACE_ME";
  private static final String CHARGE_SERVER_URL = "https://" + CHARGE_SERVER_HOST + "/";

  public static boolean serverHostSet() {
    return !CHARGE_SERVER_HOST.equals("REPLACE_ME");
  }

  public static boolean merchantIdSet() {
    return !GOOGLE_PAY_MERCHANT_ID.equals("REPLACE_ME");
  }

  public static Retrofit createRetrofitInstance() {
    return new Retrofit
            .Builder()
            .baseUrl(io.nightfox.plugins.capacitorsquarepayments.ConfigHelper.CHARGE_SERVER_URL)
            .build();
  }
}
