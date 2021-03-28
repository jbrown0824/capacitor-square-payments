package io.nightfox.plugins.capacitorsquarepayments;

public final class ChargeResult {

  public static io.nightfox.plugins.capacitorsquarepayments.ChargeResult success() {
    return new io.nightfox.plugins.capacitorsquarepayments.ChargeResult(true, false, null);
  }

  public static io.nightfox.plugins.capacitorsquarepayments.ChargeResult error(String message) {
    return new io.nightfox.plugins.capacitorsquarepayments.ChargeResult(false, false, message);
  }

  public static io.nightfox.plugins.capacitorsquarepayments.ChargeResult networkError() {
    return new io.nightfox.plugins.capacitorsquarepayments.ChargeResult(false, true, null);
  }

  public final boolean success;
  public final boolean networkError;

  /**
   * Set if {@link #success} is false and {@link #networkError} is false.
   */
  public final String errorMessage;

  private ChargeResult(boolean success, boolean networkError, String errorMessage) {
    this.success = success;
    this.networkError = networkError;
    this.errorMessage = errorMessage;
  }
}
