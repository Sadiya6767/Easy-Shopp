package com.example.easyshopp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyshopp.components.ProductItemView
import com.example.easyshopp.model.ProductModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CategoryProductPage(
    modifier: Modifier = Modifier,
    categoryId: String
) {
    val productList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", categoryId)
            .get()
            .addOnSuccessListener { result ->
                productList.value =
                    result.documents.mapNotNull { it.toObject(ProductModel::class.java) }
            }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 20.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {
        items(productList.value.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { product ->
                    ProductItemView(
                        product = product,
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .padding(6.dp)
                    )
                }
            }
        }
    }
}
