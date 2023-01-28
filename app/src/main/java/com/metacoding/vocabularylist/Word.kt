package com.metacoding.vocabularylist

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//table로도 사용하기 위해 어노테이션 추가해주기
@Parcelize
@Entity(tableName = "word")
data class Word(
    val word: String,
    val mean: String,
    val type: String,

    //database에서 사용할 때에는 테이블에서 key값이 필요하다.
    //자동으로 생성되게끔 셋팅을 해준다!
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : Parcelable
