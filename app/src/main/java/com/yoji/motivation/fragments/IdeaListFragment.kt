package com.yoji.motivation.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.motivation.R
import com.yoji.motivation.adapter.IdeaAdapter
import com.yoji.motivation.application.App
import com.yoji.motivation.databinding.DialogEditNameBinding
import com.yoji.motivation.viewmodel.IdeaListViewModel
import com.yoji.motivation.databinding.FragmentIdeaListBinding
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor
import com.yoji.motivation.fragments.LoginFragment.Companion.AUTHOR_ID
import com.yoji.motivation.listeners.OnIdeaClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdeaListFragment : Fragment() {

    private var _binding: FragmentIdeaListBinding? = null
    private val binding get() = _binding!!
    private val ideaListViewModel by viewModels<IdeaListViewModel>(ownerProducer = ::requireActivity)

    private val authorKey = "saved_filter"

    companion object {
        val prefs: SharedPreferences =
            App.appContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    private val ideaAdapter by lazy {
        IdeaAdapter(object : OnIdeaClickListener {

            override fun onLike(idea: Idea) = ideaListViewModel.likeById(idea.id)

            override fun onDislike(idea: Idea) = ideaListViewModel.dislikeById(idea.id)

            override fun onShare(ideaWithAuthor: IdeaWithAuthor) =
                ideaListViewModel.share(ideaWithAuthor, requireContext())

            override fun onLink(idea: Idea) = ideaListViewModel.link(idea, requireContext())

            override fun onAuthor(ideaWithAuthor: IdeaWithAuthor) {
                with(ideaListViewModel) {
                    if (!isFiltered()) {
                        setAuthorFilter(ideaWithAuthor.idea.authorId)
                        binding.ideaListToolbarId.apply {
                            visibility = View.VISIBLE
                            title = getString(R.string.author_filter, ideaWithAuthor.authorName)
                        }
                        with(prefs.edit()) {
                            putString(authorKey, ideaWithAuthor.authorName)
                            apply()
                        }
                    }
                }
            }

            override fun onDelete(idea: Idea) {
                with(idea.imageUri) { if (toString() != "null") toFile().delete() }
                ideaListViewModel.removeById(idea.id)
            }

            override fun onEdit(idea: Idea) {
                val bundle = bundleOf("editingIdeaId" to idea.id)
                findNavController().navigate(
                    R.id.action_ideaListFragment_to_createOrEditFragment,
                    bundle
                )
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdeaListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.ideaListViewId.adapter = ideaAdapter

        binding.createIdeaFabId.setOnClickListener {
            val bundle = bundleOf("currentAuthorId" to ideaListViewModel.currentAuthor.value?.id)
            findNavController().navigate(
                R.id.action_ideaListFragment_to_createOrEditFragment,
                bundle
            )
        }

        ideaListViewModel.data.observe(viewLifecycleOwner) {
            ideaAdapter.submitData(lifecycle, it)
        }

        binding.ideaListToolbarId.apply {
            setNavigationOnClickListener {
                with(ideaListViewModel) {
                    if (isFiltered()) {
                        clearAuthorFilter()
                        this@apply.visibility = View.GONE
                    }
                }
            }
            if (ideaListViewModel.isFiltered()) {
                title = getString(R.string.author_filter, prefs.getString(authorKey, ""))
                visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    with(ideaListViewModel) {
                        if (isFiltered()) {
                            clearAuthorFilter()
                            binding.ideaListToolbarId.visibility = View.GONE
                        }
                    }
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authorId = prefs.getLong(AUTHOR_ID, 0)
        val navController = findNavController()

        if (authorId == 0L) navController.navigate(R.id.loginFragment)
        else {
            ideaListViewModel.setAuthor(authorId)
            binding.authorNameToolbarId.apply {
                ideaListViewModel.currentAuthor.observe(viewLifecycleOwner, {
                    title = getString(R.string.user_name, it.name)
                })
                also { it.menu.clear() }.inflateMenu(R.menu.author_toolbar_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit_author -> {
                            val dialogBinding = DialogEditNameBinding.inflate(layoutInflater)
                            AlertDialog.Builder(requireActivity()).apply {
                                setTitle(getString(R.string.change_user_name))
                                setView(dialogBinding.root)
                                setPositiveButton(getString(R.string.ok)) { _, _ ->
                                    ideaListViewModel.changeAuthorName(
                                        authorId,
                                        dialogBinding.editNameEdtTxtViewId.editText?.text.toString()
                                            .trim()
                                    )
                                }
                                setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                                show()
                            }
//                            val dialog: AlertDialog = requireActivity().let {
//                                val builder = AlertDialog.Builder(it)
//                                builder.apply {
//                                    setTitle(getString(R.string.change_user_name))
//                                    setView(dialogBinding.root)
//                                    setPositiveButton(getString(R.string.ok)) { _, _ ->
//                                        ideaListViewModel.changeAuthorName(
//                                            authorId,
//                                            dialogBinding.editNameEdtTxtViewId.text.toString()
//                                                .trim()
//                                        )
//                                        isEnabled = false
//                                    }
//                                    setNegativeButton(getString(R.string.cancel)) { _, _ -> }
//                                }
//                                builder.create()
//                            }
//                            dialogBinding.editNameEdtTxtViewId.addTextChangedListener(
//                                object : TextWatcher {
//                                    override fun beforeTextChanged(
//                                        s: CharSequence?,
//                                        start: Int,
//                                        count: Int,
//                                        after: Int
//                                    ) = Unit
//
//                                    override fun onTextChanged(
//                                        s: CharSequence?,
//                                        start: Int,
//                                        before: Int,
//                                        count: Int
//                                    ) {
//                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
//                                            !s.isNullOrBlank()
//                                    }
//
//                                    override fun afterTextChanged(s: Editable?) = Unit
//                                })
//                            dialog.show()
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}