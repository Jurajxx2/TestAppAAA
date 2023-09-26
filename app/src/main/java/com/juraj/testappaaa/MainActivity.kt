package com.juraj.testappaaa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.juraj.testappaaa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val testLayoutManager = GridHorizontalLayoutManager(this, 3, 4, reverseLayout = true)
        /*testLayoutManager.spanSizeLookup = object: SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }

            override fun getSpanIndex(position: Int, spanCount: Int): Int {
                return 4
            }
        }*/
        val snapHelper = GridPagerSnapHelper(2, 5)
        val testAdapter = TestAaaAdapter()

        binding.recyclerView.apply {
            layoutManager = testLayoutManager
            adapter = testAdapter
        }

        val testList = (0..100L).map { SomeTestModel(it, "Item: $it") }
        testAdapter.setItems(testList)

        //snapHelper.attachToRecyclerView(binding.recyclerView)
    }
}