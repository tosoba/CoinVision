package com.trm.coinvision.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinResponse(
  @SerialName("block_time_in_minutes") val blockTimeInMinutes: Int?,
  @SerialName("categories") val categories: List<String>?,
  @SerialName("coingecko_rank") val coingeckoRank: Int?,
  @SerialName("coingecko_score") val coingeckoScore: Double?,
  @SerialName("community_score") val communityScore: Double?,
  @SerialName("country_origin") val countryOrigin: String?,
  @SerialName("description") val description: Description?,
  @SerialName("developer_score") val developerScore: Double?,
  @SerialName("genesis_date") val genesisDate: String?,
  @SerialName("hashing_algorithm") val hashingAlgorithm: String?,
  @SerialName("id") val id: String?,
  @SerialName("image") val image: Image?,
  @SerialName("last_updated") val lastUpdated: String?,
  @SerialName("liquidity_score") val liquidityScore: Double?,
  @SerialName("market_cap_rank") val marketCapRank: Int?,
  @SerialName("market_data") val marketData: MarketData?,
  @SerialName("name") val name: String?,
  @SerialName("preview_listing") val previewListing: Boolean?,
  @SerialName("public_interest_score") val publicInterestScore: Double?,
  @SerialName("sentiment_votes_down_percentage") val sentimentVotesDownPercentage: Double?,
  @SerialName("sentiment_votes_up_percentage") val sentimentVotesUpPercentage: Double?,
  @SerialName("symbol") val symbol: String?,
  @SerialName("watchlist_portfolio_users") val watchlistPortfolioUsers: Long?
)
