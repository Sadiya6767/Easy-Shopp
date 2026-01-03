package com.example.easyshopp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.easyshopp.AppUtil
import com.example.easyshopp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductDetailsPage(
    modifier: Modifier = Modifier,
    productId: String
) {

    var product by remember {
        mutableStateOf(ProductModel())
    }

    var context = LocalContext.current

    LaunchedEffect(productId) {
        Firebase.firestore
            .collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener {
                it.toObject(ProductModel::class.java)?.let { result ->
                    product = result
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 IMAGES SAFE CHECK
        if (product.images.isNotEmpty()) {

            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { product.images.size }
            )

            HorizontalPager(
                state = pagerState,
                pageSpacing = 24.dp
            ) { page ->
                AsyncImage(
                    model = product.images[page],
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            DotsIndicator(
                dotCount = product.images.size,
                pagerState = pagerState,
                type = ShiftIndicatorType(
                    DotGraphic(
                        color = MaterialTheme.colorScheme.primary,
                        size = 6.dp
                    )
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
                Text(
                    text = product.actualPrice,
                    fontSize = 14.sp,
                    style = TextStyle(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = product.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* TODO */ },
                modifier = Modifier.padding(end = 3.dp)) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Add to Favorite"
                )
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            AppUtil.addToCart(context, productId)
        },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ){
            Text(text = "Add to Cart", fontSize = 16.sp)
        }


        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Product description : ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(product.othersDetails.isNotEmpty())
            Text(text=" Other Product details :",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        Spacer(modifier = Modifier.height(8.dp))


            product.othersDetails.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        text = "$key : ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = value,
                        fontSize = 16.sp
                    )
                }
            }
    }
}
