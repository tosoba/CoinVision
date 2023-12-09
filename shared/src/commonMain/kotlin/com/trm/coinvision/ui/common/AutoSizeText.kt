package com.trm.coinvision.ui.common

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SingleLineAutoSizeText(
  modifier: Modifier = Modifier,
  text: String = "",
  style: TextStyle = LocalTextStyle.current
) {
  var multiplier by remember { mutableStateOf(1f) }
  Text(
    modifier = modifier,
    text = text,
    maxLines = 1,
    overflow = TextOverflow.Visible,
    style = style.copy(fontSize = style.fontSize * multiplier),
    onTextLayout = { if (it.hasVisualOverflow) multiplier *= 0.99f }
  )
}
