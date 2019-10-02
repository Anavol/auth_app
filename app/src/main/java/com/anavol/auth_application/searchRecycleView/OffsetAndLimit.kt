package com.anavol.auth_application.searchRecycleView

class OffsetAndLimit(val offset: Int, val limit: Int) {

    override fun toString(): String {
        return "OffsetAndLimit{" +
                "offset=" + offset +
                ", limit=" + limit +
                '}'.toString()
    }
}