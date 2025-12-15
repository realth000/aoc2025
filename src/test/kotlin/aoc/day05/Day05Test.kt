package aoc.day05

import kotlin.test.Test
import kotlin.test.assertEquals

val DUMMY_INPUT = """
3-5
10-14
16-20
12-18

1
5
8
11
17
32
""".trimIndent()

class DayTest {
    @Test
    fun solveTest() {
        val data = DUMMY_INPUT.split("\n\n")
        val ranges = parseRanges(data[0])
        val ids = parseIds(data[1])
        assertEquals(3, solvePart1(ranges, ids))
        assertEquals(14_uL, solvePart2(ranges))
    }
}

