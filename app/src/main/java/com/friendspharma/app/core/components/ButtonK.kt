package com.friendspharma.app.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.theme.GrayExtraLight
import com.friendspharma.app.core.theme.Primary

@Composable
fun ButtonK(
    text: Int,
    textColor: Color = Primary,
    backGroundColor: Color = BackGroundDark,
    borderColor: Color = Color.Transparent,
    height: Int = 45,
    isLoading: Boolean = false,
    isValid: Boolean = true,
    prefixIcon: Int? = null,
    suffixIcon: Int? = null,
    fontWeight: FontWeight = FontWeight.W500,
    onTap: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(
                color = if (isValid && !isLoading) backGroundColor else GrayExtraLight,
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                onTap()
            }
            .height(height.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (prefixIcon != null)
                Image(
                    painter = painterResource(id = prefixIcon), contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = text),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = fontWeight,
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (suffixIcon != null)
                Image(painter = painterResource(id = suffixIcon), contentDescription = null)
        }
    }
}