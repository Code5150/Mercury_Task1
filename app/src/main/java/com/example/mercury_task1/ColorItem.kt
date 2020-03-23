package com.example.mercury_task1

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorItem (val color: Int, val label: String, val circleVisible: Boolean): Parcelable

