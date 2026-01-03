package com.example.easyshopp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.easyshopp.AppUtil
import com.example.easyshopp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CartItemView(
    modifier: Modifier = Modifier,
    productId: String,
    qty: Long
) {

    var product by remember {
        mutableStateOf(ProductModel())
    }

    LaunchedEffect(productId) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener {
                val result = it.toObject(ProductModel::class.java)
                if (result != null) {
                    product = result
                }
            }
    }

    val context = LocalContext.current

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.title,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {

                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.actualPrice,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.LineThrough
                )

                // ✅ FIXED QUANTITY ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {
                            AppUtil.removeFromCart(context, productId)
                        }
                    ) {
                        Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = qty.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    IconButton(
                        onClick = {
                            AppUtil.addToCart(context, productId)
                        }
                    ) {
                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            IconButton(
                onClick = {
                    AppUtil.removeFromCart(context, productId, removeAll = true)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from cart"
                )
            }
        }
    }
}
