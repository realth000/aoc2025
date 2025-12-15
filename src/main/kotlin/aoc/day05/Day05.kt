package aoc.day05

import kotlin.math.max
import kotlin.math.min

typealias Id = ULong
typealias IdList = List<Id>

data class Range(var start: Id, var end: Id) {
    fun compose(other: Range): Boolean {
        if (this.end < other.start || other.end < this.start) {
            return false
        }

        this.start = min(this.start, other.start)
        this.end = max(this.end, other.end)
        return true
    }
}

typealias RangeList = List<Range>


fun parseRanges(input: String): RangeList = input.split("\n").map {
    val rs = it.split("-")
    Range(rs[0].toULong(), rs[1].toULong())
}.toList()


fun parseIds(input: String): IdList = input.split("\n").map { it.toULong() }.toList()


fun solvePart1(ranges: RangeList, ids: IdList): Int =
    ids.count { id -> ranges.any { range -> range.start <= id && id <= range.end } }

fun solvePart2(ranges: RangeList): ULong {
    val tmp: MutableList<Range> = mutableListOf()
    for (range in ranges) {
        if (tmp.any { it.compose(range) }) {
            continue
        } else {
            tmp.add(range)
        }
    }

    round@ while (true) {
        // Check overlap
        for (idx in 0..<(tmp.size - 1)) {
            for (idx2 in (idx + 1)..<tmp.size) {
                if (tmp[idx].compose(tmp[idx2])) {
                    tmp.removeAt(idx2)
                    continue@round
                }
            }
        }
        break
    }

    return tmp.sumOf { it.end - it.start + 1_uL }
}

fun main() {
    val data = aoc.Utils.readInput(5).split("\n\n")
    val ranges = parseRanges(data[0])
    val ids = parseIds(data[1])
    println("part1: ${solvePart1(ranges, ids)}")
    println("part2: ${solvePart2(ranges)}")
}
