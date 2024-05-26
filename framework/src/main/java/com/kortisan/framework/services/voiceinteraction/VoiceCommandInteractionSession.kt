package com.kortisan.framework.services.voiceinteraction

import android.content.Context
import android.os.Bundle
import android.service.voice.VoiceInteractionSession

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 06/01/23.
 * * * * * * * * * * **/
class VoiceCommandInteractionSession( context: Context ): VoiceInteractionSession( context ) {
    override fun onShow(args: Bundle?, showFlags: Int) {
        super.onShow(args, showFlags)
    }
}