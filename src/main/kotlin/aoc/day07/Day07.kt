package aoc.day07

import kotlin.streams.toList

enum class Item {
    // .
    Empty,

    // S
    Source,

    // ^
    Splitter,

    // |
    Beam;

    fun isSplitter(): Boolean = this == Splitter
    fun hasBeam(): Boolean = this == Beam || this == Source
    fun isEmpty(): Boolean = this == Empty

    companion object {
        fun parseChar(ch: Char): Item = when (ch) {
            '.' -> Empty
            'S' -> Source
            '^' -> Splitter
            '|' -> Beam
            else -> throw Exception("Invalid item char $ch") // Unreachable
        }
    }
}

typealias Line = MutableList<Item>
typealias Grid = MutableList<Line>

class World(
    var grid: Grid,
    val width: Int,
    var round: Int,
    val maxRound: Int,
    var splitCount: Int,
    val pathList: MutableList<List<Int>>
) {
    companion object {
        fun parseText(input: String): World {
            val lines = input
                .split("\n")
                .map { it.chars().toList().map { ch -> Item.parseChar(ch.toChar()) }.toMutableList() }
                .toMutableList()
            val width = lines[0].size
            val maxRound = lines.size - 1
            val paths = mutableListOf<List<Int>>()
            val l0 = MutableList(width) { 0 }
            l0[lines[0].indexOf(Item.Source)] = 1
            paths.add(l0)
            return World(lines, width, 0, maxRound, 0, paths)
        }
    }

    fun nextRound(): Boolean {
        if (round >= maxRound) {
            return false
        }
        val (roundSplitCount, newLine) = splitTo(grid[round], grid[round + 1])
        grid[round + 1] = newLine
        splitCount += roundSplitCount
        round += 1

        return true
    }

    private fun splitTo(prev: Line, curr: Line): Pair<Int, Line> {
        var splitCount = 0
        val currLine = curr.toMutableList()
        val currLinePaths = MutableList(width) { 0 }
        for (pos in 0..<width) {
            if (pathList[round][pos] > 0) {
                if (curr[pos].isSplitter()) {
                    splitCount += 1
                    // Split
                    if (pos >= 1 && !curr[pos - 1].isSplitter()) {
                        // Split left.
                        currLine[pos - 1] = Item.Beam
                        currLinePaths[pos - 1] += pathList[round][pos]
                    }
                    if (pos < width - 1 && !curr[pos + 1].isSplitter()) {
                        // Split right.
                        currLine[pos + 1] = Item.Beam
                        currLinePaths[pos + 1] += pathList[round][pos]
                    }
                } else {
                    // Forward only
                    currLine[pos] = Item.Beam
                    currLinePaths[pos] += pathList[round][pos]
                }
            }
        }
        pathList.add(currLinePaths)
        return Pair(splitCount, currLine)
    }

    fun countPath(): ULong = pathList.last().sumOf { it.toULong() }

    private fun getPath(pos: Int): List<List<Int>> =
        pathList.filter { it.size == round && it.last() == pos }.toList()
}

fun solvePart1(world: World): Int {
    while (world.nextRound()) {
        // Next round.
    }
    return world.splitCount
}

fun solvePart2(world: World): ULong {
    while (world.nextRound()) {
        // Next round.
    }
    val count = world.countPath()
    return count
}

fun main() {
    println("part1: ${solvePart1(World.parseText(aoc.Utils.readInput(7)))}")
    println("part2: ${solvePart2(World.parseText(aoc.Utils.readInput(7)))}")
}
