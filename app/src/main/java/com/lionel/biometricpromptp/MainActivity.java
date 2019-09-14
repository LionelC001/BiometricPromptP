package com.lionel.biometricpromptp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS;

public class MainActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIsBiometricHardwareAvailable();
        initBiometricPrompt();
    }

    private void checkIsBiometricHardwareAvailable() {
        ImageView imgAvailable = findViewById(R.id.imgAvailable);

        if (BiometricManager.from(this).canAuthenticate() == BIOMETRIC_SUCCESS) {
            imgAvailable.setImageResource(R.drawable.ic_check_circle);
        } else {
            imgAvailable.setImageResource(R.drawable.ic_cancel);
        }
    }

    private void initBiometricPrompt() {

        Executor newExecutor = Executors.newSingleThreadExecutor();

        biometricPrompt = new BiometricPrompt(this, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d("<>error", errString.toString());
                Toast.makeText(MainActivity.this,  errString.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d("<>success","resutl "+ result.toString());
                Log.d("<>success","getCryptoObject "+ result.getCryptoObject());
//                Log.d("<>success","getCipher "+ result.getCryptoObject().getCipher());
//                Log.d("<>success","getMac "+ result.getCryptoObject().getMac());
//                Log.d("<>success","getSignature "+ result.getCryptoObject().getSignature());

                runOnUiThread(() -> Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("<>failed", "thread " + Thread.currentThread().getName());
                Toast.makeText(MainActivity.this,"failed", Toast.LENGTH_LONG).show();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("將 Touch ID 用於「OOXX」")
                .setNegativeButtonText("取消")
                .build();

    }

    public void onBiometric(View view) {
        biometricPrompt.authenticate(promptInfo);
    }
}
