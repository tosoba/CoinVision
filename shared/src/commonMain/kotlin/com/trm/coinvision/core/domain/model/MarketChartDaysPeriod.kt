package com.trm.coinvision.core.domain.model

enum class MarketChartDaysPeriod(val queryParam: String, val label: String) {
  DAY("1", "1D"),
  WEEK("7", "7D"),
  MONTH("30", "1M"),
  MONTHS_3("90", "3M"),
  YEAR("365", "1Y");

  companion object {
    val default: MarketChartDaysPeriod
      get() = DAY
  }
}
