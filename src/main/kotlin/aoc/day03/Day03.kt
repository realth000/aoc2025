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
    fun getIncreasingSeqLength(arr: Array<Int?>, incoming: Int): Int {
        val w = arr.toList().windowed(2, 1).reversed()
        var count = 0;
        for ((prev, next) in w) {
            if (prev!! < next!! && next < incoming) {
                count += 1
            } else {
                break
            }
        }
        return count
    }

    fun Array<Int?>.partialShiftLeft(step: Int, incoming: Int) {
        val step = if (step > this.size) {
            this.size
        } else {
            step
        }
        // [x, x, x, x , x]
        // step = 1, incoming = 100
        // [x, x, x, x, 100]
        var shiftCurrIdx = this.size - step
        while (true) {
            if (shiftCurrIdx < this.size - 1) {
                // Has next.
                this[shiftCurrIdx] = this[shiftCurrIdx + 1]
                shiftCurrIdx += 1
            } else if (shiftCurrIdx == this.size - 1) {
                // At the end of array.
                this[shiftCurrIdx] = incoming
                break
            } else {
                // Out of range.
                throw Exception("Unreachable")
            }
        }
    }

    fun maxJoltage(bank: Bank, size: Int): ULong {
        var hs = Array<Int?>(size) { null }
        for (i in bank.batteries) {
            val firstNullIdx = hs.indexOf(null)
            if (firstNullIdx >= 0) {
                hs[firstNullIdx] = i
                continue
            }

            // [1, 2, 5, 4, 3] 6
            // -> ? [1, 2, 5, 4, 6]

            // All hs not null since here.

            // How many elements at tail need to shift left.
            val seqShiftLength = getIncreasingSeqLength(hs, i)
            println("hs=${hs.contentToString()} , seqShiftLength=$seqShiftLength")

            if (hs.last()!! >= i && seqShiftLength < 1) {
                continue
            }

            println(">>> shift: $seqShiftLength for i=$i")
            if (seqShiftLength > 0) {
                println(">>> shift? ${hs.contentToString()}")
                hs.partialShiftLeft(seqShiftLength, i)
                println(">>> shift! ${hs.contentToString()}")
            }
        }

        println(">>> jolt!: ${hs.contentToString()}")
        return hs.filterNotNull().reversed()
            .mapIndexed { idx: Int, value: Int -> value.toULong() * 10.toDouble().pow(idx).toULong() }.sum()
    }

    return banks.sumOf { maxJoltage(it, size) }
}

fun main() {
    val banks = aoc.Utils.readInput(3).split("\n").map { Bank.parseText(it) }.toList()
    println("part1: ${solvePart1(banks)}")
}
