package com.metacoding.vocabularylist

import androidx.room.*

@Dao
interface WordDao {

    //리스트를 갖고오기
    @Query("SELECT * from word ORDER BY id DESC")
    fun getAll(): List<Word>

    @Query("SELECT * from word ORDER BY id DESC LIMIT 1")
    fun getLatestWord(): Word

    @Insert
    fun insert(word: Word)

    @Delete
    fun delete(word: Word)

    @Update
    fun update(word: Word)

}