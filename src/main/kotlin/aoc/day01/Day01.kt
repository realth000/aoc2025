package aoc.day01

enum class Direction {
    UP,
    DOWN,
}

data class Rotation(val direction: Direction, val step: UInt) {
    companion object {
        fun parseText(data: String): Rotation {
            val v = data.trim()
            val direction = when (v[0]) {
                'L' -> Direction.DOWN
                'R' -> Direction.UP
                else -> throw Exception("Invalid direction ${v[0]}")
            }
            val distance = v.substring(1).toUInt()

            return Rotation(direction, distance)
        }
    }
}

fun move(pos: UInt, rotation: Rotation): UInt {
    val step = rotation.step % 100u
    return when (rotation.direction) {
        Direction.UP -> (pos + step) % 100u
        Direction.DOWN -> if (pos > step) {
            pos - step
        } else if (pos < step) {
            pos + 100u - step
        } else {
            0u
        }
    }
}


fun move2(pos: UInt, rotation: Rotation): Pair<UInt, UInt> {
    var rounds = rotation.step / 100u
    val step = rotation.step % 100u
    val pos = when (rotation.direction) {
        Direction.UP -> {
            rounds += if (pos + step >= 100u) {
                1u
            } else {
                0u
            }
            (pos + step) % 100u
        }

        Direction.DOWN -> if (pos > step) {
            pos - step
        } else if (pos < step) {
            if (pos != 0u) {
                // Do NOT record we pass 0 if the starting position is already 0.
                rounds += 1u
            }
            pos + 100u - step
        } else {
            rounds += 1u
            0u
        }
    }

    return Pair(pos, rounds)
}

fun solvePart1(rotations: List<Rotation>): Int {
    var pos = 50u
    var count = 0
    for (r in rotations) {
        pos = move(pos, r)
        if (pos == 0u) {
            count += 1
        }
    }
    return count
}

fun solvePart2(rotations: List<Rotation>): UInt {
    var pos = 50u
    var count = 0u
    for (r in rotations) {
        val result = move2(pos, r)
        pos = result.first
        count += result.second
    }
    return count
}

fun main() {
    val rotations = aoc.Utils.readInput(1).split("\n").map { Rotation.parseText(it) }.toList()
    val count1 = solvePart1(rotations)
    println(count1)
    val count2 = solvePart2(rotations)
    println(count2)
}
