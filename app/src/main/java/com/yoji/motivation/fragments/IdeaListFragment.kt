package com.yoji.motivation.fragments

import android.content.Context
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
import com.yoji.motivation.viewmodel.IdeaListViewModel
import com.yoji.motivation.databinding.FragmentIdeaListBinding
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor
import com.yoji.motivation.listeners.OnIdeaClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdeaListFragment : Fragment() {

    private var _binding: FragmentIdeaListBinding? = null
    private val binding get() = _binding!!
    private val ideaListViewModel by viewModels<IdeaListViewModel>(ownerProducer = ::requireActivity)
    private val prefs = App.appContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val authorKey = "saved_filter"

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
                        setAuthor(ideaWithAuthor.idea.authorId)
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
            findNavController().navigate(R.id.action_ideaListFragment_to_createOrEditFragment)
        }

        ideaListViewModel.data.observe(viewLifecycleOwner) {
            ideaAdapter.submitData(lifecycle, it)
        }

        binding.ideaListToolbarId.apply {
            setNavigationOnClickListener {
                with(ideaListViewModel) {
                    if (isFiltered()) {
                        clearAuthor()
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
                            clearAuthor()
                            binding.ideaListToolbarId.visibility = View.GONE
                        }
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}