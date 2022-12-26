package com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.R
import com.pdf.pdfreader.pdfviewer.pdfeditor.imagetopdf.pdfconverter.jpgtopdf.pdfreaderfree.repository.FilesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class InAppPurchase(context: Context) : PurchasesUpdatedListener, KoinComponent{
    private val mContext: Context = context
    private lateinit var appBilling:String
     private val filesRepository:FilesRepository by inject()
    private var billingClient: BillingClient
    var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val billingPref: SharedPreferences.Editor = context.getSharedPreferences("billingPref", Context.MODE_PRIVATE).edit()
                billingPref.putBoolean("purchased", true)
                billingPref.apply()
                showMessage("Item Purchased")
                (mContext as Activity).recreate()
            }
        }
    init {
        billingClient = BillingClient.newBuilder(mContext).enablePendingPurchases().setListener(this).build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchase = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                    val queryPurchases = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    } else {
                        val billingPref: SharedPreferences.Editor = context.getSharedPreferences("billingPref", Context.MODE_PRIVATE).edit()
                        billingPref.putBoolean("purchased", false)
                        billingPref.apply()
                    }
                }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }
    fun productPurchase() {
        if (billingClient.isReady) {
            initiatePurchase()
        } else {
            billingClient =
                BillingClient.newBuilder(mContext).enablePendingPurchases().setListener(this).build()
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        showMessage("Error" + billingResult.debugMessage)
                    }
                }
                override fun onBillingServiceDisconnected() {}
            })
        }
    }
    private fun initiatePurchase() {
        val skuList: MutableList<String> = ArrayList()
      skuList.add(mContext.resources.getString(R.string.product_id))
       //skuList.add("android.test.purchased")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient.launchBillingFlow(mContext as Activity, flowParams)
                } else {
                    showMessage("Purchase Item not Found")
                }
            } else {
                showMessage(" Error " + billingResult.debugMessage)
            }
        }
    }
    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (mContext.getString(R.string.product_id) == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
                } else {
                    var billingPrefGet = mContext.getSharedPreferences("billingPref", Context.MODE_PRIVATE)
                    if (!billingPrefGet.getBoolean("purchased", false)) {
                        val billingPref: SharedPreferences.Editor = mContext.getSharedPreferences("billingPref", Context.MODE_PRIVATE).edit()
                        billingPref.putBoolean("purchased", true)
                        billingPref.apply()
                        showMessage("Item Purchased")
                        (mContext as Activity).recreate()
                    }
                }
            } else if (mContext.getString(R.string.product_id) == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                showMessage("Purchase is Pending. Please complete Transaction")
            } else if (mContext.getString(R.string.product_id) == purchase.skus[0] && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                val billingPref: SharedPreferences.Editor = mContext.getSharedPreferences("billingPref", Context.MODE_PRIVATE).edit()
                billingPref.putBoolean("purchased", false)
                billingPref.apply()
                showMessage("Purchase Status Unknown")
            }
        }
    }
    private fun showMessage(message: String) {
        (mContext as Activity).runOnUiThread{
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult =
                billingClient.queryPurchases(BillingClient.SkuType.INAPP)
            val alreadyPurchases = queryAlreadyPurchasesResult.purchasesList
            alreadyPurchases?.let { handlePurchases(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            showMessage("Purchase Canceled")
        } else {
            showMessage("Error updated" + billingResult.debugMessage)
        }
    }
}