package com.stardust.crusaders.android;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.stardust.crusaders.DatabaseInterface;
import com.stardust.crusaders.SpaceShooterGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        DatabaseInterface databaseInterface = new AndroidDatabaseHelper(this);
        initialize(new SpaceShooterGame(databaseInterface), config);
	}
}
