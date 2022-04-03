package com.example.myapplication

import ImageLoader
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

//адаптер, осуществляющий слайдовую навигацию между разделами "Добавить", "Словарь", "Базис"
class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                ImageLoader()
            }
            1 ->{
                Dict()
            }
            2->{
                Basis()
            }
            else ->{
                Fragment()
            }
        }
    }
}