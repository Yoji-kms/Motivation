package com.yoji.motivation.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yoji.motivation.R
import com.yoji.motivation.databinding.DialogAddLinkBinding
import com.yoji.motivation.databinding.FragmentCreateOrEditBinding
import com.yoji.motivation.observers.CreateOrEditLifecycleObserver
import com.yoji.motivation.viewmodel.IdeaListViewModel
import java.io.FileOutputStream
import java.util.*

class CreateOrEditFragment : Fragment() {
    private var _binding: FragmentCreateOrEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var observer: CreateOrEditLifecycleObserver
    private val ideaViewModel: IdeaListViewModel by viewModels(ownerProducer = ::requireActivity)
    private val editingIdea by lazy { ideaViewModel.editingIdea.value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observer = CreateOrEditLifecycleObserver(requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateOrEditBinding.inflate(inflater, container, false)

        binding.apply {
            if (editingIdea?.id != 0L) {
                createOrEditToolbarId.title = getString(R.string.edit_idea)
                newContentEdtTxtViewId.setText(editingIdea?.content)
                if (editingIdea?.imageUri.toString() != "null") {
                    addingImageImgViewId.apply {
                        setImageURI(editingIdea?.imageUri)
                        imageGroupId.visibility = View.VISIBLE
                        addImageBtnId.visibility = View.GONE
                    }
                }
                if (!editingIdea?.link.isNullOrBlank()) {
                    linkTxtViewId.text = editingIdea?.link
                    linkGroupId.visibility = View.VISIBLE
                    addLinkBtnId.visibility = View.GONE
                }
                saveIdeaBtnId.isEnabled = true
            } else createOrEditToolbarId.title = getString(R.string.create_new_idea)

            createOrEditToolbarId.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_createOrEditFragment_to_ideaListFragment)
            }
            addImageBtnId.setOnClickListener {
                observer.selectImage(
                    imageView = addingImageImgViewId,
                    imageGroup = imageGroupId,
                    addBtn = addImageBtnId,
                )
            }
            addLinkBtnId.setOnClickListener {
                val dialogBinding = DialogAddLinkBinding.inflate(inflater)
                AlertDialog.Builder(requireActivity()).apply {
                    setTitle(getString(R.string.adding_link))
                    setView(dialogBinding.root)
                    val spinner = dialogBinding.schemaSpinnerId
                    ArrayAdapter.createFromResource(
                        context,
                        R.array.spinner,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }
                    setPositiveButton(getString(R.string.ok)) { _, _ ->
                        binding.linkTxtViewId.apply {
                            spinner.selectedItem.toString()
                            with(dialogBinding.addLinkEdtTxtViewId.text.toString())
                            {
                                if (this.isNotBlank()) {
                                    text =
                                        if (this.startsWith("http://")
                                            || this.startsWith("https://")
                                        ) this
                                        else spinner.selectedItem.toString() + this
                                } else Snackbar.make(
                                    binding.root,
                                    context.getString(R.string.link_is_empty),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                        linkGroupId.visibility = View.VISIBLE
                        it.visibility = View.GONE
                    }
                    setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                    show()
                }
            }
            removeLinkBtnId.setOnClickListener {
                linkTxtViewId.text = ""
                linkGroupId.visibility = View.GONE
                addLinkBtnId.visibility = View.VISIBLE
            }
            removeImageBtnId.setOnClickListener {
                addingImageImgViewId.setImageDrawable(null)
                imageGroupId.visibility = View.GONE
                addImageBtnId.visibility = View.VISIBLE
            }
            saveIdeaBtnId.setOnClickListener {
                with(ideaViewModel.editingIdea.value?.imageUri) {
                    if (this.toString() != "null"
                        && this != null
                    ) toFile().delete()
                }
                val file = if (addingImageImgViewId.isVisible)
                    requireContext().filesDir
                        .resolve(Calendar.getInstance().timeInMillis.toString() + ".jpeg")
                        .also { file ->
                            FileOutputStream(file).use {
                                viewToBitmap(addingImageImgViewId).compress(
                                    Bitmap.CompressFormat.JPEG,
                                    50,
                                    it
                                )
                            }
                        } else null
                ideaViewModel.changeContent(
                    newContent = newContentEdtTxtViewId.text.toString(),
                    newImageUri = if (file != null) Uri.fromFile(file) else Uri.parse("null"),
                    newLink = linkTxtViewId.text.toString()
                )
                observer.unregister()
                ideaViewModel.save()
                ideaViewModel.clear()
                findNavController().navigate(R.id.action_createOrEditFragment_to_ideaListFragment)
            }
            newContentEdtTxtViewId.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    saveIdeaBtnId.isEnabled = !s.isNullOrBlank()
                }

                override fun afterTextChanged(s: Editable?) = Unit
            })
        }

        return binding.root
    }

    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}