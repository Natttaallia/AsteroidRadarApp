package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.ui.adapters.AsteroidListAdapter
import com.udacity.asteroidradar.ui.listeners.AsteroidClickListener

class MainFragment : Fragment() {

    private var mAdapter: AsteroidListAdapter? = null

    private val mViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = mViewModel

        mAdapter = AsteroidListAdapter(AsteroidClickListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = mAdapter

        mViewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroids.apply {
                mAdapter?.submitList(asteroids)
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> mViewModel.onViewWeekAsteroidsClicked()
            R.id.show_today_menu -> mViewModel.onViewTodayAsteroidsClicked()
            R.id.show_saved_menu -> mViewModel.onViewAllAsteroidsClicked()
        }
        return true
    }
}
