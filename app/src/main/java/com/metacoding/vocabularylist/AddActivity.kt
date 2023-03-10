package com.metacoding.vocabularylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.forEach
import com.google.android.material.chip.Chip
import com.metacoding.vocabularylist.databinding.ActivityAddBinding
import java.util.zip.Inflater
import kotlin.concurrent.thread

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var originWord: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {

            if (originWord == null) add() else edit()
        }
    }

    private fun initViews() {
        val types = listOf(
            "명사", "동사", "대명사", " 형용사", "부사", "감탄사", "전치사", "접속사"
        )

        binding.typeChipGroup.apply {
            types.forEach { text ->
                addView(createChip(text))
            }
        }

        originWord = intent.getParcelableExtra("originWord")
        originWord?.let { word ->
            binding.wordInputEditText.setText(word.word)
            binding.meanInputEditText.setText(word.mean)
            val selectedChip =
                binding.typeChipGroup.children.firstOrNull { (it as Chip).text == word.type } as? Chip
            selectedChip?.isCheckable = true
        }
    }

    //chip을 만들어주는 함수
    private fun createChip(text: String): Chip {
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }

    //단어, 타입을 갖고 오는 함수 만들기

    //새로운 단어 추가하는 함수
    private fun add() {
        //단어, 뜻, 타입을 갖고 오기
        val text = binding.wordInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()

        //selected 된 Chip 을 갖고 오기
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()

        //입력한 설정값을 모두 갖고 와서 새로운 단어를 만든다.
        val word = Word(text, mean, type)

        //데이터베이스에 단어 갖고 오기
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.insert(word)

            //toast는 화면에 UI를 그리는 것이므로 UIThread에서 해준다.
            runOnUiThread {
                Toast.makeText(this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show()

            }

            val intent = Intent().putExtra("isUpdated", true)

            setResult(RESULT_OK, intent)
            finish()
        }.start()
    }

    private fun edit() {
        //단어, 뜻, 타입을 갖고 오기
        val text = binding.wordInputEditText.text.toString()
        val mean = binding.meanInputEditText.text.toString()

        //selected 된 Chip 을 갖고 오기
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()

        //입력한 설정값을 모두 갖고 와서 새로운 단어를 만든다.
        val editWord = originWord?.copy(word = text, mean = mean, type = type)

        //데이터베이스에 단어 갖고 오기
        Thread {
            editWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.update(word) ?: null
                val intent = Intent().putExtra("editWord", editWord)
                setResult(RESULT_OK, intent)

                //toast는 화면에 UI를 그리는 것이므로 UIThread에서 해준다.
                runOnUiThread {
                    Toast.makeText(this, "수정을 완료했습니다.", Toast.LENGTH_SHORT).show()
                }

                finish()
            }

        }.start()
    }
}