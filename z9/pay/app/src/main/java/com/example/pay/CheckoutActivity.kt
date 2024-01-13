package com.example.pay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.paymentsheet.PaymentSheet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheetResult
import android.util.Log

val stripe_key = "pk_test_51OXi1KEbgHp6w50CbfbIR9Kxv2vkIVI3quFOTlqAcaTWL1BUxBI1HYj7AvmUXNUi9ZEcqPZTyov5caxZ6ATeXEJb006fZm28H8"

class CheckoutActivity : AppCompatActivity() {
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        "$url/stripe/$amount".httpPost().responseJson { _, _, result ->
            Log.i("mytag","result: "+result)
            if(result is Result.Success) {
                val responseJson = result.get().obj()
                paymentIntentClientSecret = responseJson.getString("paymentIntent")
                customerConfig = PaymentSheet.CustomerConfiguration(
                    responseJson.getString("customer"),
                    responseJson.getString("ephemeralKey")
                )
                val publishableKey = responseJson.getString("publishableKey")
                PaymentConfiguration.init(this, publishableKey)
                presentPaymentSheet()
            }
        }
    }
    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = merchName,
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business handles
                // delayed notification payment methods like US bank accounts.
                allowsDelayedPaymentMethods = true
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.i("mytag","Canceled")
            }
            is PaymentSheetResult.Failed -> {
                Log.i("mytag","Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                Log.i("mytag","Completed")
                MainActivity.c!!.deleteAt(position)
                finish()
            }
        }
    }
}