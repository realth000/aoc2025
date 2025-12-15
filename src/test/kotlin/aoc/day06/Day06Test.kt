package aoc.day06

import kotlin.test.Test
import kotlin.test.assertEquals

const val DUMMY_INPUT = """123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  """

class DayTest {
    @Test
    fun solveTest() {
        val stmts = parseStatements(DUMMY_INPUT)
        assertEquals(4277556uL, solvePart1(stmts))
        val stmtsP2 = parseStatementsV2(DUMMY_INPUT)
        assertEquals(3263827uL, solvePart2(stmtsP2))
    }
}