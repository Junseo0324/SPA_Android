package com.example.spa_android.Search

class Trie {
    private val root: TrieNode = TrieNode()

    // 단어 삽입
    fun insert(word: String) {
        var currentNode = root
        for (char in word) {
            currentNode = currentNode.children.computeIfAbsent(char) { TrieNode() }
        }
        currentNode.isEndOfWord = true
    }

    // 단어 검색
    fun search(word: String): Boolean {
        var currentNode = root
        for (char in word) {
            currentNode = currentNode.children[char] ?: return false
        }
        return currentNode.isEndOfWord
    }

    // 자음 검색
    fun startsWith(prefix: String): Boolean {
        var currentNode = root
        for (char in prefix) {
            currentNode = currentNode.children[char] ?: return false
        }
        return true
    }
}
