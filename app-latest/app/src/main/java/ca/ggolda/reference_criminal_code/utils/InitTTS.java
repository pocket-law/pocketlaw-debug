package ca.ggolda.reference_criminal_code.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import ca.ggolda.reference_criminal_code.R;
import java.util.Locale;





public class InitTTS {

    private TextToSpeech textToSpeech;

    private static Context mContext;

    private static InitTTS mInitTTS;

    private String DATABASE_VERSION = "";

    public static synchronized InitTTS getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        mContext = context;



        if (mInitTTS == null) {
            mInitTTS = new InitTTS(context.getApplicationContext());
        }
        return mInitTTS;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private InitTTS(Context context) {
        super();
        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(mContext.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}