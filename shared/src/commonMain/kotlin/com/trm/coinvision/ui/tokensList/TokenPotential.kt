package com.trm.coinvision.ui.tokensList

import com.trm.coinvision.core.domain.model.TokenDTO

internal data class TokenPotential(
  val token: TokenDTO,
  val potentialPriceFormatted: String,
  val potentialUpsideFormatted: String?
)
