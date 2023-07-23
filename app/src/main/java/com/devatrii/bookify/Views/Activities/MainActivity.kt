package com.devatrii.bookify.Views.Activities

import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.devatrii.bookify.Adapters.HomeAdapter
import com.devatrii.bookify.Models.HomeModel
import com.devatrii.bookify.Repository.BookRepo
import com.devatrii.bookify.Repository.MainRepo
import com.devatrii.bookify.Utils.FirebaseResponse
import com.devatrii.bookify.Utils.removeViewAnim
import com.devatrii.bookify.Utils.showViewAnim
import com.devatrii.bookify.ViewModels.BookViewModel
import com.devatrii.bookify.ViewModels.BookViewModelFactory
import com.devatrii.bookify.ViewModels.MainViewModel
import com.devatrii.bookify.ViewModels.MainViewModelFactory
import com.devatrii.bookify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    val activity = this
    var list: ArrayList<HomeModel> = ArrayList()
    val adapter = HomeAdapter(list, activity)

    private val mainRepo by lazy {
        MainRepo(activity)
    }
    private val viewModel by lazy {
        ViewModelProvider(activity, MainViewModelFactory(mainRepo))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            mRecyclerViewHome.adapter = adapter
            mRecyclerViewHome.onScroll()
            handleBackend()

            mError.mTryAgain.setOnClickListener {
                viewModel.getHomeData()
            }


        }

    }

    private fun handleBackend() {
        viewModel.getHomeData()
        viewModel.homeLiveData.observe(activity) {
            Log.d(TAG, "handleBackend: $it")
            when (it) {
                is FirebaseResponse.Error -> {
                    // handle error
                    binding.mLoader.removeViewAnim()
                    binding.mErrorHolder.showViewAnim()
                }

                is FirebaseResponse.Loading -> {
                    // handle loading
                    binding.mLoader.showViewAnim()
                    binding.mErrorHolder.removeViewAnim()
                }

                is FirebaseResponse.Success -> {
                    // handle success
                    binding.mLoader.removeViewAnim()
                    binding.mErrorHolder.removeViewAnim()
                    list.clear()
                    it.data?.forEach {
                        list.add(it)
                        adapter.notifyItemChanged(list.size)
                    }
                }
            }
        }
    }

    private fun RecyclerView.onScroll() {
        val state = IntArray(1)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state[0] = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && (state[0] == 0 || state[0] == 2)) {
                    supportActionBar?.hide()
                } else if (dy < -10) {
                    supportActionBar?.show()
                }
            }
        })
    }
}