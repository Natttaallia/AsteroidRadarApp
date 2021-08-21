package com.udacity.asteroidradar.ui.holders

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

/**
 * @author Kulbaka Nataly
 * @date 21.08.2021
 */
class AsteroidListViewHolder (val viewDataBinding: ItemAsteroidBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_asteroid
    }
}