package com.juraj.testappaaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val layoutManager = GridHorizontalLayoutManager(this, 2, 5, reverseLayout = true)
        val snapHelper = GridPagerSnapHelper(2, 5)

        recyclerView.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(recyclerView)
    }
}