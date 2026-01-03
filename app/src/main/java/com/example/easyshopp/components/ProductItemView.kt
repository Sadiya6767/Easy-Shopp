package com.example.easyshopp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.easyshopp.AppUtil
import com.example.easyshopp.GlobalNavigation
import com.example.easyshopp.model.ProductModel

@Composable
fun ProductItemView(
    product: ProductModel,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var isFav by remember {
        mutableStateOf(AppUtil.checkFavorite(context, product.id))
    }

    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                GlobalNavigation.navController
                    .navigate("product-details/${product.id}")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )

            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = product.actualPrice,
                        fontSize = 12.sp,
                        style = TextStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )

                    Text(
                        text = product.price,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(onClick = {
                        AppUtil.addOrRemoveFromFavorite(context, product.id)
                        isFav = AppUtil.checkFavorite(context, product.id)
                    }) {
                        Icon(
                            imageVector =
                                if (isFav)
                                    Icons.Default.Favorite
                                else
                                    Icons.Default.FavoriteBorder,
                            tint = if (isFav) Color.Red else Color.Gray,
                            contentDescription = "Favorite"
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        AppUtil.addToCart(context, product.id)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        tint = Color(0xFF4B2E83)
                    )
                }
            }
        }
    }
}
