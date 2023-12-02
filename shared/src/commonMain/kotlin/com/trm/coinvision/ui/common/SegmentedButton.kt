package com.trm.coinvision.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T : Any> SegmentedButton(
  modifier: Modifier = Modifier,
  items: List<T> = emptyList(),
  selectedItem: T? = null,
  label: (T) -> String = { it.toString() },
  onItemClick: (T) -> Unit = {}
) {
  Row(modifier = modifier) {
    val cornerRadius = 16.dp
    val colorScheme = MaterialTheme.colorScheme
    val selectedColor =
      ButtonDefaults.outlinedButtonColors(containerColor = colorScheme.primary.copy(alpha = 0.1f))
    val nonSelectedColor = ButtonDefaults.outlinedButtonColors(containerColor = colorScheme.surface)

    items.forEachIndexed { index, item ->
      OutlinedButton(
        onClick = { onItemClick(item) },
        contentPadding = PaddingValues(0.dp),
        shape =
          when (index) {
            0 -> {
              RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = 0.dp,
                bottomStart = cornerRadius,
                bottomEnd = 0.dp
              )
            }
            items.lastIndex -> {
              RoundedCornerShape(
                topStart = 0.dp,
                topEnd = cornerRadius,
                bottomStart = 0.dp,
                bottomEnd = cornerRadius
              )
            }
            else -> {
              RoundedCornerShape(0.dp)
            }
          },
        border =
          BorderStroke(
            1.dp,
            if (item == selectedItem) {
              colorScheme.primary
            } else {
              colorScheme.primary.copy(alpha = 0.75f)
            }
          ),
        colors =
          if (item == selectedItem) {
            selectedColor
          } else {
            nonSelectedColor
          }
      ) {
        Text(label(item))
      }
    }
  }
}
