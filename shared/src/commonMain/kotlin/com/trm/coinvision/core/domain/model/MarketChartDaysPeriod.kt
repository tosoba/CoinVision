package com.trm.coinvision.core.domain.model

enum class MarketChartDaysPeriod(val queryParam: String) {
  DAY("1"),
  WEEK("7"),
  MONTH("30"),
  THREE_MONTHS("90"),
  MAX("max");

  companion object {
    val default: MarketChartDaysPeriod
      get() = DAY
  }
}
