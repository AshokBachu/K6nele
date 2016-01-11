Kõnele
======

[![Codacy Badge](https://api.codacy.com/project/badge/grade/b15968aa0a524d2986ba5deac6801196)](https://www.codacy.com/app/kaljurand/K6nele)

Kõnele is an Android app that offers speech-to-text services to other apps.
Its main components are:

  - [RecognizerIntent](http://developer.android.com/reference/android/speech/RecognizerIntent.html) activity
  - 2 implementations of [SpeechRecognizer](http://developer.android.com/reference/android/speech/SpeechRecognizer.html)
    - one uses <https://github.com/alumae/kaldi-gstreamer-server>
    - the other uses <https://github.com/alumae/ruby-pocketsphinx-server>
  - speech keyboard that implements the [input method editor (IME) API](http://developer.android.com/reference/android/inputmethodservice/InputMethodService.html)

![Components](https://rawgithub.com/Kaljurand/K6nele/master/docs/components.dot.svg)

The main goals of this project is to offer support for Estonian speech recognition on the
Android platform, as well as grammar-based speech recognition for voice command applications.

How to add support for other languages than Estonian is detailed in https://github.com/Kaljurand/K6nele/issues/38

For more information see <http://kaljurand.github.io/K6nele/>.


Compiling
---------

Clone the source code including the `net-speech-api` and `speechutils` submodules:

    git clone --recursive git@github.com:Kaljurand/K6nele.git


Point to the Android SDK directory by setting the environment variable
`ANDROID_HOME`, e.g.

    ANDROID_HOME=${HOME}/myapps/android-sdk/


Build the Kõnele app

    gradle assembleRelease


If you have access to a release keystore then

  - point to its location by setting the environment variable `KEYSTORE`
  - set `KEY_ALIAS` to the key alias
  - create the file `gradle.properties` containing the lines:

        storePassword=<password1>
        keyPassword=<password2>


The (signed and unsigned) APKs will be generated into `app/build/outputs/apk/`.


Contributions
-------------

The client for <https://github.com/alumae/kaldi-gstreamer-server>
was originally based on <https://github.com/truongdq54/kaldi-gstreamer-android-client>.

Please read through the [Contributing Guide](CONTRIBUTING.md) before making a pull request.
