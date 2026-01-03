package com.example.easyshopp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshopp.AppUtil
import com.example.easyshopp.GlobalNavigation
import com.example.easyshopp.model.ProductModel
import com.example.easyshopp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun CheckoutPage(modifier: Modifier = Modifier) {

    val userModel = remember { mutableStateOf<UserModel?>(null) }
    val productList = remember { mutableStateListOf<ProductModel>() }

    val subTotal = remember { mutableStateOf(0f) }
    val discount = remember { mutableStateOf(0f) }
    val tax = remember { mutableStateOf(0f) }
    val total = remember { mutableStateOf(0f) }

    fun calculateAndAssign() {
        subTotal.value = 0f   // 🔥 IMPORTANT reset

        productList.forEach { product ->
            val qty = userModel.value?.cartItem?.get(product.id) ?: 0
            val price = product.actualPrice.toFloatOrNull() ?: 0f
            subTotal.value += price * qty
        }

        discount.value = subTotal.value * AppUtil.getDiscountPer() / 100
        tax.value = subTotal.value * AppUtil.getTaxPer() / 100
        total.value = subTotal.value - discount.value + tax.value
    }

    LaunchedEffect(Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return@LaunchedEffect   //no crash app

        Firebase.firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                val user = doc.toObject(UserModel::class.java) ?: return@addOnSuccessListener
                userModel.value = user

                if (user.cartItem.isEmpty()) return@addOnSuccessListener

                Firebase.firestore.collection("data")
                    .document("stock")
                    .collection("products")
                    .whereIn("id", user.cartItem.keys.toList())
                    .get()
                    .addOnSuccessListener { snapshot ->
                        productList.clear()
                        productList.addAll(snapshot.toObjects(ProductModel::class.java))
                        calculateAndAssign()
                    }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(18.dp)
    ) {

    Text("Checkout", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Deliver to :", fontWeight = FontWeight.SemiBold)
        Text(userModel.value?.name ?: "")
        Text(userModel.value?.address ?: "")

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        RowCheckoutItem("Subtotal", "₹${subTotal.value}")
        Spacer(modifier = Modifier.height(8.dp))

        RowCheckoutItem("Discount (-)", "₹${discount.value}")
        Spacer(modifier = Modifier.height(8.dp))

        RowCheckoutItem("Tax (+)", "₹${tax.value}")
        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "To Pay",
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "₹${"%.2f".format(total.value)}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                AppUtil.startPayment(total.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Pay Now",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }


    }
}

@Composable
fun RowCheckoutItem(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = 18.sp)
    }
}
