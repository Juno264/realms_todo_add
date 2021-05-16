package com.outlook.jung.realms_todo_add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class WordAdapter(
    private val context: Context,
    private var wordList: OrderedRealmCollection<Word>?,
    private val autoUpdate: Boolean
) :
    RealmRecyclerViewAdapter<Word, WordAdapter.WordViewHolder>(wordList, autoUpdate) {

    override fun getItemCount(): Int = wordList?.size ?: 0

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val task: Word = wordList?.get(position) ?: return

        holder.contentJapanese.text = task.japanese
        holder.contentEnglish.text = task.english
        holder.dateTextView.text =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(task.createdAt)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WordViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false)
        return WordViewHolder(v)
    }

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentJapanese: TextView = view.japanese_view
        val contentEnglish: TextView = view.english_view
        val dateTextView: TextView = view.dateTextView
    }

}