package dk.rbyte.sheetleafapp.data

class WebServerPointer {
    companion object {
        private const val useLocal = true
        //private const val local = "http://10.209.232.32:8080"
        private const val local = "http://192.168.0.105:8080"
        private const val web = "https://sheetleaf.herokuapp.com/"
        @JvmStatic
        fun getBaseURL() : String {
            if (useLocal) return local
            return web
        };
    }
}
