package com.metacoding.vocabularylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.metacoding.vocabularylist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private var selectedWord: Word? = null

    private val updateAddWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { result ->
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false)
        if (result.resultCode == RESULT_OK && isUpdated == true) {
            updateAddWord()
        }
    }

    private val updateEditWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    { result ->

        val editdWord = result.data?.getParcelableExtra<Word>("editWord")
        if (result.resultCode == RESULT_OK && editdWord != null) {
            updateEditWord(editdWord)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.addButton.setOnClickListener {

            Intent(this, AddActivity::class.java).let {
                updateAddWordResult.launch(it)
            }
        }
        binding.deleteImageView.setOnClickListener {
            delete()
        }

        binding.editImageView.setOnClickListener {
            edit()
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
            Log.d("listt", "$list")
            wordAdapter.list.addAll(list)

            //어댑터에 데이터를 넣었음을 알리는 내용이 반드시 추가되어야 함
            runOnUiThread {
                wordAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun updateAddWord() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let { word ->
                wordAdapter.list.add(0, word)

                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    private fun updateEditWord(word: Word) {
        val index = wordAdapter.list.indexOfFirst { it.id == word.id }
        wordAdapter.list[index] = word
        runOnUiThread { wordAdapter.notifyItemChanged(index) }

        runOnUiThread {

            selectedWord = word
            wordAdapter.notifyItemChanged(index)
            binding.wordTextView.text = ""
            binding.meanTextView.text = ""
        }
    }

    private fun delete() {
        if (selectedWord == null) return
        Thread {

            selectedWord?.let { word ->

                AppDatabase.getInstance(this)?.wordDao()?.delete(word)

                runOnUiThread {

                    wordAdapter.list.remove(word)

                    wordAdapter.notifyDataSetChanged()
                    binding.wordTextView.text = ""
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun edit(){
        if(selectedWord == null) return

        val intent = Intent(this,AddActivity::class.java).putExtra("originWord",selectedWord)
        updateEditWordResult.launch(intent)
    }

    override fun onClick(word: Word) {

        //선택된 단어 담기
        selectedWord = word

        //UI 업데이트
        binding.wordTextView.text = word.word
        binding.meanTextView.text = word.mean
    }
}