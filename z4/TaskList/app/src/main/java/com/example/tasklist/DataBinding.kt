package com.example.tasklist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class DataBinding{
    companion object{
        var list:ArrayList<Task> = arrayListOf()
        fun read(c:Context){
            val dbHelper = FeedReaderDbHelper(c)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM Tasks",null)
            list = arrayListOf()
            with(cursor) {
                while (moveToNext()) {
                    val name = getString(getColumnIndex("name"))
                    val description = getString(getColumnIndex("description"))
                    val dtime = getString(getColumnIndex("dtime"))
                    val done = getInt(getColumnIndex("done")) != 0
                    list.add(Task(name,description, dtime, done))
                }
            }
            cursor.close()
        }
        fun write(c:Context){
            val dbHelper = FeedReaderDbHelper(c)
            val db = dbHelper.writableDatabase
            //delete old
            db.delete("Tasks",null,null)
            //write new
            for(task in list){
                val values = ContentValues().apply {
                    put("name", task.name)
                    put("description",task.description)
                    put("dtime",task.dtime)
                    put("done",task.done)
                }
                // Insert the new row, returning the primary key value of the new row
                val newRowId = db?.insert("Tasks", null, values)
            }
        }
        fun at(i:Int):Task{ return list[i] }
        fun size():Int{ return list.size }
        fun add(t:Task){ list.add(t) }
        fun indexOf(t:Task):Int{ return list.indexOf(t) }
        fun remove(t:Task){ list.remove(t) }
        fun removeAt(i:Int){ list.removeAt(i) }
    }
}

data class Task(val name:String,val description:String,val dtime:String,var done:Boolean=false)

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE Tasks (" +
            "name VARCHAR(255) PRIMARY KEY," +
            "description VARCHAR(255)," +
            "dtime VARCHAR(255),"+
            "done boolean"+
            ")"
private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS Tasks"

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}

