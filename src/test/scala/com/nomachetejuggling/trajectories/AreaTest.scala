package com.nomachetejuggling.trajectories

import org.junit.Test
import org.junit.Assert._
import scala.Tuple2

class AreaTest {

    @Test
    def shouldCheckForBoundaryRestrictions() {
        val partial = (('b', 3) to('c', 2)).contains(_: Tuple2[Char, Int])

        assertFalse(partial(('a', 4)))
        assertFalse(partial(('a', 3)))
        assertFalse(partial(('a', 2)))
        assertFalse(partial(('a', 1)))

        assertFalse(partial(('b', 4)))
        assertTrue(partial(('b', 3)))
        assertTrue(partial(('b', 2)))
        assertFalse(partial(('b', 1)))

        assertFalse(partial(('c', 4)))
        assertTrue(partial(('c', 3)))
        assertTrue(partial(('c', 2)))
        assertFalse(partial(('c', 1)))

        assertFalse(partial(('d', 4)))
        assertFalse(partial(('d', 3)))
        assertFalse(partial(('d', 2)))
        assertFalse(partial(('d', 1)))
    }
}
