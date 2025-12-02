package kzs.th000

object AocUtils {
    fun readInput(dayId: Int) : String {
        return AocUtils::class.java.getResource("/day${String.format("%02d", dayId)}.txt")!!.readText()
    }
}