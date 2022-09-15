package com.yoji.motivation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yoji.motivation.R
import com.yoji.motivation.adapter.AuthorAdapter
import com.yoji.motivation.databinding.DialogChangeAuthorBinding
import com.yoji.motivation.dto.Author
import com.yoji.motivation.fragments.LoginFragment.Companion.AUTHOR_ID
import com.yoji.motivation.listeners.OnAuthorClickListener
import com.yoji.motivation.utils.DataStoreUtils
import com.yoji.motivation.viewmodel.AuthorListViewModel
import com.yoji.motivation.viewmodel.IdeaListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangeAuthorDialogFragment(
    private val ideaListViewModel: IdeaListViewModel
) : DialogFragment() {

    private var _binding: DialogChangeAuthorBinding? = null
    private val binding get() = _binding!!

    private val authorListViewModel by viewModels<AuthorListViewModel>(ownerProducer = ::requireActivity)

    private val authorAdapter by lazy {
        ideaListViewModel.currentAuthor.value?.let {
            AuthorAdapter(
                object : OnAuthorClickListener {
                    override fun onAuthor(author: Author) {
                        ideaListViewModel.setAuthor(author.id)

                        lifecycleScope.launch {
                            DataStoreUtils.setValue(AUTHOR_ID, author.id)
                        }

                        dialog.dismiss()
                    }

                    override suspend fun onRemove(author: Author) {
                        authorListViewModel.remove(author)
                    }
                },
                currentAuthorId = it.id
            )
        }
    }

    private val dialog: AlertDialog by lazy {
        requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.choose_author))
                setView(binding.root)
            }
            builder.create()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangeAuthorBinding.inflate(layoutInflater)

        binding.authorListRecyclerViewId.adapter = authorAdapter

        authorListViewModel.data.observe(this) {
            authorAdapter?.submitData(lifecycle, it)
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}