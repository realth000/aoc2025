package kzs.th000

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

/**
 * @brief Get the first N chars in string
 */
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
fun generateNextLevelMinNumber(s: Int): String = if (s < 2) {
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

    for (pos in 0..<s1.length) {
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

fun generateSeedWithLength(startLength: Int, targetLength: Int, maxLength: Int): String? {
    var len = startLength
    while (true) {
        if (len > maxLength) {
            return null
        }
        val seed = "1" + "0".repeat(len - 1)
        if (targetLength % seed.length == 0) {
            return seed
        }
        len += 1
    }
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
                true -> getFirstNChars(r.start, 2)
                false -> getFirstNChars(generateNextLevelMinNumber(r.start.length), 2)
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
    println("invalidIds=$invalidIds")
    return invalidIds.sum()
}


fun solvePart2(ranges: List<IdRange>): ULong {
    val invalidIds = mutableSetOf<ULong>()
    // For each IdRange:
    // For each possible `step` from 1 to 10, check if the Id is consists of a number repeated `step` times.
    // Note that the `2` in part1 is not `step` in part2, `2` is assume the Id is any times of 2 where 2 is `step`.
    //
    // var seed = if start.length() % step == 0
    //     getFirstNChars(start, step)
    // else
    //     generateSeedWithLength(step, start.length(), SOME_MAX_LENGTH) where
    //         (l: Int, targetLength: Int, maxLength: Int) : String? {
    //             var len = l - 1
    //             loop
    //                 if len > SOME_MAX_LENGTH
    //                     return null (?)
    //
    //                 val seed = '1' + '0'.repeat(len)
    //                 if targetLength % seed.length() == 0
    //                     return seed
    //                 len += 1
    //         }
    // loop
    //     if seed == null
    //         break
    //     v = seed.duplicate()
    //     loop
    //         if v <= end
    //             if v >= start
    //                 COUNT++
    //             seed = safe_add(v, 1)
    //             continue
    //         else
    //             break
    //     seed = geneSeedWithLength(step, seed, SOME_MAX_LENGTH)
    for (r in ranges) {
        println("checking range: $r")
        for (step in 1..10) {
            val t = r.start.length / step
            val m = r.start.length % step
            var seed: String? =
                when (t != 1 && m == 0) {
                    true -> getFirstNChars(r.start, step)
                    false -> generateSeedWithLength(step, r.start.length, r.end.length)
                }
            while (true) {
                if (seed == null) {
                    break
                }
                // FIXME: Check the times we need to repeat.
                for (times in 1..10) {
                    val value = seed!!.repeat(times)
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
        }
    }
    // println("invalidIds=$invalidIds")
    return invalidIds.sum()
}

fun main() {
    val ranges = AocUtils.readInput(2).split(",").map { IdRange.parseText(it) }.toList()
    // 17931620893 too low.
    // 17931620992 too low.
    // 29940925364 too high.
    // 29940924880 <- use mutableSetOf, instead of mutableListOf to remove duplicate ids.
    println("part1: ${solvePart1(ranges)}")
    println("part2: ${solvePart2(ranges)}")
}
