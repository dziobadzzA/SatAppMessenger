package com.satellite.messenger.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.satellite.messenger.R

class PreferenceScreen: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey)
    }
}