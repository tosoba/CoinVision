package com.trm.coinvision.core.domain.model

enum class MarketChartDaysPeriod(val queryParam: String, val label: String) {
  DAY("1", "1D"),
  WEEK("7", "7D"),
  MONTH("30", "30D"),
  DAYS_200("200", "200D"),
  MAX("max", "MAX");

  companion object {
    val default: MarketChartDaysPeriod
      get() = DAY
  }
}
