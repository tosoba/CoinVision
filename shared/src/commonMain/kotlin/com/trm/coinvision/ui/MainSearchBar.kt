package com.trm.coinvision.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TokensSearchBar(
  modifier: Modifier = Modifier,
  initialQuery: String = "",
  onQueryChange: (String) -> Unit = {}
) {
  var query by rememberSaveable { mutableStateOf(initialQuery) }
  var active by rememberSaveable { mutableStateOf(false) }

  DockedSearchBar(
    modifier = modifier,
    query = query,
    onQueryChange = {
      query = it
      onQueryChange(it)
    },
    onSearch = { active = false },
    active = active,
    onActiveChange = { active = it },
    placeholder = { Text("Hinted search text") },
    leadingIcon = {
      IconButton({ active = !active }) {
        if (active) {
          Icon(Icons.Rounded.ArrowBack, contentDescription = null)
        } else {
          Icon(Icons.Rounded.Search, contentDescription = null)
        }
      }
    },
    trailingIcon = { Icon(Icons.Rounded.MoreVert, contentDescription = null) },
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      items(15) { idx ->
        val resultText = "Suggestion $idx"
        ListItem(
          modifier =
            Modifier.clickable {
              query = resultText
              onQueryChange(resultText)
              active = false
            },
          headlineContent = {
            Text(text = resultText, style = MaterialTheme.typography.titleMedium)
          },
          supportingContent = {
            Text(text = "Additional info", style = MaterialTheme.typography.titleMedium)
          },
          leadingContent = { Icon(Icons.Rounded.Star, contentDescription = null) },
        )
      }
    }
  }
}
