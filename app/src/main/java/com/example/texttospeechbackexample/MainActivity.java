package com.example.texttospeechbackexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout text;
    private TextView notifier;
    private Button hear,record;
    private TextToSpeech tspeech;
    SpeechRecognizer speechRecognizer;
    Intent recogniserIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text= findViewById(R.id.text);
        notifier=(TextView) findViewById(R.id.notifier);
        hear=(Button) findViewById(R.id.hear);
        record=(Button) findViewById(R.id.record);

        notifier.setText("");
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        recogniserIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recogniserIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recogniserIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> list=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(list!=null)
                {
                    String keeper=null;
                    keeper=list.get(0).toString();
                    notifier.setText(keeper);
                }

            }


            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(recogniserIntent);
                        notifier.setText("Listening");
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        notifier.setText("");
                        break;
                }
                return false;
            }
        });
        tspeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS)
                {
                    int lang=tspeech.setLanguage(Locale.UK);
                    if(lang==tspeech.LANG_MISSING_DATA || lang==tspeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(MainActivity.this, "Lang not supported", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!text.getEditText().getText().toString().equals(""))
                {
                    int status=tspeech.speak(text.getEditText().getText().toString().trim(),TextToSpeech.QUEUE_FLUSH,null);
                    if(status==TextToSpeech.ERROR)
                    {
                        Log.e("msg :","error converting");
                    }
                    text.getEditText().setText("");
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tspeech!=null)
        {
            tspeech.stop();
            tspeech.shutdown();
        }
    }
}
