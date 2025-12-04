package kzs.th000

import kotlin.streams.toList

enum class Ordering {
    LESS,
    EQUAL,
    GREATER,
}

data class IdRange(val start: String, val end: String) {
    companion object {
        fun parseText(data: String): IdRange {
            val parts = data.split("-").toList()
            val start = parts[0]
            val end = parts[1]
            return IdRange(start, end)
        }
    }
}

fun getFirstHalf(s: String): String {
    return s.substring(0, s.length / 2)
}

// Input    Output
//     1        10
//     2        10
//     9        10
//    11       100
//    99       100
//   100      1000
//   999      1000
//  1000     10000
//  9999     10000
// 10000    100000
// 99999    100000
fun generateNextLevelMinNumber(s: String): String {
    return "1" + "0".repeat(s.length)
}

fun compareString(s1: String, s2: String): Ordering {
    if (s1.length < s2.length) {
        return Ordering.LESS
    } else if (s1.length > s2.length) {
        return Ordering.GREATER
    }

    for (pos in 0..s1.length - 1) {
        val c1 = s1[pos]
        val c2 = s2[pos]
        if (c1 < c2) {
            return Ordering.LESS
        } else if (c1 > c2) {
            return Ordering.GREATER
        }
    }

    return Ordering.EQUAL
}

fun String.notGreaterThan(other: String): Boolean {
    return compareString(this, other) != Ordering.GREATER
}

fun String.notLessThan(other: String): Boolean {
    return compareString(this, other) != Ordering.LESS
}

fun safeAdd1(s: List<Char>): List<Char> {
    if (s.isEmpty()) {
        return listOf('1')
    }
    var ss = s.toMutableList()
    val last = ss.removeLast()
    when (last.digitToInt()) {
        in 0..8 -> ss.add((last.digitToInt() + 1).digitToChar())
        9 -> {
            ss = safeAdd1(ss.toList()).toMutableList()
            ss.add('0')
        }
        else -> throw Exception("unreachable")
    }
    return ss
}

fun solvePart1(ranges: List<IdRange>): ULong {
    val invalidIds = mutableSetOf<ULong>()
    // 95 - 101
    //
    // 95.length % 2 == 0 => 95.substring(0, 95.length()) = 9
    // 9.duplicate = 99
    // 99 >= start && 99 <= end ?   +1
    //
    // 998-1012
    //
    // 998.length % 2 != 0
    // 998.length + 1 = 4 => 1000 => 1000.substring(0, 1000.length()) = 10
    // 10.duplicate = 1010
    // 1010 >= start && 1010 <= end ?   +1
    // 11.duplicate = 1111
    // 11111 >= start && 1111 <= end ?   FALSE
    //
    // 11-22
    //
    // 800 - 10000
    // 900.length %2 != 0
    // 900.length + 1 = 4 => 1000 => 1000.substring(0, 1000.length()) = 10
    // 10.duplicate = 1010
    // 1010 >= start && 1010 <= end ?   +1
    // 11.duplicate = 1111
    // 1111 >= start && 1111 <= end ?   +1
    // 12 ...  +1
    // 13 ... +1
    // 14 ... +1
    // ...
    // 99 ... +1
    // 100.duplicate = 100100
    // 100100 >= start && 100100 <= end ?  FALSE
    //
    //
    // For each IdRange:
    //
    // seed = if start.length() %2 == 0
    //     getFirstHalf(start)
    // else
    //     getFirstHalf(generateMinNumber(start.length() + 1)) // 1...0
    // v = seed.duplicate()
    // loop
    //     if v <= end
    //         if v >= start
    //             COUNT++
    //         seed = safe_add(v, 1)
    //         continue
    //     else
    //         break
    for (r in ranges) {
        println("checking range: $r")
        var seed: String =
                when (r.start.length % 2 == 0) {
                    true -> getFirstHalf(r.start)
                    false -> getFirstHalf(generateNextLevelMinNumber(r.start))
                }
        while (true) {
            val value = seed.repeat(2)
            if (value.notGreaterThan(r.end)) {
                if (value.notLessThan(r.start)) {
                    invalidIds.add(value.toULong())
                    println("hit=${value.toULong()}")
                }
                seed = safeAdd1(seed.toCharArray().toList()).joinToString("")
                println("new seed=$seed")
                continue
            } else {
                break
            }
        }
    }
    println("invalidIds=$invalidIds")
    return invalidIds.sum()
}

fun main() {
    val ranges = AocUtils.readInput(2).split(",").map { IdRange.parseText(it) }.toList()
    // 17931620893 too low.
    // 17931620992 too low.
    // 29940925364 too high.
    // 29940924880 <- use mutableSetOf, instead of mutableListOf to remove duplicate ids.
    println("part1: ${solvePart1(ranges)}")
}
