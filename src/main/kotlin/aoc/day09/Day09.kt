package aoc.day09

import kotlin.math.absoluteValue

data class Point(val x: Long, val y: Long) {
    companion object {
        fun parseText(data: String): Point {
            val s = data.split(",").map { it.toLong() }
            return Point(s[0], s[1])
        }

        fun calcArea(p1: Point, p2: Point): Long = ((p1.x - p2.x).absoluteValue + 1) * ((p1.y - p2.y).absoluteValue + 1)
    }
}

fun <T> generatePairs(items: List<T>): List<Pair<T, T>> {
    val pairs = mutableListOf<Pair<T, T>>()
    for (i in items.indices) {
        for (j in (i + 1)..<items.size) {
            pairs.add(items[i] to items[j])
        }
    }
    return pairs
}

fun solvePart1(points: List<Point>): Long =
    generatePairs(points).map { (p1, p2) -> Point.calcArea(p1, p2) }.maxOf { it }

/**
 * 1. Pair all # points that are in the same row or column that connected (with even index).
 * 2. For each regtangle formed by pair of # points:
 *    Find the two opposite points, these points MUST ALL satisfy at least one of the following requirements:
 *     Set the current point we figuring is (x,y)。
 * 	1. Is # point.
 * 	2. In vertical direction, satisfy at least one of the following requirements:
 * 		1. Exists two # points (x1,y1) and (x2,y2) that forms a vertical line go through it:
 * 			x1=x2, x=x1, y is between y1 and y2, and y!=y1, y!=y2
 * 		2. Exists four # points (x1,y1) and (x2,y2), (x3,y3) and (x4,y4) that forms two lines on the left and right side of the point:
 * 			Assume the former line on the left and later line on the right, then:
 * 			x1=x2, x1<x, y is between y1 and y2, and y!=y1, y!=y2
 * 			x3=x4, x3>x, y is between y1 and y2, and y!=y1, y!=y2
 * 	3. In horizontal direction, satisfy at least one of the following requirements:
 * 		1. Exists two # points (x1,y1) and (x2,y2) that forms a horizontal line go through it:
 * 			y1=y2, y=y1, x is between x1 and x2, and x!=x1, x!=x2
 * 		2. Exists four # points (x1,y1) and (x2,y2), (x3,y3) and (x4,y4) that forms two lines on the top and bottom side of the point:
 * 			Assume the former line on the top and later line on the bottom, then:
 * 			y1=y2, y1<y, x is between x1 and x2, and x!=x1, x!=x2
 * 			y3=y4, y3>y, x is between x1 and x2, and x!=x1, x!=x2
 */
fun solvePart2(points: List<Point>): Long {
    val pairs = generatePairs(points)
    fun filterRedGreenRegtangles(pointPair: Pair<Point, Point>): Boolean {
        val horizontalLinesMap = buildMap<Long, List<Pair<Long, Long>>> {
            pairs
                .filter { it.first.y == it.second.y }
                .groupBy { it.first.y }
                .map { entry ->
                    val lines = entry
                        .value
                        .withIndex()
                        .partition { (index, _) -> index % 2 == 0 }
                        .first
                        .toList()
                        .map { (_, line) ->
                            if (line.first.x <= line.second.x) {
                                line.first.x to line.second.x
                            } else {
                                line.second.x to line.first.x
                            }
                        }
                    set(entry.key, lines)
                }
        }

        val verticalLinesMap = buildMap<Long, List<Pair<Long, Long>>> {
            pairs
                .filter { it.first.x == it.second.x }
                .groupBy { it.first.x }
                .map { entry ->
                    val lines = entry
                        .value
                        .withIndex()
                        .partition { (index, _) -> index % 2 == 0 }
                        .first
                        .toList()
                        .map { (_, line) ->
                            if (line.first.y <= line.second.y) {
                                line.first.y to line.second.y
                            } else {
                                line.second.y to line.first.y
                            }
                        }
                    set(entry.key, lines)
                }
        }

        fun isPointGreenOrReg(p: Point): Boolean {
            if (points.contains(p)) {
                return true
            }

            // Check vertical direction:
            val currVerticalLines = verticalLinesMap[p.x]
            if (currVerticalLines != null && currVerticalLines.any { it.first < p.y && p.y < it.second }) {
                // In line.
                return true
            }
            val (leftLines, rightLines) = verticalLinesMap.entries.partition { it.key < p.x }
            if (leftLines.any { l -> l.value.any { it.first <= p.y && p.y <= it.second } }
                && rightLines.any { l -> l.value.any { it.first <= p.y && p.y <= it.second } }) {
                return true
            }

            // Check horizontal direction:
            val currHorizontalLines = horizontalLinesMap[p.y]
            if (currHorizontalLines != null && currHorizontalLines.any { it.first < p.x && p.x < it.second }) {
                // In line.
                return true
            }
            val (topLines, bottomLines) = horizontalLinesMap.entries.partition { it.key < p.y }
            if (topLines.any { l -> l.value.any { it.first <= p.x && p.x <= it.second } }
                && bottomLines.any { l -> l.value.any { it.first <= p.x && p.x <= it.second } }) {
                return true
            }

            return false
        }

        // Opposite points are red or green:
        return listOf(Point(pointPair.first.x, pointPair.second.y), Point(pointPair.second.x, pointPair.first.y))
            .all { isPointGreenOrReg(it) }
    }

    return pairs.filter { filterRedGreenRegtangles(it) }.map { (p1, p2) ->
        val area = Point.calcArea(p1, p2)
        println(">>> p1=$p1, p2=$p2, area=$area")
        area
    }.maxOf { it }
}

fun main() {
    val points = aoc.Utils.readInput(9).split("\n").map { Point.parseText(it) }
    println("part1: ${solvePart1(points)}")
    // 4604141472 too large
    println("part2: ${solvePart2(points)}")
}
