package com.juraj.testappaaa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juraj.testappaaa.databinding.ItemTestAaaBinding

class TestAaaAdapter: RecyclerView.Adapter<TestAaaAdapter.TestAaaViewHolder>() {

    private val items = mutableListOf<SomeTestModel>()

    fun setItems(newItems: List<SomeTestModel>) {
        val diffResult = DiffUtil.calculateDiff(TestItemsDiffUtil(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestAaaAdapter.TestAaaViewHolder {
        //val binding = ItemTestAaaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val binding = DataBindingUtil.inflate<ItemTestAaaBinding>(LayoutInflater.from(parent.context), R.layout.item_test_aaa, parent, false)
        return TestAaaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestAaaAdapter.TestAaaViewHolder, position: Int) {
        val binding = holder.binding
        val item = items[position]
        binding.model = item
        binding.executePendingBindings()
    }

    inner class TestAaaViewHolder(val binding: ItemTestAaaBinding): RecyclerView.ViewHolder(binding.root)

    class TestItemsDiffUtil(private val oldList: List<SomeTestModel>, private val newList: List<SomeTestModel>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id
        }

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.title == newItem.title
        }
    }
}