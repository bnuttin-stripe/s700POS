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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s700pos.R

@Composable
fun FormattedPriceLabel(amount: Double, modifier: Modifier = Modifier): String {
    val price = amount / 100
    return "$%.2f".format(price)
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
    modifier: Modifier
){
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
//        if (status == "loading") {
//            CircularProgressIndicator(
//                color = Color.White,
//                strokeWidth = 2.dp,
//                modifier = Modifier.size(ButtonDefaults.IconSize)
//            )
//        } else {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(start = 0.dp, end = 0.dp)
//            ) {
//                Icon(
//                    painterResource(icon),
//                    contentDescription = label,
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(28.dp)
//                        .padding(end = 8.dp)
//                )
//                Text(label)
//            }
//        }
    }
}

@Composable
fun PrettyIcon(
    onClick: () -> Unit,
    status: String,
    icon: Int,
    label: String,
    modifier: Modifier
){
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
    modifier: Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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