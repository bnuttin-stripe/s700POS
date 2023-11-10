package com.bnuttin.s700pos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s700pos.R
import java.text.DateFormat
import java.util.Date

@Composable
fun FormattedPriceLabel(amount: Double, modifier: Modifier = Modifier): String {
    val price = amount / 100
    return "$%.2f".format(price)
}

@Composable
fun FormattedDate(date: Long, modifier: Modifier = Modifier): String {
    if (date == 0L) return "";
    val dt = Date(date * 1000)
    return DateFormat.getDateInstance().format(dt);
}

@Composable
fun LoadingImage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorImage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.width(64.dp),
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = "Connection Error",
        )
    }
}

@Composable
fun PrettyButton(
    onClick: () -> Unit,
    status: String,
    icon: Int,
    label: String,
    modifier: Modifier,
) {
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(size = 6.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        modifier = modifier
            .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
            .defaultMinSize(1.dp)
    ) {
        when (status) {
            "done" -> Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 0.dp, end = 0.dp)
            ) {
                Icon(
                    painterResource(icon),
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                        .graphicsLayer {
                            rotationX =
                                if (icon == R.drawable.baseline_subdirectory_arrow_left_24) 180f else 0f
                        }
                )
                Text(label)
            }

            "loading" -> CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )

            "error" -> Icon(
                painterResource(R.drawable.baseline_cloud_off_24),
                contentDescription = "Error",
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun PrettyIcon(
    onClick: () -> Unit,
    status: String,
    icon: Int,
    label: String,
    modifier: Modifier,
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = modifier
    ) {
        when (status) {
            "done" -> Icon(
                painterResource(icon),
                contentDescription = label,
                tint = Color.DarkGray
            )

            "loading" -> CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )

            "error" -> Icon(
                painterResource(R.drawable.baseline_cloud_off_24),
                contentDescription = "Error",
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun TopRow(
    title: String,
    onClick: () -> Unit,
    status: String,
    icon: Int,
    label: String,
    modifier: Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 10.dp)
    ) {
        Text(
            title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(weight = 1f))
        PrettyIcon(
            onClick = onClick,
            status = status,
            icon = icon,
            label = label,
            modifier = Modifier
        )
    }
}

@Composable
fun PaymentMethod(brand: String, wallet: String?) {
    Row(){
        Image(
            painterResource(
                when (brand) {
                    "amex" -> R.drawable.amex
                    "diners" -> R.drawable.diners
                    "discover" -> R.drawable.discover
                    "jcb" -> R.drawable.jcb
                    "mastercard" -> R.drawable.mastercard
                    "unionpay" -> R.drawable.cup
                    "visa" -> R.drawable.visa
                    "unknown" -> R.drawable.credit_card
                    else -> {
                        R.drawable.credit_card
                    }
                }
            ),
            contentDescription = "Search",
            modifier = Modifier
                .size(24.dp)
        )
        if (wallet == "apple_pay"){
            Image(
                painterResource(
                    R.drawable.applepay
                ),
                contentDescription = "Search",
                modifier = Modifier
                    .size(28.dp)
                    .padding(start = 4.dp)
            )
        }
        if (wallet == "google_pay"){
            Image(
                painterResource(
                    R.drawable.gpay
                ),
                contentDescription = "Search",
                modifier = Modifier
                    .size(28.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}