package com.yoji.motivation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.motivation.databinding.FragmentLoginBinding
import com.yoji.motivation.fragments.IdeaListFragment.Companion.prefs
import com.yoji.motivation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel by viewModels<LoginViewModel>(ownerProducer = ::requireActivity)

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object{
        const val AUTHOR_ID: String = "AUTHOR_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            authorTextInputLayoutId.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    saveAuthorBtnId.isEnabled = !s.isNullOrBlank()
                }

                override fun afterTextChanged(s: Editable?) = Unit
            })

            saveAuthorBtnId.setOnClickListener {
                val authorId = loginViewModel.saveAuthor(
                    authorTextInputLayoutId.editText?.text.toString()
                )
                with(prefs.edit()){
                    putLong(AUTHOR_ID, authorId)
                    apply()
                }
                findNavController().popBackStack()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}