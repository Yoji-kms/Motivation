package com.yoji.motivation.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yoji.motivation.R
import com.yoji.motivation.adapter.IdeaAdapter
import com.yoji.motivation.viewmodel.IdeaListViewModel
import com.yoji.motivation.databinding.FragmentIdeaListBinding
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor
import com.yoji.motivation.fragments.LoginFragment.Companion.AUTHOR_ID
import com.yoji.motivation.listeners.OnIdeaClickListener
import com.yoji.motivation.utils.DataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IdeaListFragment : Fragment() {

    private var _binding: FragmentIdeaListBinding? = null
    private val binding get() = _binding!!
    private val ideaListViewModel by viewModels<IdeaListViewModel>(ownerProducer = ::requireActivity)

    private val authorKey = stringPreferencesKey("saved_filter")

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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

                        lifecycleScope.launch {
                            DataStoreUtils.setValue(authorKey, ideaWithAuthor.authorName)
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
                title = DataStoreUtils.getString(authorKey)
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

        val authorId = DataStoreUtils.getLong(AUTHOR_ID)
        val navController = findNavController()

        if (authorId == 0L) navController.navigate(R.id.loginFragment)
        else {
            ideaListViewModel.setAuthor(authorId)
            binding.authorNameToolbarId.apply {
                ideaListViewModel.currentAuthor.observe(viewLifecycleOwner) {
                    title = getString(R.string.user_name, it.name)
                }
                also { it.menu.clear() }.inflateMenu(R.menu.author_toolbar_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit_author -> {
                            ChangeAuthorNameDialogFragment(
                                ideaListViewModel,
                                authorId
                            ).show(childFragmentManager, "change_author_name_tag")
                            true
                        }
                        R.id.change_author -> {
                            ChangeAuthorDialogFragment(
                                ideaListViewModel
                            ).show(childFragmentManager, "change_author_tag")
                            true
                        }
                        R.id.add_author -> {
                            navController.navigate(R.id.loginFragment)
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