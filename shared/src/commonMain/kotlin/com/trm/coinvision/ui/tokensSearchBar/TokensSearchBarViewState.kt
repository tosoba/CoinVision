package com.trm.coinvision.ui.tokensSearchBar

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.trm.coinvision.core.domain.model.SelectedToken
import com.trm.coinvision.core.domain.model.TokenListItemDTO

@Stable
internal class TokensSearchBarViewState {
  var query by mutableStateOf("")
    private set

  var selectedToken by mutableStateOf(SelectedToken(id = "", symbol = "", name = "", image = null))
    private set

  var active by mutableStateOf(false)
    private set

  var isLoading by mutableStateOf(true)
    private set

  internal fun updateSelectedToken(token: SelectedToken) {
    query = token.name
    selectedToken = token
    isLoading = false
  }

  internal fun updateQuery(query: String) {
    this.query = query
  }

  internal fun updateActive(active: Boolean) {
    this.active = active
  }

  internal fun updateSelectedTokenFromSearch(token: TokenListItemDTO) {
    query = token.name
    selectedToken = SelectedToken(token.id, token.symbol, token.name, token.image)
    active = false
  }
}
