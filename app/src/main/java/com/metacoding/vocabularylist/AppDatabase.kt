package com.metacoding.vocabularylist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//entities : table
@Database(entities = [Word::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    //사용할 Dao 갖고오기
    abstract fun wordDao(): WordDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context:Context): AppDatabase?{
            if(INSTANCE == null){
                //초기화해줘야 한다
                synchronized(AppDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app-database.db"
                    ).build()
                }
            }
            //null이 아니면 return
            return INSTANCE
        }
    }
}