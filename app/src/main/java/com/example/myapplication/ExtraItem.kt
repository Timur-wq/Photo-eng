package com.example.myapplication

data class ExtraItem(val  engWord: String, val ruWord: String): Comparable<ExtraItem>{
    override fun compareTo(other: ExtraItem): Int {
        val r = if(this.engWord > other.engWord) 1 else if (this.engWord < other.engWord) -1 else 0
        return r
    }
}
