package aoc.day01

import kotlin.test.Test
import kotlin.test.assertEquals

val DUMMY_INPUT = """
   L68
   L30
   R48
   L5
   R60
   L55
   L1
   L99
   R14
   L82
""".trimIndent()

class Day01Test {
    @Test
    fun solveTest() {
        val rotations = DUMMY_INPUT.split("\n").map { Rotation.parseText(it) }.toList()
        assertEquals(3, solvePart1(rotations))
        assertEquals(6u, solvePart2(rotations))
    }
}
