package com.recepyesilkaya.arabam.data.model

data class Filter(
    var minYear: Int?,
    var maxYear: Int?,
    var category: Int?,
    var minDate: String?,
    var maxDate: String?
)