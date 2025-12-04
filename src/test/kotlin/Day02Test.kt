import kotlin.test.Test
import kotlin.test.assertEquals
import kzs.th000.IdRange
import kzs.th000.solvePart1

const val DUMMY_INPUT_02 =
        "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"

class Day02Test {
    @Test
    fun dumyTest() {
        val ranges = DUMMY_INPUT_02.split(",").map { IdRange.parseText(it) }.toList()
        assertEquals(1227775554_uL, solvePart1(ranges))

        // The default showcase does not handle it.
        val ranges2 = "19-46".split(",").map { IdRange.parseText(it) }.toList()
        assertEquals((22_uL + 33_uL + 44_uL), solvePart1(ranges2))
    }
}

