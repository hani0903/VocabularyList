package com.metacoding.vocabularylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.metacoding.vocabularylist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let {
                startActivity(it)
            }
        }
    }

    private fun initRecyclerView() {

//        //예시 데이터 만들어보기
//        val dummyList = mutableListOf(
//            Word("weather", "날씨", "명사"),
//            Word("run", "실행하다", "동사"),
//            Word("lovely", "사랑스러운", "형용사"),
//        )

        /**리사이클러뷰*/
        //1. Adapter 만들기
        //어댑터에 데이터 넣기
        wordAdapter = WordAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            adapter = wordAdapter

            //layout 매니저 지정하기
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            //아이템 사이에 선 긋기
            val dividerItemDecoration =
                DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }

        Thread {
            //Database에서 data 갖고오기
            //어댑터에 데이터를 넣어주기
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            Log.d("listt","$list")
            wordAdapter.list.addAll(list)

            //어댑터에 데이터를 넣었음을 알리는 내용이 반드시 추가되어야 함
            runOnUiThread {
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.word}가 클릭됐습니다", Toast.LENGTH_SHORT)
    }
}