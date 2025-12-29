package aoc.day09

import kotlin.test.Test
import kotlin.test.assertEquals

val DUMMY_INPUT = """
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3""".trimIndent()

class DayTest {

    @Test
    fun solveTest() {
        val points = DUMMY_INPUT.split("\n").map { Point.parseText(it) }
        assertEquals(50L, solvePart1(points))
        assertEquals(24L, solvePart2(points))
    }

}
