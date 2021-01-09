package com.bro.pagingpicker.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bro.pagingpicker.BR
import com.bro.pagingpicker.databinding.ItemGalleryImageBinding
import com.bro.pagingpicker.model.gallery.GalleryItem

/**
 * Created by kyunghoon on 2020-12-14
 */
class GalleryAdapter(val galleryEventListener: GalleryEventListener) : PagedListAdapter<GalleryItem, ImageViewHolder>(MainDiff) {
    // memo. super(Diff) 방법

    companion object {
        const val COLUMN_COUNT = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.eventListener = galleryEventListener
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ImageViewHolder(private val binding: ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryItem: GalleryItem?) {
        binding.setVariable(BR.galleryItem, galleryItem)
        binding.executePendingBindings()
    }
}

// memo. object. static instance. MainDiff()로 접근하면 안됨. just MainDiff
// object 는 아래와 같다.
//  public final class ExampleObject {
//   public static final ExampleObject INSTANCE = new ExampleObject();
object MainDiff : DiffUtil.ItemCallback<GalleryItem>() {
    override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        return oldItem.getId() == newItem.getId()
    }

    override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
        // TODO 데이터 내용 늘어나면 변경해줘야함
        return oldItem.getFilePath().contentEquals(newItem.getFilePath())
    }
}