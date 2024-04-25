package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
actual data class LaunchUserProfileScreen actual constructor(override val login: String) : Parcelable, UserProfileScreen