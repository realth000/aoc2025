package aoc.day04

import kotlin.test.Test
import kotlin.test.assertEquals

val DUMMY_INPUT = """
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
""".trimIndent()

class DayTest {
    @Test
    fun solveTest() {
        val world = World.parseText(DUMMY_INPUT)
        assertEquals(13, solvePart1(world))
        assertEquals(43, solvePart2(world))
    }
}

