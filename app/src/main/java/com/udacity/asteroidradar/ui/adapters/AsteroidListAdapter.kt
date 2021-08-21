package com.udacity.asteroidradar.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import com.udacity.asteroidradar.ui.holders.AsteroidListViewHolder
import com.udacity.asteroidradar.ui.listeners.AsteroidClickListener

/**
 * @author Kulbaka Nataly
 * @date 21.08.2021
 */
class AsteroidListAdapter(val listener: AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListViewHolder {
        val withDataBinding: ItemAsteroidBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidListViewHolder.LAYOUT,
            parent,
            false
        )
        return AsteroidListViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AsteroidListViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.data = getItem(position)
            it.listener = listener
        }
    }

     companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

}