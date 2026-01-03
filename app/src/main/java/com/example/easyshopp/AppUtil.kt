package com.example.easyshopp

import android.content.Context
import android.widget.Toast
import com.example.easyshopp.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID
import androidx.core.content.edit

object AppUtil {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun addToCart(context: Context, productId: String) {

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnSuccessListener {
            val currentCart =
                it.get("cartItem") as? Map<String, Long> ?: emptyMap()

            val currentQuantity = currentCart[productId] ?: 0
            val updatedQuantity = currentQuantity + 1

            val updatedCart = mapOf("cartItem.$productId" to updatedQuantity)

            userDoc.update(updatedCart)
                .addOnSuccessListener {
                    showToast(context, "Item added to the cart")
                }
                .addOnFailureListener {
                    showToast(context, "Failed adding item to the cart")
                }
        }
    }

    fun removeFromCart(
        context: Context,
        productId: String,
        removeAll: Boolean = false
    ) {

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnSuccessListener {
            val currentCart =
                it.get("cartItem") as? Map<String, Long> ?: emptyMap()

            val currentQuantity = currentCart[productId] ?: 0
            val updatedQuantity = currentQuantity - 1

            val updatedCart =
                if (updatedQuantity <= 0 || removeAll)
                    mapOf("cartItem.$productId" to FieldValue.delete())
                else
                    mapOf("cartItem.$productId" to updatedQuantity)

            userDoc.update(updatedCart)
                .addOnSuccessListener {
                    showToast(context, "Item removed from the cart")
                }
                .addOnFailureListener {
                    showToast(context, "Failed removing item from the cart")
                }
        }
    }

    fun clearCartAndAddToOrder() {

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnSuccessListener {

            val currentCart =
                it.get("cartItem") as? Map<String, Long> ?: emptyMap()

            val order = OrderModel(
                id = "ORD_" + UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .take(10)
                    .uppercase(),
                userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                date = Timestamp.now(),
                items = currentCart,
                status = "ORDERED",
                address = it.get("address") as String
            )

            Firebase.firestore.collection("orders")
                .document(order.id)
                .set(order)
                .addOnSuccessListener {
                    userDoc.update("cartItem", FieldValue.delete())
                }
        }
    }

    fun getDiscountPer(): Float = 10f

    fun getTaxPer(): Float = 12f

    fun razorpayApiKey(): String {
        return "rzp_test_5WgA34F9ljiXAX"
    }

    fun startPayment(amount: Float) {

        val checkout = Checkout()
        checkout.setKeyID(razorpayApiKey())

        val options = JSONObject()
        options.put("name", "Easy Shopp")
        options.put("description", "Order Payment")
        options.put("amount", (amount * 100).toInt())
        options.put("currency", "INR")

        // NOTE: checkout.open(activity, options)
        // Activity se call karna padega
    }

    fun formatDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        )
        return sdf.format(timestamp.toDate())
    }




    //favorite page


    private const val PREF_NAME = "favorite_pref"
    private const val KEY_FAVORITES = "favorite_list"



    fun addOrRemoveFromFavorite(context: Context, productId: String) {

        val list = getFavoriteList(context).toMutableSet()

        if (list.contains(productId)) {
            list.remove(productId)
            showToast(context, "Item removed from Favorite")
        } else {
            list.add(productId)
            showToast(context, "Item added to Favorite")
        }

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit {
            putStringSet(KEY_FAVORITES, list)
        }
    }

    fun checkFavorite(context: Context, productId: String): Boolean {
        return getFavoriteList(context).contains(productId)
    }

    fun getFavoriteList(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }




}
