package aoc.day06

import kotlin.streams.toList

enum class Operator {
    Unknown,
    Add,
    Multiply
}

data class Statement(var op: Operator, val values: MutableList<ULong>) {
    fun calculate(): ULong = when (op) {
        Operator.Unknown -> throw Exception("Execpted unknown operator in calculation")
        Operator.Add -> values.sum()
        Operator.Multiply -> values.fold(1uL) { acc, it -> acc * it }
    }
}

fun emptyStatement(): Statement = Statement(Operator.Unknown, mutableListOf())

fun parseStatements(input: String): List<Statement> {
    val lines = input.split("\n").map { it.trim() }.asReversed()
    val stmtCount = lines[0].split(Regex(" +")).size
    val stmts = MutableList(stmtCount) { emptyStatement() }

    val opLine = lines[0]
    for ((idx, op) in opLine.split(Regex(" +")).withIndex()) {
        stmts[idx].op = when (op) {
            "+" -> Operator.Add
            "*" -> Operator.Multiply
            else -> throw Exception("Unknown operator $op")
        }
    }

    for (line in lines.drop(1)) {
        for ((idx, v) in line.split(Regex(" +")).withIndex()) {
            stmts[idx].values.add(v.toULong())
        }
    }

    return stmts
}

fun solvePart1(stmts: List<Statement>): ULong = stmts.sumOf { it.calculate() }


fun parseStatementsV2(input: String): List<Statement> {
    val lines = input.split("\n").toMutableList()
    val opLine = lines.removeLast().chars().toList().map { it.toChar() }.reversed()
    val valueLines = lines.map { line -> line.chars().toList().reversed().map { ch -> ch.toChar() } }
    val width = lines[0].length
    val stmts = mutableListOf<Statement>()

    var stmt = emptyStatement()
    for (i in opLine.indices) {
        if (valueLines.all { it[i] == ' ' }) {
            // Skip empty line.
            continue
        }

        var value = 0
        for (l in valueLines) {
            if (l[i] != ' ') {
                if (value == 0) {
                    value = l[i].digitToInt()
                } else {
                    value *= 10
                    value += l[i].digitToInt()
                }
            }
        }
        stmt.values.add(value.toULong())

        if (opLine[i] != ' ') {
            stmt.op = when (opLine[i]) {
                '+' -> Operator.Add
                '*' -> Operator.Multiply
                else -> throw Exception("Unknown operator ${opLine[i]}")
            }

            stmts.add(stmt)
            stmt = emptyStatement()
        }
    }

    return stmts
}

fun solvePart2(stmts: List<Statement>): ULong = stmts.sumOf { it.calculate() }

fun main() {
    println("part1: ${solvePart1(parseStatements(aoc.Utils.readInput(6)))}")
    println("part2: ${solvePart2(parseStatementsV2(aoc.Utils.readInput(6)))}")
}