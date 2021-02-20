package edu.vt.cs.cs5254.criminalintent

import android.icu.text.CaseMap
import java.util.*

data class Crime(val id: UUID = UUID.randomUUID(), var title: String = "", var date: Date=Date(), var isSolved:Boolean = false)