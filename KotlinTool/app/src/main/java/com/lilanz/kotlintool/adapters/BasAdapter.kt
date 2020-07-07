package com.lilanz.kotlintool.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lilanz.kotlintool.R
import com.lilanz.kotlintool.beans.Bean

class BasAdapter: RecyclerView.Adapter<BasAdapter.Holer> {
    private var context: Context? = null
    private var beanList: List<Bean>? = null


    // TODO 1.修改对象
    constructor(context: Context, list: List<Bean>?): super(){
        this.context = context
        this.beanList = list
        if (beanList == null) {
            beanList = ArrayList<Bean>()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holer { // TODO 2.修改布局
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_main, viewGroup, false)
        return Holer(view)
    }

    override fun onBindViewHolder(pianShuHolder: Holer, i: Int) { // TODO 3.设置界面数据
        val bean: Bean = beanList!![i]
    }

    override fun getItemCount(): Int {
        return beanList!!.size
    }

    inner class Holer(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var listener: OnItemClickListener? = null
    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(bean: Bean?)
    }

}