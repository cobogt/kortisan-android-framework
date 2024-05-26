package com.kortisan.framework.services.voiceinteraction

import android.service.voice.VoiceInteractionService

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 06/01/23.
 * * * * * * * * * * **/
class VoiceCommandInteractionService: VoiceInteractionService() {
    override fun onReady() {
        super.onReady()
    }

    override fun onGetSupportedVoiceActions(voiceActions: MutableSet<String>): MutableSet<String> {
        return super.onGetSupportedVoiceActions(voiceActions)
    }

}