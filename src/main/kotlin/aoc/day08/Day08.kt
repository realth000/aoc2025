package aoc.day08

data class Point(val x: ULong, val y: ULong, val z: ULong) : Comparable<Point> {
    companion object {
        fun parseText(line: String): Point {
            val ds = line.split(",").map { it.toULong() }.toList()
            return Point(ds[0], ds[1], ds[2])
        }
    }

    override fun compareTo(other: Point): Int = compareValuesBy(
        this,
        other,
        { it.x },
        { it.y },
        { it.z },
    )
}

data class Distance(val distance: ULong, val p1: Point, val p2: Point) {
    companion object {
        fun between(p1: Point, p2: Point): Distance {
            val dx = p1.x.toLong() - p2.x.toLong()
            val dy = p1.y.toLong() - p2.y.toLong()
            val dz = p1.z.toLong() - p2.z.toLong()
            val distance = dx * dx + dy * dy + dz * dz
            val (s1, s2) = if (p1 < p2) {
                p1 to p2
            } else {
                p2 to p1
            }
            return Distance(distance.toULong(), s1, s2)
        }
    }
}

class UnionFind<T>(points: List<T>) {
    private val parent = mutableMapOf<T, T>()
    private val size = mutableMapOf<T, Int>()

    init {
        for (p in points) {
            parent[p] = p
            size[p] = 1
        }
    }

    fun find(p: T): T {
        if (parent[p] != p) {
            parent[p] = find(parent[p]!!)
        }
        return parent[p]!!
    }


    fun union(p1: T, p2: T): Boolean {
        val r1 = find(p1)
        val r2 = find(p2)
        if (r1 == r2) {
            return false
        }

        if (size[r1]!! < size[r2]!!) {
            parent[r1] = r2
            size[r2] = size[r2]!! + size[r1]!!
        } else {
            parent[r2] = r1
            size[r1] = size[r1]!! + size[r2]!!
        }
        return true
    }

    fun getRootSizes(): List<Int> {
        val roots = mutableSetOf<T>()
        val rootSizes = mutableListOf<Int>()
        for (p in parent.keys) {
            val r = find(p)
            if (roots.add(r)) {
                rootSizes.add(size[r]!!)
            }
        }
        return rootSizes.sortedDescending()
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

fun solvePart1(points: List<Point>, round: Int): ULong {
    val distances = generatePairs(points)
        .map { (p1, p2) -> Distance.between(p1, p2) }
        .sortedBy { it.distance }

    val unionFind = UnionFind(points)
    var connectionsCount = 0

    for (d in distances) {
        if (connectionsCount >= round) {
            break
        }
        // PART 1:
        //
        // When counting connections, count all attemps no matter new established or not.
        // **QUESTION DESCRIPTION SUCKS**
        // if (unionFind.union(d.p1, d.p2)) {
        //     connectionsCount += 1
        // }
        unionFind.union(d.p1, d.p2)
        connectionsCount += 1
    }

    return unionFind.getRootSizes().take(3).fold(1uL) { acc, size -> acc * size.toULong() }
}

fun solvePart2(points: List<Point>, round: Int): ULong {
    val distances = generatePairs(points)
        .map { (p1, p2) -> Distance.between(p1, p2) }
        .sortedBy { it.distance }

    val unionFind = UnionFind(points)
    var connectionsCount = 0
    var lastDistance : Distance? = null

    for (d in distances) {
        if (connectionsCount >= round) {
            break
        }
        if (unionFind.union(d.p1, d.p2)) {
            lastDistance = d
        }
    }

    return lastDistance!!.p1.x.toULong() * lastDistance!!.p2.x.toULong()
}

fun main() {
    val points = aoc.Utils.readInput(8).split('\n').map { Point.parseText(it) }.toList()
    println("part1: ${solvePart1(points, 1000)}")
    println("part2: ${solvePart2(points, 1000)}")
}
