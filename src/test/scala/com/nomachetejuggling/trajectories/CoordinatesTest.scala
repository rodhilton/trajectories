package com.nomachetejuggling.trajectories

import org.junit.Test
import org.junit.Assert._

class CoordinatesTest {

    @Test
    def shouldCalculateBoundaries() {
        //These look weird, but it's because we cound up from the left, but also up from the bottom.
        assertEquals(('c', 2), ('b', 3) + 2)
        assertEquals(('b', 3), ('b', 3) + 1)
        assertEquals(('d', 1), ('b', 3) + 3)
    }

    @Test
    def canOffset() {
        assertEquals(('d', 8), ('f',5) + (-2, 3))
    }
}
