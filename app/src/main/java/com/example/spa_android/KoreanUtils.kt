package com.example.spa_android

object KoreanUtils {
    private const val HANGUL_BEGIN_UNICODE = 44032 // '가'
    private const val HANGUL_END_UNICODE = 55203 // '힣'
    private const val HANGUL_BASE_UNIT = 588

    private val INITIAL_SOUND = charArrayOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
        'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ',
        'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ',
        'ㅎ'
    )

    fun convertCharToUnicode(ch: Char): Int {
        return ch.code
    }

    fun convertStringToUnicode(str: String): IntArray {
        return str.map { convertCharToUnicode(it) }.toIntArray()
    }

    fun convertUnicodeToChar(unicode: Int): Char {
        return unicode.toChar()
    }

    fun getKoreanInitialSound(value: String): String {
        val result = StringBuilder()
        val unicodeList = convertStringToUnicode(value)
        for (unicode in unicodeList) {
            if (unicode in HANGUL_BEGIN_UNICODE..HANGUL_END_UNICODE) {
                val tmp = unicode - HANGUL_BEGIN_UNICODE
                val index = tmp / HANGUL_BASE_UNIT
                result.append(INITIAL_SOUND[index])
            } else {
                result.append(convertUnicodeToChar(unicode))
            }
        }
        return result.toString()
    }

    fun getIsConsonantsList(name: String): BooleanArray {
        val consonantsList = BooleanArray(name.length)
        for (i in name.indices) {
            val c = name[i]
            consonantsList[i] = INITIAL_SOUND.contains(c)
        }
        return consonantsList
    }

    fun getKoreanInitialSound(value: String, searchKeyword: String): String {
        return getKoreanInitialSound(value, getIsConsonantsList(searchKeyword))
    }

    private fun getKoreanInitialSound(value: String, isChoList: BooleanArray): String {
        val result = StringBuilder()
        val unicodeList = convertStringToUnicode(value)
        for (idx in unicodeList.indices) {
            val unicode = unicodeList[idx]
            if (idx < isChoList.size && isChoList[idx]) {
                if (unicode in HANGUL_BEGIN_UNICODE..HANGUL_END_UNICODE) {
                    val tmp = unicode - HANGUL_BEGIN_UNICODE
                    val index = tmp / HANGUL_BASE_UNIT
                    result.append(INITIAL_SOUND[index])
                } else {
                    result.append(convertUnicodeToChar(unicode))
                }
            } else {
                result.append(convertUnicodeToChar(unicode))
            }
        }
        return result.toString()
    }

    // 초성 검색 기능 추가
    fun filterByInitialSound(itemList: List<String>, searchKeyword: String): List<String> {
        val searchInitialSound = getKoreanInitialSound(searchKeyword)
        return itemList.filter { item ->
            getKoreanInitialSound(item).startsWith(searchInitialSound)
        }
    }
}
