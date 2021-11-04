package com.swapnil.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.swapnil.mvvmnewsapp.R
import com.swapnil.mvvmnewsapp.adapters.NewsAdapter
import com.swapnil.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.swapnil.mvvmnewsapp.ui.NewsViewModel
import com.swapnil.mvvmnewsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter
    private val viewModel: NewsViewModel by viewModels()
    val TAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewModel.breakingNews.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { errorMessage ->
                        Log.d(TAG, "onViewCreated: Error: $errorMessage")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }


}