package com.yoji.motivation.observers

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class CreateOrEditLifecycleObserver(private val registry: ActivityResultRegistry) :
    DefaultLifecycleObserver {
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var imageView: ImageView
    lateinit var imageGroup: Group
    lateinit var addBtn: Button
    private val key = "key"

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register(key, owner, ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageView.setImageURI(uri)
                imageGroup.visibility = View.VISIBLE
                addBtn.visibility = View.GONE
            }
        }
    }

    fun selectImage(imageView: ShapeableImageView, imageGroup: Group, addBtn: MaterialButton) {
        this.imageView = imageView
        this.imageGroup = imageGroup
        this.addBtn = addBtn
        getContent.launch("image/*")
    }

    fun unregister(){
        getContent.unregister()
    }
}