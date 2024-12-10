package com.example.teamproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.teamproject.databinding.FragmentProfInfoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profInfo.newInstance] factory method to
 * create an instance of this fragment.
 */
class profInfo : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfInfoBinding.inflate(inflater, container, false)
        binding.btnUpdate.setOnClickListener { findNavController().navigate(R.id.action_fragment_prof_info_to_update) }
        binding.btnBack.setOnClickListener { findNavController().navigate(R.id.action_fragment_prof_info_to_main) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val professor = (requireActivity() as MainActivity).selectedProfessor

        professor?.let {
            binding.professorNameTextView.text = getString(R.string.prof_name, it.name)
            binding.professorDegreeTextView.text = getString(R.string.prof_degree, it.degree)
            binding.professorUnivTextView.text = getString(R.string.prof_university, it.university)
            binding.professorFieldTextView.text = getString(R.string.prof_field, it.field)
            binding.professorEmailTextView.text = getString(R.string.prof_email, it.email)
            binding.professorLabTextView.text = getString(R.string.prof_lab, it.lab)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profInfo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}