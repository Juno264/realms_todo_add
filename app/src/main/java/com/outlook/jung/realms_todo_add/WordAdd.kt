package com.outlook.jung.realms_todo_add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.word_add.*
import java.util.*

class WordAdd : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_add)


        enter.setOnClickListener(){
            if (japanese_edit.text.toString() !="" && english_edit.text.toString() !="") {
                create(japanese_edit.text.toString(),english_edit.text.toString())
                finish()
            }
        }

        back.setOnClickListener(){
            finish()
        }


    }
    fun create(japanese: String, english: String) {
        realm.executeTransaction {
            val task = it.createObject(Word::class.java, UUID.randomUUID().toString())
            task.japanese = japanese
            task.english = english
        }
    }
}