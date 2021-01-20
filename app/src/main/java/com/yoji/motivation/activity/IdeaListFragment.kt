package com.yoji.motivation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.motivation.R
import com.yoji.motivation.adapter.IdeaAdapter
import com.yoji.motivation.viewmodel.IdeaListViewModel
import com.yoji.motivation.databinding.FragmentIdeaListBinding
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.listeners.OnIdeaClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdeaListFragment : Fragment() {

    private var _binding: FragmentIdeaListBinding? = null
    private val binding get() = _binding!!
    private val ideaListViewModel: IdeaListViewModel by viewModels(ownerProducer = ::requireActivity)

    private val ideaAdapter by lazy {
        IdeaAdapter(object : OnIdeaClickListener {

            override fun onLike(idea: Idea) {
                ideaListViewModel.likeById(idea.id)
            }

            override fun onDislike(idea: Idea) {
                ideaListViewModel.dislikeById(idea.id)
            }

            override fun onShare(idea: Idea) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, idea.author + "\n" + idea.content)
                    putExtra(Intent.EXTRA_STREAM, idea.imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    type = "*/*"
//                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "Share idea"))
            }

            override fun onLink(idea: Idea) {
                if (idea.link.isNotBlank()) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        with(idea.link) {
                            data = if (!this.startsWith("http://") && !this.startsWith("https://"))
                                Uri.parse("http://$this")
                            else Uri.parse(this)
                        }
                    }
                    startActivity(intent)
                }
            }

            override fun onAuthor(idea: Idea) {
                updateDataByAuthor(idea.author)
            }

            override fun onDelete(idea: Idea) {
                with(idea.imageUri) { if (toString() != "null") toFile().delete() }
                ideaListViewModel.removeById(idea.id)
            }

            override fun onEdit(idea: Idea) {
                ideaListViewModel.edit(idea)
                findNavController().navigate(R.id.action_ideaListFragment_to_createOrEditFragment)
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
            ideaListViewModel.clear()
            findNavController().navigate(R.id.action_ideaListFragment_to_createOrEditFragment)
        }

//        ideaListViewModel.createDemoIdeas()

        ideaListViewModel.data.observe(viewLifecycleOwner) { ideas ->
            ideaAdapter.submitList(ideas)
        }

        return binding.root
    }

    private fun updateDataByAuthor(author: String) {
        with(ideaListViewModel) {
            if (isFiltered()) clearAuthor() else setAuthor(author)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}