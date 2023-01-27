package com.metacoding.vocabularylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metacoding.vocabularylist.databinding.ItemWordBinding
import java.util.zip.Inflater


class WordAdapter(
    //단어(Word) 배열
    private val list: MutableList<Word>,
    private val itemClickListener: ItemClickListener? = null,
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {


    //뷰홀더를 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        //뷰를 그리기
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemWordBinding.inflate(inflater, parent, false)

        //뷰 홀더에 뷰를 넣고 리턴
        return WordViewHolder(binding)
    }

    //뷰홀더에 데이터를 넣어주기
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {

        //홀더에서 아이템뷰를 갖고 온 뒤
        val word = list[position]   //데이터 갖고 오기
        holder.bind(word)           //뷰홀더와 데이터 바인딩해주기 (넣어주기)


        holder.itemView.setOnClickListener{
            itemClickListener?.onClick(word)
        }
    }

    //어댑터가 갖고있는 리스트의 데이터 개수를 반환
    override fun getItemCount(): Int {
        return list.size
    }

    //view holder는 inner class로 만듦
    class WordViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //데이터를 연결해주는 함수
        fun bind(word: Word) {
            binding.apply {
                //list에서 데이터 갖고오기
                wordTextView.text = word.word
                meanTextView.text = word.mean
                typeChip.text = word.type
            }
        }
    }

    //clickListener
    interface ItemClickListener {
        fun onClick(word: Word)
    }
}