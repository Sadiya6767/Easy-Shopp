package com.example.easyshopp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopp.components.OrderView
import com.example.easyshopp.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


@Composable
fun OrderPage(modifier: Modifier = Modifier) {

    var orderList by remember {
        mutableStateOf<List<OrderModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("orders")
            .whereEqualTo(
                "userId",
                FirebaseAuth.getInstance().currentUser?.uid
            )
            .get()
            .addOnSuccessListener { querySnapshot ->
                val resultList = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)
                }
                orderList = resultList
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(18.dp)
    ) {

        Text(
            text = "Your Orders",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orderList) { order ->
                OrderView(order)
            }
        }

    }
}
