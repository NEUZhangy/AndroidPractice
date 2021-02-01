package edu.vt.cs.cs5254.geoquiz

import androidx.annotation.StringRes
import edu.vt.cs.cs5254.multiquiz.Answer

data class Questions (@StringRes val textResId: Int, val answerID : Int)