package io.nightfox.plugins.capacitorsquarepayments;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import sqip.Call;

public class ChargeCall implements Call<io.nightfox.plugins.capacitorsquarepayments.ChargeResult> {

  public static class Factory {
    private final ChargeService service;
    private final Converter<ResponseBody, ChargeService.ChargeErrorResponse> errorConverter;

    public Factory(Retrofit retrofit) {
      service = retrofit.create(ChargeService.class);
      Annotation[] noAnnotations = {};
      Type errorResponseType = ChargeService.ChargeErrorResponse.class;
      errorConverter = retrofit.responseBodyConverter(errorResponseType, noAnnotations);
    }

    public Call<io.nightfox.plugins.capacitorsquarepayments.ChargeResult> create(String nonce) {
      return new io.nightfox.plugins.capacitorsquarepayments.ChargeCall(this, nonce);
    }
  }

  private final io.nightfox.plugins.capacitorsquarepayments.ChargeCall.Factory factory;
  private final String nonce;
  private final retrofit2.Call<Void> call;

  private ChargeCall(io.nightfox.plugins.capacitorsquarepayments.ChargeCall.Factory factory,
                     String nonce) {
    this.factory = factory;
    this.nonce = nonce;
    call = factory.service.charge(new ChargeService.ChargeRequest(nonce));
  }

  @Override
  public io.nightfox.plugins.capacitorsquarepayments.ChargeResult execute() {
    Response<Void> response;
    try {
      response = call.execute();
    } catch (IOException e) {
      return io.nightfox.plugins.capacitorsquarepayments.ChargeResult.networkError();
    }
    return responseToResult(response);
  }

  @Override
  public void enqueue(sqip.Callback<io.nightfox.plugins.capacitorsquarepayments.ChargeResult> callback) {
    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull Response<Void> response) {
        callback.onResult(responseToResult(response));
      }

      @Override
      public void onFailure(@NonNull retrofit2.Call<Void> call, Throwable throwable) {
        if (throwable instanceof IOException) {
          callback.onResult(io.nightfox.plugins.capacitorsquarepayments.ChargeResult.networkError());
        } else {
          throw new RuntimeException("Unexpected exception", throwable);
        }
      }
    });
  }

  private io.nightfox.plugins.capacitorsquarepayments.ChargeResult responseToResult(Response<Void> response) {
    if (response.isSuccessful()) {
      return io.nightfox.plugins.capacitorsquarepayments.ChargeResult.success();
    }
    try {
      //noinspection ConstantConditions
      ResponseBody errorBody = response.errorBody();
      ChargeService.ChargeErrorResponse errorResponse = factory.errorConverter.convert(errorBody);
      return io.nightfox.plugins.capacitorsquarepayments.ChargeResult.error(errorResponse.errorMessage);
    } catch (IOException exception) {
//      if (BuildConfig.DEBUG) {
//        Log.d("ChargeCall", "Error while parsing error response: " + response.toString(),
//            exception);
//      }
      return io.nightfox.plugins.capacitorsquarepayments.ChargeResult.networkError();
    }
  }

  @Override
  public boolean isExecuted() {
    return call.isExecuted();
  }

  @Override
  public void cancel() {
    call.cancel();
  }

  @Override
  public boolean isCanceled() {
    return call.isCanceled();
  }

  @NonNull
  @Override
  public Call<io.nightfox.plugins.capacitorsquarepayments.ChargeResult> clone() {
    return factory.create(nonce);
  }
}
