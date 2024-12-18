package com.example.teamproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.teamproject.ProfessorDatabase
import com.example.teamproject.databinding.FragmentProfCreateBinding
import com.example.teamproject.Professor
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfCreate.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfCreate : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfCreateViewModel by viewModels()

    private lateinit var db: ProfessorDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_prof_create,
            container,
            false
        )
        
        // 이 두 줄이 중요합니다!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        
        // 데이터베이스 초기화
        db = ProfessorDatabase.getInstance(requireContext())
        
        setupButtonListeners()
        return binding.root
    }

    private fun setupButtonListeners() {
        binding.createButton.setOnClickListener {
            if (validateInputs()) {
                lifecycleScope.launch {
                    createProfessor()
                }
            }
        }
        
        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_prof_create_to_main)
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val name = viewModel.name.value ?: ""
        val degree = viewModel.degree.value ?: ""
        val university = viewModel.university.value ?: ""
        val field = viewModel.field.value ?: ""
        val email = viewModel.email.value ?: ""
        val lab = viewModel.lab.value ?: ""
        
        if (name.isBlank()) {
            binding.profNameInput.error = "이름을 입력해주세요"
            isValid = false
        }
        
        if (degree.isBlank()) {
            binding.profDegreeInput.error = "학위를 입력해주세요"
            isValid = false
        }
        
        if (university.isBlank()) {
            binding.profUniversityInput.error = "대학교를 입력해주세요"
            isValid = false
        }
        
        if (field.isBlank()) {
            binding.profFieldInput.error = "전공 분야를 입력해주세요"
            isValid = false
        }
        
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+"
        if (email.isBlank() || !email.matches(emailPattern.toRegex())) {
            binding.profEmailInput.error = "올바른 이메일 형식을 입력해주세요"
            isValid = false
        }
        
        if (lab.isBlank()) {
            binding.profLabInput.error = "연구실을 입력해주세요"
            isValid = false
        }
        
        return isValid
    }

    private suspend fun createProfessor() {
        try {
            val professor = Professor(
                name = viewModel.name.value ?: "",
                degree = viewModel.degree.value ?: "",
                university = viewModel.university.value ?: "",
                field = viewModel.field.value ?: "",
                email = viewModel.email.value ?: "",
                lab = viewModel.lab.value ?: ""
            )
            
            withContext(Dispatchers.IO) {
                db.professorDao().insert(professor)
            }
            
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "교수 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_fragment_prof_create_to_main)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "저장 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profCreate.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfCreate().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

