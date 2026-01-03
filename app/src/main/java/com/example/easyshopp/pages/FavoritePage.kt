package com.example.easyshopp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopp.AppUtil
import com.example.easyshopp.components.ProductItemView
import com.example.easyshopp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun FavoritePage(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    var productList by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {

        val favoriteList = AppUtil.getFavoriteList(context)

        if (favoriteList.isEmpty()) {
            productList = emptyList()
        } else {
            Firebase.firestore.collection("data")
                .document("stock")
                .collection("products")
                .whereIn("id", favoriteList.toList())
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val resultList = querySnapshot.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                    productList = resultList
                }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(18.dp)
    ) {

        Text(
            text = "Your Favorites",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (productList.isNotEmpty()) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(productList.chunked(2)) { rowItems ->
                    Row {
                        rowItems.forEach { product ->
                            ProductItemView(
                                product = product,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

        } else {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Items Here", fontSize = 32.sp)
            }
        }
    }
}
