package com.yoji.motivation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment
import com.yoji.motivation.R
import com.yoji.motivation.databinding.DialogEditNameBinding
import com.yoji.motivation.viewmodel.IdeaListViewModel

class ChangeAuthorNameDialogFragment(
    private val ideaListViewModel: IdeaListViewModel,
    val authorId: Long
) :
    DialogFragment() {

    private var _binding: DialogEditNameBinding? = null
    private val binding get() = _binding!!

    private val dialog: AlertDialog by lazy {
        requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.change_user_name))
                setView(binding.root)
                setPositiveButton(getString(R.string.ok)) { _, _ ->
                    ideaListViewModel.changeAuthorName(
                        authorId,
                        binding.editNameEdtTxtViewId.editText?.text.toString()
                            .trim()
                    )
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            }
            builder.create()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditNameBinding.inflate(layoutInflater)

        binding.editNameEdtTxtViewId.editText?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        !s.isNullOrBlank()
                }

                override fun afterTextChanged(s: Editable?) = Unit
            }
        )

        return dialog
    }

    override fun onResume() {
        super.onResume()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}