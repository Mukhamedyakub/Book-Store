import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erazero1.bookstore.R
import com.erazero1.bookstore.model.FavoriteBook

class FavoritesAdapter(
    private var favorites: List<FavoriteBook>,
    private val onRemoveClick: (FavoriteBook) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewCover: ImageView = itemView.findViewById(R.id.imageViewCover)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val buttonRemove: ImageButton = itemView.findViewById(R.id.buttonRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_book, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = favorites[position]
        holder.textViewTitle.text = favorite.title
        holder.textViewAuthor.text = favorite.author
        Glide.with(holder.itemView.context)
            .load(favorite.cover)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(holder.imageViewCover)

        holder.buttonRemove.setOnClickListener {
            onRemoveClick(favorite)
        }
    }

    override fun getItemCount(): Int = favorites.size

    fun updateFavorites(newFavorites: List<FavoriteBook>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}
