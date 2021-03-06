package com.income.icminventory.fragments.new_position

import android.os.Bundle
import android.os.Parcel
import com.income.icminventory.backstack.BaseRoute
import paperparcel.PaperParcel

const val NEW_ITEM_CODE = "item_code"
const val NEW_ITEM_SUPPLIER_ID= "supplier_id"
const val NEW_ITEM_ORDER_ID= "order_id"
const val NEW_ITEM_STATE = "item_state"

@PaperParcel
data class NewItemRoute(
    val code: String? = null,
    val supplierId: String? = null,
    val orderId: String? = null,
    val itemState: String? = null,
    val tag: String = NewItemRoute::javaClass.name
) : BaseRoute() {

    override fun createFragment() = NewItemFragment().apply {
        arguments = (arguments ?: Bundle()).apply {
            putString(NEW_ITEM_CODE, code)
            putString(NEW_ITEM_SUPPLIER_ID, supplierId)
            putString(NEW_ITEM_ORDER_ID, orderId)
            putString(NEW_ITEM_STATE, itemState)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        PaperParcelNewItemRoute.writeToParcel(this, parcel, flags)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelNewItemRoute.CREATOR
    }
}