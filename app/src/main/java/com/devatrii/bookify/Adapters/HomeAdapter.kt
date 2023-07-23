package com.devatrii.bookify.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnFlingListener
import com.devatrii.bookify.Models.BooksModel
import com.devatrii.bookify.Models.HomeModel
import com.devatrii.bookify.Utils.SpringScrollHelper
import com.devatrii.bookify.Utils.loadOnline
import com.devatrii.bookify.Views.Activities.ActivityCategory
import com.devatrii.bookify.Views.Activities.DetailsActivity
import com.devatrii.bookify.databinding.ItemBodBinding
import com.devatrii.bookify.databinding.ItemHomeBinding
import kotlin.math.abs
import kotlin.math.sign

const val LAYOUT_HOME = 0
const val LAYOUT_BOD = 1

class HomeAdapter(val list: ArrayList<HomeModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HomeItemViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        val mViewPool = RecyclerView.RecycledViewPool()
        fun bind(model: HomeModel, context: Context) {
            binding.apply {
                mRvChildCat.setupChildRv(model.booksList!!)
                mCatTitle.text = model.catTitle
                mBtnSeeAll.setOnClickListener {
                    // handle here
                    Intent().apply {
                        putExtra("book_list",model.booksList)
                        putExtra("cat_title",model.catTitle)
                        setClass(context, ActivityCategory::class.java)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            mRvChildCat,
                            mRvChildCat.transitionName
                        )
                        context.startActivity(this, options.toBundle())
                    }
                }
            }
        }

        private fun RecyclerView.setupChildRv(list: ArrayList<BooksModel>) {
            val adapter = HomeChildAdapter(list, this.context)
            this.adapter = adapter
            val linearLayoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            linearLayoutManager.isItemPrefetchEnabled = true
            layoutManager = linearLayoutManager

            setRecycledViewPool(mViewPool)
            SpringScrollHelper().apply {
                attachToRecyclerView(this@setupChildRv)
            }
        }
    }

    class BODItemViewHolder(val binding: ItemBodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: HomeModel, context: Context) {
            binding.apply {
                model.bod?.apply {
                    mBookImage.loadOnline(image)
                    mReadTodayBook.setOnClickListener {
                        // handle on click later
                        Intent().apply {
                            putExtra("book_model", model.bod)
                            setClass(context, DetailsActivity::class.java)
                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as Activity,
                                mBookImage,
                                mBookImage.transitionName
                            )
                            context.startActivity(this, options.toBundle())
                        }
                    }
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            LAYOUT_HOME -> {
                HomeItemViewHolder(
                    ItemHomeBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                BODItemViewHolder(
                    ItemBodBinding.inflate(LayoutInflater.from(context), parent, false)
                )
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].LAYOUT_TYPE) {
            LAYOUT_HOME -> LAYOUT_HOME
            else -> LAYOUT_BOD
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].LAYOUT_TYPE) {
            LAYOUT_HOME -> {
                (holder as HomeItemViewHolder).bind(list[position], context)
            }

            else -> {
                (holder as BODItemViewHolder).bind(list[position], context)
            }
        }
    }


}