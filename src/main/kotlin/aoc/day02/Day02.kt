package aoc.day02

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
            val end = parts[1].trim()
            return IdRange(start, end)
        }
    }

    fun acceptLength(len: Int): Boolean =
        ((start.length)..(end.length)).toList().any { it % len == 0 }
}

/** @brief Get the first N chars in string */
fun getFirstNChars(s: String, n: Int): String = s.take(n)

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
fun generateNextLevelMinNumber(s: Int): String =
    if (s < 2) {
        "1"
    } else {
        "1" + "0".repeat(s - 1)
    }

fun compareString(s1: String, s2: String): Ordering {
    if (s1.length < s2.length) {
        return Ordering.LESS
    } else if (s1.length > s2.length) {
        return Ordering.GREATER
    }

    for (pos in s1.indices) {
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

fun String.notGreaterThan(other: String): Boolean = compareString(this, other) != Ordering.GREATER

fun String.notLessThan(other: String): Boolean = compareString(this, other) != Ordering.LESS

fun String.greaterThan(other: String): Boolean = compareString(this, other) == Ordering.GREATER

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

fun generateSeedWithLength(startLength: Int, minLength: Int, maxLength: Int): String? {
    println(
        "generateSeedWithLength: startLength: $startLength, minLength: $minLength, maxLength: $maxLength"
    )
    var len = startLength
    while (true) {
        if (len < minLength) {
            len += 1
            continue
        }
        if (len > maxLength) {
            return null
        }
        val seed = "1" + "0".repeat(len - 1)
        if (minLength % seed.length == 0) {
            return seed
        }
        len += 1
    }
}

fun solvePart1(ranges: List<IdRange>): ULong {
    val invalidIds = mutableSetOf<ULong>()
    for (r in ranges) {
        var seed: String =
            when (r.start.length % 2 == 0) {
                true -> getFirstNChars(r.start, r.start.length / 2)
                false ->
                    getFirstNChars(
                        generateNextLevelMinNumber(r.start.length),
                        r.start.length / 2
                    )
            }
        while (true) {
            val value = seed.repeat(2)
            if (value.notGreaterThan(r.end)) {
                if (value.notLessThan(r.start)) {
                    invalidIds.add(value.toULong())
                    // println("hit=${value.toULong()}")
                }
                seed = safeAdd1(seed.toCharArray().toList()).joinToString("")
                // println("new seed=$seed")
                continue
            }

            break
        }
    }
    // println("invalidIds=$invalidIds")
    return invalidIds.sum()
}

fun solvePart2(ranges: List<IdRange>): ULong {
    val invalidIds = mutableSetOf<ULong>()

    fun isStringRepeated(s: String): Boolean {
        val len = s.length
        val possibleSeedLength = when (len) {
            1 -> listOf(1)
            2 -> listOf(1)
            3 -> listOf(1)
            4 -> listOf(1, 2)
            5 -> listOf(1)
            6 -> listOf(1, 2, 3)
            7 -> listOf(1)
            8 -> listOf(1, 2, 4)
            9 -> listOf(1, 3)
            10 -> listOf(1, 2, 5)
            11 -> listOf(1)
            12 -> listOf(1, 2, 3, 4, 6)
            13 -> listOf(1)
            14 -> listOf(1, 2, 7)
            15 -> listOf(1, 3, 5)
            16 -> listOf(1, 2, 4, 8)
            else -> throw Exception("target string too long")
        }

        for (seedLen in possibleSeedLength) {
            val times = len / seedLen
            if (times == 1) {
                continue
            }
            val seed = s.subSequence(0, seedLen)
            if ((1..times).toList().map { it * seedLen }.windowed(2, 1).all { s.substring(it[0], it[1]) == seed }
            ) {
                return true
            }
        }
        return false
    }


    for (r in ranges) {
        var target = r.start;
        while (true) {
            if (target.greaterThan(r.end)) {
                break
            }
            if (isStringRepeated(target)) {
                invalidIds.add(target.toULong())
            }
            target = safeAdd1(target.toCharArray().toList()).joinToString("")
        }
    }

    return invalidIds.sum()
}

fun main() {
    val ranges = aoc.Utils.readInput(2).split(",").map { IdRange.parseText(it) }.toList()
    // 17931620893 too low.
    // 17931620992 too low.
    // 29940925364 too high.
    // 29940924880 <- use mutableSetOf, instead of mutableListOf to remove duplicate ids.
    println("part1: ${solvePart1(ranges)}")
    // 30092076142 too low.
    // 29940924869
    // 48631959042
    println("part2: ${solvePart2(ranges)}")
}
