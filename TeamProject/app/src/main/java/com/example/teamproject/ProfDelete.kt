package com.example.teamproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.databinding.DataBindingUtil
import com.example.teamproject.ProfessorDao
import com.example.teamproject.databinding.FragmentProfDeleteBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.teamproject.ProfessorDatabase
import com.example.teamproject.Professor
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject.databinding.ItemProfessorDeleteBinding
import android.widget.CompoundButton

/**
 * A simple [Fragment] subclass.
 * Use the [profDelete.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfDelete : Fragment() {
    private var _binding: FragmentProfDeleteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProfessorDeleteAdapter
    private lateinit var professorDao: ProfessorDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_prof_delete,
            container,
            false
        )
        _binding!!.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        professorDao = ProfessorDatabase.getInstance(requireContext()).professorDao()
        lifecycleScope.launch {
            setupRecyclerView()
        }
        setupButtons()
    }

    private suspend fun setupRecyclerView() {
        val professors = professorDao.getAll()
        adapter = ProfessorDeleteAdapter(professors.toList())
        binding.professorList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ProfDelete.adapter
        }
    }

    private fun setupButtons() {
        binding.deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val checkedProfessors = adapter.getCheckedProfessors()
                if (checkedProfessors.isEmpty()) {
                    Toast.makeText(requireContext(), "삭제할 교수를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                checkedProfessors.forEach { professor ->
                    professorDao.delete(professor)
                }

                Toast.makeText(requireContext(), "선택한 교수가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ProfessorDeleteAdapter(
    private val professors: List<Professor>
) : RecyclerView.Adapter<ProfessorDeleteAdapter.ViewHolder>() {

    private val checkedProfessors = mutableSetOf<Professor>()

    inner class ViewHolder(private val binding: ItemProfessorDeleteBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(professor: Professor) {
            binding.professor = professor
            binding.isChecked = checkedProfessors.contains(professor)
            binding.checkboxListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkedProfessors.add(professor)
                } else {
                    checkedProfessors.remove(professor)
                }
            }
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                // 클릭 시 수행할 작업
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfessorDeleteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(professors[position])
    }

    override fun getItemCount() = professors.size

    fun getCheckedProfessors() = checkedProfessors.toList()
}