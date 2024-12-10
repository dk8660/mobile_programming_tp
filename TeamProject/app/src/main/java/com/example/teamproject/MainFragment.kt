package com.example.teamproject

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject.Professor
import com.example.teamproject.ProfessorDatabase
import com.example.teamproject.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMainBinding.bind(view)

        // RecyclerView 초기화
                binding.professorRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Room 데이터베이스에서 교수 정보 불러오기
        val database = ProfessorDatabase.getInstance(requireContext())
        val professorDao = database.professorDao()

        lifecycleScope.launch {
//            //   임시 데이터 삽입
//            val sampleData = listOf(
//                Professor(name = "김낙현", degree = "공학박사", university = "University of Texas Austin", field = "컴퓨터비젼, 멀티미디어 신호처리, 멀티미디어 소프트웨어개발", email = "nhkim@hufs.ac.kr", lab = "공학관 420호, 백년관 801호"),
//                Professor(name = "김상철", degree = "공학박사", university = "Michigan State University", field = "멀티미디어시스템, 컴퓨터게임", email = "kimsa@hufs.ac.kr", lab = "공학관 416호")
//            )
//            sampleData.forEach { professorDao.insert(it) }
            val professors = professorDao.getAll().toList() // Room에서 데이터 가져오기
            val adapter = ProfessorAdapter(professors) {professor ->
                (requireActivity() as MainActivity).selectedProfessor = professor
                findNavController().navigate(R.id.action_main_to_prof_info)
            }
            binding.professorRecyclerView.adapter = adapter
        }

        // 메뉴 버튼 클릭 시 드로어 열기
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        // 네비게이션 뷰 설정
        val navigationView: NavigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_main_to_prof_create -> {
                    // 교수 정보 추가 클릭 시 이동
                    findNavController().navigate(R.id.action_main_to_prof_create)
                }
//                R.id.action_main_to_prof_update -> {
//                    // 교수 정보 수정 클릭 시 이동
//                    findNavController().navigate(R.id.action_main_to_prof_update)
//                }
                R.id.action_main_to_prof_delete -> {
                    // 교수 정보 삭제 클릭 시 이동
                    findNavController().navigate(R.id.action_main_to_prof_delete)
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
