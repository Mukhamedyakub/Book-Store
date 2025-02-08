package com.erazero1.bookstore.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erazero1.bookstore.R
import com.erazero1.bookstore.presentation.adapters.CartAdapter
import com.erazero1.bookstore.presentation.viewmodels.CartViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.roundToInt

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var viewModel: CartViewModel
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var overallPriceTextView: TextView
    private lateinit var fabBuy: FloatingActionButton
    private lateinit var cartAdapter: CartAdapter
    private lateinit var textViewCartIsEmpty: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        initObservers()
        initListeners()
    }

    private fun initViews(view: View) {
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)
        overallPriceTextView = view.findViewById(R.id.textViewOverallPrice)
        fabBuy = view.findViewById(R.id.fabBuy)
        textViewCartIsEmpty = view.findViewById(R.id.textViewCartIsEmpty)

        cartAdapter = CartAdapter(emptyList()) { book ->
            viewModel.deleteBookFromCart(book)
        }
        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCart.adapter = cartAdapter
    }

    private fun initObservers() {
        viewModel.books.observe(viewLifecycleOwner) { books ->
            if (books.isEmpty()) {
                textViewCartIsEmpty.visibility = TextView.VISIBLE
            } else {
                textViewCartIsEmpty.visibility = TextView.GONE
            }
            cartAdapter.updateBooks(books)
        }

        viewModel.overallPrice.observe(viewLifecycleOwner) { price ->
            overallPriceTextView.text = "Total: ${price.roundToInt()} KZT"
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initListeners() {
        fabBuy.setOnClickListener {
            viewModel.buyCart()
            Toast.makeText(context, "This feature is not available now", Toast.LENGTH_SHORT).show()
        }
    }
}
