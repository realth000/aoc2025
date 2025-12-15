package aoc.day04

import kotlin.streams.toList

data class World(val grid: MutableList<MutableList<Char>>, val width: Int, val height: Int) {
    companion object {
        fun parseText(data: String): World {
            val rows = data.split("\n")
            val width = rows[0].chars().toList().size
            val height = rows.size

            val grid: MutableList<MutableList<Char>> = mutableListOf()
            for (row in rows) {
                grid.add(row.chars().toList().map { it.toChar() }.toMutableList())
            }

            return World(grid, width, height)
        }
    }
}

data class Position(var x: Int, var y: Int, val world: World) {
    fun reset() {
        x = 0
        y = 0
    }

    fun curr(): Char? = if (0 <= x && x <= world.width - 1 && 0 <= y && y <= world.height - 1) {
        world.grid[y][x]
    } else {
        null
    }

    fun markRemove() {
        world.grid[y][x] = 'x'
    }

    fun next() {
        if (x >= world.width - 1) {
            x = 0
            y += 1
        } else {
            x += 1
        }
    }

    fun surroundings(): List<Char> = buildList {
        if (y != 0 && x != 0) {
            add(world.grid[y - 1][x - 1])
        }
        if (y != 0) {
            add(world.grid[y - 1][x])
        }
        if (y != 0 && x != world.width - 1) {
            add(world.grid[y - 1][x + 1])
        }

        if (x != 0) {
            add(world.grid[y][x - 1])
        }
        if (x != world.width - 1) {
            add(world.grid[y][x + 1])
        }

        if (y != world.height - 1 && x != 0) {
            add(world.grid[y + 1][x - 1])
        }
        if (y != world.height - 1) {
            add(world.grid[y + 1][x])
        }
        if (y != world.height - 1 && x != world.width - 1) {
            add(world.grid[y + 1][x + 1])
        }
    }
}

fun solvePart1(world: World): Int {
    var count = 0
    var pos = Position(0, 0, world)
    while (pos.curr() != null) {
        if (pos.curr() == '@') {
            val surroundingPapers = pos.surroundings().count { it == '@' }
            if (surroundingPapers < 4) {
                count += 1
            }
        }
        pos.next()
    }
    return count
}


fun solvePart2(world: World): Int {
    var count = 0
    var oldCount = 0
    var pos = Position(0, 0, world)
    while (true) {
        while (pos.curr() != null) {
            if (pos.curr() == '@') {
                val surroundingPapers = pos.surroundings().count { it == '@' }
                if (surroundingPapers < 4) {
                    pos.markRemove()
                    count += 1
                }
            }
            pos.next()
        }
        if (count == oldCount) {
            break
        }
        oldCount = count
        pos.reset()
    }
    return count
}


fun main() {
    val world = World.parseText(aoc.Utils.readInput(4))
    println("part1: ${solvePart1(world)}")
    println("part2: ${solvePart2(world)}")
}
