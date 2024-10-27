package com.example.cryptotracker.crypto.presentation.coin_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotracker.R
import com.example.cryptotracker.ui.dimension.LocalDimensions
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    formattedPrice: String,
    icon: ImageVector,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val defaultTextStyle: TextStyle = LocalTextStyle.current.copy(
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = contentColor
    )
    val dimensions = LocalDimensions.current
    Card(
        modifier = modifier
            .padding(dimensions.dimenSmall)
            .shadow(
                elevation = 15.dp,
                shape = RectangleShape,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        shape = RectangleShape,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = contentColor
        )
    ) {
        AnimatedContent(
            targetState = icon,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            label = "IconAnimation"
        ) { icon ->
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(75.dp)
                    .padding(dimensions.dimenMedium),
                tint = contentColor
            )
        }
        Spacer(modifier = Modifier.height(dimensions.dimenSmall))
        AnimatedContent(
            targetState = formattedPrice,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            label = "FormattedPriceAnimation"
        ) { price ->
            Text(
                text = price,
                style = defaultTextStyle,
                modifier = Modifier
                    .padding(horizontal = dimensions.dimenMedium)
            )
        }
        Spacer(modifier = Modifier.height(dimensions.dimenSmall))
        Text(
            text = title,
            style = defaultTextStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = dimensions.dimenMedium)
                .padding(bottom = dimensions.dimenMedium),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = contentColor
        )
    }
}

@PreviewLightDark
@Composable
fun CoinInfoCardPreview() {
    CryptoTrackerTheme {
        CoinInfoCard(
            title = "Price",
            formattedPrice = "16,345.66",
            icon = ImageVector.vectorResource(R.drawable.dollar)
        )
    }
}
