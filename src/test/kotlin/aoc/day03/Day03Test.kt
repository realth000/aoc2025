package aoc.day03

import kotlin.test.Test
import kotlin.test.assertEquals

const val DUMMY_INPUT = "987654321111111\n811111111111119\n234234234234278\n818181911112111"

class Day03Test {
    @Test
    fun solveTest() {
        val banks = DUMMY_INPUT.split("\n").map { Bank.parseText(it) }.toList()
        assertEquals(357, solvePart1(banks))
        assertEquals(357_uL, solvePart2(banks, 2))
        // assertEquals(3121910778619_uL, solvePart2(banks))
    }
}

