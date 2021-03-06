package com.income.icminventory.fragments.abstraction

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.income.icminventory.R
import com.income.icminventory.activities.MainActivity
import com.income.icminventory.database.dto.Item_Table
import com.income.icminventory.fragments.FragmentType
import com.income.icminventory.fragments.adapter.IOnReloadAdapterListener
import com.income.icminventory.fragments.adapter.ItemAdapter
import com.income.icminventory.fragments.adapter.ItemSwipeHelper
import com.income.icminventory.fragments.adapter.SearchEngine
import com.income.icminventory.fragments.adapter.TypesFactoryImpl
import com.income.icminventory.fragments.adapter.viewmodel.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.concurrent.TimeUnit

abstract class FragmentSearch : FragmentBase(), IOnReloadAdapterListener {

    private lateinit var searchEngine: SearchEngine
    private lateinit var disposable: Disposable
    private lateinit var _adapter: ItemAdapter

    abstract fun loadItemViewModels(): MutableList<ViewModel>
    abstract fun getFragmenType(): FragmentType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun reload() {
        loadAdapter()
    }

    override fun onStart() {
        super.onStart()
        initAdapter()
    }

    fun filter(predicate: (ViewModel) -> Boolean) {
        val adapterItems = _adapter.allItems
        val filteredItems = adapterItems.filter { predicate(it) }.toMutableList()
        _adapter.setListItem(filteredItems)
    }


    fun loadAdapter() {
        rv_items.layoutManager = LinearLayoutManager(activity.baseContext)
        val itemAdapter = ItemAdapter(loadItemViewModels(), TypesFactoryImpl(), this)
        rv_items.adapter = itemAdapter
        val itemTouchHelper = ItemTouchHelper(ItemSwipeHelper(itemAdapter, activity, this))
        itemTouchHelper.attachToRecyclerView(rv_items)
        tvSumItems.text = dbContext.items.where(Item_Table.oldLocation.eq((activity as MainActivity).currentLocation)).queryList().filter { it.endNumber > 0 }.sumByDouble { item -> item.endNumber }.toString()
        itemAdapter.notifyDataSetChanged()
    }

    fun loadAllData(items: MutableList<ViewModel>) {
        _adapter.setListItem(items)
    }

    private fun initAdapter() {
        val viewModels = loadItemViewModels()
        rv_items.layoutManager = LinearLayoutManager(activity.baseContext)
        _adapter = ItemAdapter(viewModels.toMutableList(), TypesFactoryImpl(), this)
        rv_items.adapter = _adapter
        searchEngine = SearchEngine()
        if (getFragmenType() == FragmentType.ScannedListFragment) {
            val itemTouchHelper = ItemTouchHelper(ItemSwipeHelper(_adapter, activity, this))
            itemTouchHelper.attachToRecyclerView(rv_items)
            tvSumItems.text = dbContext.items.where(Item_Table.oldLocation.eq((activity as MainActivity).currentLocation)).queryList().filter { it.endNumber > 0 }.sumByDouble { item -> item.endNumber }.toString()
            _adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()

        val textSearchObservable = createTextChangeObseravable()
        if (userVisibleHint) {
            etSearch.text.clear()
            disposable = textSearchObservable.observeOn(AndroidSchedulers.mainThread())
                .doOnNext {

                    showProgressBar()
                }.observeOn(Schedulers.io())
                .map<MutableList<ViewModel>> { s -> searchEngine.search(s, _adapter.items) }.observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    hideProgressBar()
                    showResult(list)
                }
        }
    }

    private fun createTextChangeObseravable(): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String> { emitter ->
            val textWatcher = object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }

                override fun afterTextChanged(s: Editable) {}
            }
            etSearch.addTextChangedListener(textWatcher)
            emitter.setCancellable { etSearch.removeTextChangedListener(textWatcher) }

        }).debounce(500, TimeUnit.MILLISECONDS)
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint)
            if (!disposable.isDisposed)
                disposable.dispose()
    }

    private fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    private fun showResult(result: MutableList<ViewModel>) {
        if (result.isEmpty()) {
            Toast.makeText(activity, getString(R.string.not_match), Toast.LENGTH_SHORT).show()
            _adapter.setListItem(emptyList<ViewModel>().toMutableList())
        } else {
            _adapter.setListItem(result)
        }
    }
}