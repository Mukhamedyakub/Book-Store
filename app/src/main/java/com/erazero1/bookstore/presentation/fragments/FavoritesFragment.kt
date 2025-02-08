package com.erazero1.bookstore.presentation.fragments

import FavoritesAdapter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erazero1.bookstore.R
import com.erazero1.bookstore.data.local.converters.toBook
import com.erazero1.bookstore.presentation.viewmodels.FavoriteViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var textViewFavoritesIsEmpty: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)
        textViewFavoritesIsEmpty = view.findViewById(R.id.textViewFavoritesIsEmpty)
        recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesAdapter(emptyList()) { favorite ->
            viewModel.removeFavorite(favorite.toBook())
        }
        recyclerViewFavorites.adapter = favoritesAdapter

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isEmpty()) {
                textViewFavoritesIsEmpty.visibility = TextView.VISIBLE
            } else {
                textViewFavoritesIsEmpty.visibility = TextView.GONE
            }
            favoritesAdapter.updateFavorites(favorites)
        }
    }
}
