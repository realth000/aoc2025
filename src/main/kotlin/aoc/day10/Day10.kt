package aoc.day10

typealias Step = List<Boolean>

data class Machine(val lights: List<Boolean>, val buttons: List<List<Int>>, val joltage: List<Int>) {
    companion object {
        fun parseText(data: String): Machine {
            val segments = data.trim().split(' ')
            val lights = segments.first().toCharArray().toList().mapNotNull {
                if (it == '#') {
                    true
                } else if (it == '.') {
                    false
                } else {
                    null
                }
            }
            val joltage = segments.last().trim { it == '{' || it == '}' }.split(',').map { it.toInt() }
            val buttons = segments.subList(1, segments.size - 1).map {
                it.trim { it == '(' || it == ')' }.split(',').map { it.toInt() }
            }
            return Machine(lights, buttons, joltage)
        }
    }
}

fun solvePart1(machines: List<Machine>): Int {
    fun calculateSum(machine: Machine, step: List<Boolean>): Int? {
        val lights = Array(machine.lights.size) { 0 }.toMutableList()
        for ((idx, toggle) in step.withIndex()) {
            if (toggle) {
                for (pos in machine.buttons[idx]) {
                    lights[pos] += 1
                }
            }
        }
        if (lights.map { (it % 2) == 1 } == machine.lights) {
            return step.count { it }
        }
        return null
    }

    fun generateMinSteps(machine: Machine): Int {
        val size = machine.buttons.size
        var maxValue = 1 shl size;
        var count = 0
        for (i in 0 until maxValue) {
            val step = mutableListOf<Boolean>()
            for (j in 0 until size) {
                step.add(((i shr j) and 1) == 1)
            }
            val m = calculateSum(machine, step)
            if (m == null || (count > 0 && m >= count)) {
                continue
            }
            count = m
        }
        return count
    }

    return machines.sumOf { generateMinSteps(it) }
}

fun main() {
    val machines = aoc.Utils.readInput(10).split('\n').map { Machine.parseText(it) }
    print("part1: ${solvePart1(machines)}")
}