package com.income.icminventory.fragments.scan_positions

import android.os.Parcel
import com.income.icminventory.backstack.BaseRoute
import paperparcel.PaperParcel

@PaperParcel
data class ScanPositionsRoute( val tag: String = ScanPositionsRoute::javaClass.name) : BaseRoute() {

    override fun createFragment() = ScanPositionsFragment()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        PaperParcelScanPositionsRoute.writeToParcel(this, parcel, flags)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelScanPositionsRoute.CREATOR
    }
}