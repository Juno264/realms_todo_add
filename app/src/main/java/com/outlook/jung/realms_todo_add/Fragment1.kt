package com.outlook.jung.realms_todo_add

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_1.*
import java.util.*

class Fragment1 : Fragment() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private lateinit var adapter: WordAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return  inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskList = readAll()

        val adapter = WordAdapter(requireContext(), taskList, true)

        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView)

        if (taskList.isEmpty()) {
            createDummyData()
        }


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

    }

    private val swipeToDismissTouchHelper by lazy {
        getSwipeToDismissTouchHelper(adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun createDummyData() {
        for (i in 0..10) {
            create("日本語 $i","英語 $i")
        }
    }

    fun create(japanese: String, english: String) {
        realm.executeTransaction {
            val task = it.createObject(Word::class.java, UUID.randomUUID().toString())
            task.japanese = japanese
            task.english = english
        }
    }

    fun readAll(): RealmResults<Word> {
        return realm.where(Word::class.java).findAll().sort("createdAt", Sort.DESCENDING)
    }

    private fun getSwipeToDismissTouchHelper(adapter: WordAdapter) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val timeTasks = adapter.getItems()
                mainViewModel.delete(timeTasks[viewHolder.adapterPosition])
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)
                val deleteIcon = AppCompatResources.getDrawable(
                    context as Context,
                    R.drawable.ic_baseline_delete_forever_24
                )
                val iconMarginVertical =
                    (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2

                deleteIcon.setBounds(
                    itemView.left + iconMarginVertical,
                    itemView.top + iconMarginVertical,
                    itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMarginVertical
                )
                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.right + dX.toInt(),
                    itemView.bottom
                )
                background.draw(c)
                deleteIcon.draw(c)
            }
        })
}
