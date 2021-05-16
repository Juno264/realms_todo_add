package com.outlook.jung.realms_todo_add

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Word (
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var japanese: String = "",
    open var english: String = "",
    open var createdAt: Date = Date(System.currentTimeMillis())
        ) : RealmObject()