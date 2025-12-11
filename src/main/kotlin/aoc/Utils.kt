package aoc

object Utils {
    fun readInput(dayId: Int): String {
        return Utils::class.java.getResource("/day${String.format("%02d", dayId)}.txt")!!.readText()
    }
}