package com.income.icminwentaryzacja.fragments.positions_list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.income.icminwentaryzacja.R
import com.income.icminwentaryzacja.database.DBContext
import com.income.icminwentaryzacja.fragments.FragmentType
import com.income.icminwentaryzacja.fragments.abstraction.FragmentBase
import com.income.icminwentaryzacja.fragments.positions_list.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.fragment_empty_list.rv_items
import javax.inject.Inject

class ScannedListFragment : FragmentBase() {

    @Inject
    lateinit var databaseContext: DBContext

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_scan_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        rv_items.layoutManager = LinearLayoutManager(activity.baseContext)
        rv_items.adapter = ItemAdapter(databaseContext.items.queryList().map { ItemViewModel(it) }, TypesFactoryImpl(), FragmentType.ScannedListFragment)
    }
}


