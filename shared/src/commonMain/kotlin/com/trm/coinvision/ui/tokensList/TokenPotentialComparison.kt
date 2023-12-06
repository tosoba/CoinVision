package com.trm.coinvision.ui.tokensList

import com.trm.coinvision.core.domain.model.TokenListItemDTO

internal data class TokenPotentialComparison(
  val referenceToken: TokenListItemDTO,
  val potential: TokenPotential?,
)
