package com.friendspharma.app.features.presentation.home.comonents

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friendspharma.app.core.theme.Primary

@Composable
fun BoxSwitch(isBox: Boolean, switch: () -> Unit) {
    Row(
        modifier = Modifier
            .border(1.dp, Primary, RoundedCornerShape(20.dp))
            .padding(3.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { switch() }
            .width(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isBox)
            Text(
                text = "Leaf", modifier = Modifier.padding(horizontal = 2.dp),
                fontSize = 16.sp, fontWeight = FontWeight.W500, color = Primary
            )
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Primary
        )
        if (isBox)
            Text(
                text = "Box", modifier = Modifier.padding(horizontal = 2.dp),
                fontSize = 16.sp, fontWeight = FontWeight.W500, color = Primary
            )
    }
}