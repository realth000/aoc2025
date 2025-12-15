package aoc.day03

import kotlin.math.pow

typealias Battery = Int

data class Bank(val batteries: List<Battery>) {
    companion object {
        fun parseText(data: String): Bank = Bank(
            data.trim().toCharArray().map { it.code - 48 }.toList()
        )
    }
}


fun solvePart1(banks: List<Bank>): Int {
    fun maxJoltage(bank: Bank): Int {
        var h1: Int? = null
        var h2: Int? = null
        for (i in bank.batteries) {
            if (h1 == null) {
                h1 = i
                continue
            }
            if (h2 == null) {
                h2 = i
                continue
            }

            if (h2 < i) {
                if (h1 < h2) {
                    // 4, 2, 7, 8
                    // h1 = 4, h2 = 7, i = 8
                    // 42 -> 78
                    h1 = h2
                    h2 = i
                    continue
                } else {
                    h2 = i
                }
                continue
            }

            // h1 != null && h2 != null
            //
            // 8, 1, 9, 1
            // Now: 89, incoming 1 when h1 == 8 < h2 == 9
            // Shift.
            if (h1 < h2) {
                h1 = h2
                h2 = i
                continue
            }
        }
        return h1!! * 10 + h2!!
    }
    return banks.sumOf { maxJoltage(it) }
}

fun solvePart2(banks: List<Bank>, size: Int): ULong {
    fun maxJoltage(bank: Bank, size: Int): ULong {
        var hs = Array<Int?>(size) { null }
        for (i in bank.batteries) {
            val firstNullIdx = hs.indexOf(null)
            if (firstNullIdx >= 0) {
                hs[firstNullIdx] = i
                continue
            }

            // All hs not null since here.

            // Check and shift left.
            var shiftCount: Int? = null
            var p = 0
            while (p < size - 1) {
                if (hs[p]!! < hs[p + 1]!!) {
                    shiftCount = p
                    break
                }
                p += 1
            }

            if (hs.last()!! < i || shiftCount != null) {
                // Shift left.
                if (shiftCount != null) {
                    var pos = shiftCount
                    while (pos <= size - 2) {
                        hs[pos] = hs[pos + 1]
                        pos += 1
                    }
                }

                hs[size - 1] = i
            }
        }

        return hs.filterNotNull().reversed()
            .mapIndexed { idx: Int, value: Int -> value.toULong() * 10.toDouble().pow(idx).toULong() }.sum()
    }

    return banks.sumOf { maxJoltage(it, size) }
}

fun main() {
    val banks = aoc.Utils.readInput(3).split("\n").map { Bank.parseText(it) }.toList()
    println("part1: ${solvePart1(banks)}")
    println("part2: ${solvePart2(banks, 12)}")
}
