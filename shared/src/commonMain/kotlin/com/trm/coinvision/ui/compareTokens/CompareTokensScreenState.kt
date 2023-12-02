package com.trm.coinvision.ui.compareTokens

import com.trm.coinvision.core.common.model.Serializable
import com.trm.coinvision.core.domain.model.MarketChartDaysPeriod

data class CompareTokensScreenState(val chartPeriod: MarketChartDaysPeriod) : Serializable
