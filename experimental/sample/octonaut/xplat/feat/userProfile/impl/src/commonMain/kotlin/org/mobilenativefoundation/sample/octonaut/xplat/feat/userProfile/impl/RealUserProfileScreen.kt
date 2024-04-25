package org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.impl

import org.mobilenativefoundation.sample.octonaut.xplat.feat.userProfile.api.UserProfileScreen

data class RealUserProfileScreen(
    override val login: String
) : UserProfileScreen