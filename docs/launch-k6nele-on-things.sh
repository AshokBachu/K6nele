#!/bin/sh

# Example of launching Kõnele using 'adb shell am' optimized for Android Things.
# First set up Android Things on a Raspberry Pi3 so that you can access it via adb
# by "adb connect Android.local",
# see https://developer.android.com/things/hardware/raspberrypi.html
#
# Setting up Kõnele on the device:
# 1. adb install /path/to/K6nele-x.y.zz.apk
# 2. adb reboot (this grants the permissions)
# 3. TODO: import some rewrite rules from a URL (needs extension to Kõnele)
#    (or use adb to write a rewrites table into the local storage)
# 4. TODO: remove "Tap&Speak" + mic button (which do not make sense for a notouch device)
#    (using adb to modify the preferences + extending Kõnele not to refer to touch in the UI)
# 5. ...
#
# Usage:
#
# 1. Launch this script, i.e. launch-k6nele-on-things.sh
# 2. Speak the name of a song/artist into the device
# 3. (TODO) Some music player on the device starts playing the given song/artist
# Note that the service/locale must be among the selected combos, or the default combo.
#
# Shutdown:
#
# adb shell reboot -p

component="ee.ioc.phon.android.speak/.service.WebSocketRecognitionService"

language="et-EE"

# TODO: this fails on Android Things
# Music player intent
#intent='{
#\"action\": \"android.media.action.MEDIA_PLAY_FROM_SEARCH\",
#\"extras\": {
#    \"android.intent.extra.focus\": \"vnd.android.cursor.item/*\",
#    \"query\": \"\$1\"
#    }
#}'

# TODO: For the time being, just relaunch Kõnele to show the transcription
intent='{
\"action\": \"android.speech.action.RECOGNIZE_SPEECH\",
\"component\": \"ee.ioc.phon.android.speak/.activity.SpeechActionActivity\",
\"extras\": {
    \"android.speech.extra.PROMPT\": \"RESULT: \$1\"
    }
}'

# TODO: VOICE_PROMPT is excluded because triggers Google TTS (which resulted in a failed network connection)
# TODO: install an offline TTS, e.g. EKI TTS
# -e ee.ioc.phon.android.extra.VOICE_PROMPT "Öelge laulu või muusiku nimi" \

# TODO: AUDIO_CUES does not work

adb shell am force-stop ee.ioc.phon.android.speak;

adb shell 'am start \
-n ee.ioc.phon.android.speak/.activity.SpeechActionActivity \
-e android.speech.extra.LANGUAGE_MODEL "free_form" \
-e android.speech.extra.LANGUAGE "'$language'" \
-e ee.ioc.phon.android.extra.SERVICE_COMPONENT "'$component'" \
-e android.speech.extra.PROMPT "Say the name of a song or an artist" \
--ez ee.ioc.phon.android.extra.AUTO_START true \
--ez ee.ioc.phon.android.extra.AUDIO_CUES true \
--ei android.speech.extra.MAX_RESULTS 1 \
--ez ee.ioc.phon.android.extra.RESULT_LAUNCH_AS_ACTIVITY true \
-e ee.ioc.phon.android.extra.RESULT_ARG1 "'$intent'"'
