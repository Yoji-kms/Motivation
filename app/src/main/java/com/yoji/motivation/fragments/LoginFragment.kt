package com.yoji.motivation.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yoji.motivation.databinding.FragmentLoginBinding
import com.yoji.motivation.utils.DataStoreUtils
import com.yoji.motivation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel by viewModels<LoginViewModel>(ownerProducer = ::requireActivity)

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val AUTHOR_ID = longPreferencesKey("author_id")
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

            saveAuthorBtnId.isEnabled = false

            saveAuthorBtnId.setOnClickListener {
                lifecycleScope.launch {
                    val authorId = loginViewModel.saveAuthor(
                        authorTextInputLayoutId.editText?.text.toString()
                    )

                    DataStoreUtils.setValue(AUTHOR_ID, authorId)
                }.invokeOnCompletion { findNavController().navigateUp() }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}