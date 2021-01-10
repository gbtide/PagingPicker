package com.bro.pagingpicker.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bro.pagingpicker.BR
import com.bro.pagingpicker.databinding.ItemGalleryImageBinding
import com.bro.pagingpicker.databinding.ItemGalleryVideoBinding
import com.bro.pagingpicker.model.gallery.GalleryItem
import com.bro.pagingpicker.model.gallery.GalleryType
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.model.gallery.Video

/**
 * Created by kyunghoon on 2020-12-14
 */
class GalleryAdapter(private val galleryEventListener: GalleryEventListener) :
    PagedListAdapter<GalleryItem, GalleryItemViewHolder>(MainDiff) {

    companion object {
        const val COLUMN_COUNT = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder {
        return when (GalleryType.find(viewType)) {
            GalleryType.VIDEO -> {
                VideoItemViewHolder(ItemGalleryVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                    .apply {
                        eventListener = galleryEventListener
                    })
            }
            else -> {
                ImageItemViewHolder(ItemGalleryImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                    .apply {
                        eventListener = galleryEventListener
                    })
            }
        }
    }

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.getType()?.value ?: -1
    }

}

open abstract class GalleryItemViewHolder(
    @NonNull itemView: View
) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(galleryItem: GalleryItem?)
}

class ImageItemViewHolder(private val binding: ItemGalleryImageBinding) :
    GalleryItemViewHolder(binding.root) {

    override fun bind(galleryItem: GalleryItem?) {
        galleryItem?.let {
            binding.setVariable(BR.item, it as Image)
            binding.executePendingBindings()
        }
    }
}

class VideoItemViewHolder(private val binding: ItemGalleryVideoBinding) :
    GalleryItemViewHolder(binding.root) {

    override fun bind(galleryItem: GalleryItem?) {
        galleryItem?.let {
            binding.setVariable(BR.item, it as Video)
            binding.executePendingBindings()
        }
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