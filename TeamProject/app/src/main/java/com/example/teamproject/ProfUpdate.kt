package com.example.teamproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.teamproject.databinding.FragmentMainBinding
import com.example.teamproject.databinding.FragmentProfInfoBinding
import com.example.teamproject.databinding.FragmentProfUpdateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfUpdate.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfUpdate : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfUpdateBinding? = null
    private val binding get() = _binding!!
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfUpdateBinding.inflate(inflater, container, false)
        db = ProfessorDatabase.getInstance(requireContext())

        setupButtonListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 교수 정보 표시
        val currentProfessor = (requireActivity() as MainActivity).selectedProfessor
        currentProfessor?.let {
            binding.profNameInput.setText(it.name)
            binding.profDegreeInput.setText(it.degree)
            binding.profUniversityInput.setText(it.university)
            binding.profFieldInput.setText(it.field)
            binding.profEmailInput.setText(it.email)
            binding.profLabInput.setText(it.lab)
        }
    }

    private fun setupButtonListeners() {
        binding.updateButton.setOnClickListener {
            if (validateInputs()) {
                lifecycleScope.launch {
                    updateProfessor()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_prof_update_to_info)
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // 이름 필드 검증
        if (binding.profNameInput.text.toString().trim().isEmpty()) {
            binding.profNameInput.error = "이름을 입력해주세요"
            isValid = false
        }

        // 학위 필드 검증
        if (binding.profDegreeInput.text.toString().trim().isEmpty()) {
            binding.profDegreeInput.error = "학위를 입력해주세요"
            isValid = false
        }

        // 대학교 필드 검증
        if (binding.profUniversityInput.text.toString().trim().isEmpty()) {
            binding.profUniversityInput.error = "대학교를 입력해주세요"
            isValid = false
        }

        // 전공 분야 검증
        if (binding.profFieldInput.text.toString().trim().isEmpty()) {
            binding.profFieldInput.error = "전공 분야를 입력해주세요"
            isValid = false
        }

        // 이메일 형식 검증
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+"
        val email = binding.profEmailInput.text.toString().trim()
        if (email.isEmpty() || !email.matches(emailPattern.toRegex())) {
            binding.profEmailInput.error = "올바른 이메일 형식을 입력해주세요"
            isValid = false
        }

        // 연구실 필드 검증
        if (binding.profLabInput.text.toString().trim().isEmpty()) {
            binding.profLabInput.error = "연구실을 입력해주세요"
            isValid = false
        }

        return isValid
    }

    private suspend fun updateProfessor() {
        try {
            val currentProfessor = (requireActivity() as MainActivity).selectedProfessor

            val update_prof = Professor(
                id = currentProfessor?.id ?: 0,  // 기존 교수의 ID 유지
                name = binding.profNameInput.text.toString(),
                degree = binding.profDegreeInput.text.toString(),
                university = binding.profUniversityInput.text.toString(),
                field = binding.profFieldInput.text.toString(),
                email = binding.profEmailInput.text.toString(),
                lab = binding.profLabInput.text.toString()
            )

            withContext(Dispatchers.IO) {
                db.professorDao().update(update_prof)
            }

            // MainActivity의 selectedProfessor 업데이트
            (requireActivity() as MainActivity).selectedProfessor = update_prof

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "교수 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_fragment_prof_update_to_main)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "수정 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment profUpdate.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfUpdate().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}