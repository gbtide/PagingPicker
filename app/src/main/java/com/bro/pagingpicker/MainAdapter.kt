package com.bro.pagingpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bro.pagingpicker.databinding.ItemMainBinding
import com.bro.pagingpicker.model.gallery.Image

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainAdapter() : PagedListAdapter<Image, MainViewHolder>(MainDiff) {
    // memo. super(Diff) 방법

    companion object {
        const val COLUMN_COUNT = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class MainViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image?) {
        binding.setVariable(BR.image, image)
        binding.executePendingBindings()
    }
}

// memo. object. static instance. MainDiff()로 접근하면 안됨. just MainDiff
// object 는 아래와 같다.
//  public final class ExampleObject {
//   public static final ExampleObject INSTANCE = new ExampleObject();
object MainDiff : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        // TODO 데이터 내용 늘어나면 변경해줘야함
        return oldItem.data.contentEquals(newItem.data)
    }
}