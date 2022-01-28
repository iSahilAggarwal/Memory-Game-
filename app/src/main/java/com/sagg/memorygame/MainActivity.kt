package com.sagg.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sagg.memorygame.models.BoardSize
import com.sagg.memorygame.models.MemoryCard
import com.sagg.memorygame.models.MemoryGame
import com.sagg.memorygame.utils.DEFAULT_ICONS


class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG="MainActivity"
    }
    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter:MemoryBoardAdapter


    private var boardSize: BoardSize=BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot= findViewById(R.id.clRoot)
        rvBoard= findViewById(R.id.rvBoard)
        tvNumMoves= findViewById(R.id.tvNumMoves)
        tvNumPairs= findViewById(R.id.tvNumPairs)

        memoryGame= MemoryGame(boardSize)

        rvBoard.layoutManager= GridLayoutManager(this,boardSize.getWidth())
        adapter= MemoryBoardAdapter(this, boardSize, memoryGame.cards,object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
                Log.i(TAG, "Card Clicked $position")
            }

        })
        rvBoard.adapter= adapter
        rvBoard.setHasFixedSize(true)



    }

    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame()){
            Snackbar.make(clRoot, "You Already Won!!", Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            Snackbar.make(clRoot, "Invalid Move!", Snackbar.LENGTH_SHORT).show()
            return
        }
        if(memoryGame.flipCard(position)){
            Log.i(TAG, "Found a Match! Num Pairs Found ${memoryGame.numPairsFound}")
            tvNumPairs.text="Pairs: ${memoryGame.numPairsFound}/ ${boardSize.getNumPairs()}"
            if(memoryGame.haveWonGame()){
                Snackbar.make(clRoot, "You Have Won The Match! Congratulation", Snackbar.LENGTH_LONG).show()
            }
        }
        tvNumMoves.text="Moves: ${memoryGame.getNumMoves()}"

        adapter.notifyDataSetChanged()

    }
}
