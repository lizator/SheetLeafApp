package dk.rbyte.sheetleafapp.data.profile

import org.junit.Assert.*

import org.junit.Test

class ProfileRepositoryTest {

    @Test
    fun testConnection() {
        val p = ProfileRepository()
        p.testConnection()
    }
}