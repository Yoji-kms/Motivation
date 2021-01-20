package com.yoji.motivation.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.yoji.motivation.R
import com.yoji.motivation.application.App
import com.yoji.motivation.databinding.ItemIdeaBinding
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.listeners.OnIdeaClickListener
import java.text.SimpleDateFormat
import java.util.*

class IdeaViewHolder(
    private val binding: ItemIdeaBinding,
    private val onIdeaClickListener: OnIdeaClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(idea: Idea) {
        binding.apply {
            root.setOnLongClickListener { view ->
                PopupMenu(App.appContext(), view).apply {
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.delete_idea -> {
                                onIdeaClickListener.onDelete(idea)
                                true
                            }
                            R.id.edit_idea -> {
                                onIdeaClickListener.onEdit(idea)
                                true
                            }
                            else -> false
                        }
                    }
                    inflate(R.menu.context_menu)
                    show()
                }
                true
            }
            root.setOnClickListener {
                onIdeaClickListener.onLink(idea)
            }
            authorTxtViewId.text = idea.author
            authorTxtViewId.setOnClickListener { onIdeaClickListener.onAuthor(idea) }
            dateTxtViewId.text = idea.published.toFormattedString()
            contentTxtViewId.text = idea.content
            imgImgViewId.apply {
                with(idea.imageUri) {
                    visibility = if (this.toString() != "null") {
                        setImageURI(this)
                        View.VISIBLE
                    } else View.GONE
                }
            }
            linkTxtViewId.apply {
                with(idea.link) {
                    text = this
                    visibility = if (this.isBlank()) View.GONE else View.VISIBLE
                }
            }
            likeBtnId.setOnClickListener { onIdeaClickListener.onLike(idea) }
            dislikeBtnId.setOnClickListener { onIdeaClickListener.onDislike(idea) }
            shareBtnId.setOnClickListener { onIdeaClickListener.onShare(idea) }
            likesCounterTxtViewId.text = idea.likesCounter.toFormattedString()
        }
    }

    private fun Int.toFormattedString() = when (this) {
        in 0..999 -> this.toString()
        in 1_000..9_999 -> this.roundToThousandsWithOneDecimal().toString() + "K"
        in 10_000..999_999 -> (this / 1_000).toString() + "K"
        in 1_000_000..9_999_999 -> (this / 1_000).roundToThousandsWithOneDecimal().toString() + "M"
        in 10_000_000..999_999_999 -> (this / 1_000_000).toString() + "M"
        in 1_000_000_000..Int.MAX_VALUE -> (this / 1_000_000).roundToThousandsWithOneDecimal()
            .toString() + "B"
        in 0 downTo -999 -> this.toString()
        in -1_000 downTo -9_999 -> this.roundToThousandsWithOneDecimal().toString() + "K"
        in -10_000 downTo -999_999 -> (this / 1_000).toString() + "K"
        in -1_000_000 downTo -9_999_999 -> (this / 1_000).roundToThousandsWithOneDecimal().toString() + "M"
        in -10_000_000 downTo -999_999_999 -> (this / 1_000_000).toString() + "M"
        in -1_000_000_000 downTo Int.MIN_VALUE -> (this / 1_000_000).roundToThousandsWithOneDecimal()
            .toString() + "B"
        else -> "0"
    }

    private fun Int.roundToThousandsWithOneDecimal(): Double = (this / 100).toDouble() / 10


    @SuppressLint("SimpleDateFormat")
    private fun Date.toFormattedString() = SimpleDateFormat("dd MMM yyyy")
        .format(this)
        .toString()
}
