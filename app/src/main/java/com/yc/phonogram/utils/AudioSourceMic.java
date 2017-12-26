package com.yc.phonogram.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.text.format.Time;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class AudioSourceMic 
{
	private static final String LOG_TAG ="AudioSourceMic"; 
	private final int RINGBUFFER_CAP = 16*1024;
	private AudioRecord mRecord;
	private int mAudioSessionId;
	private boolean mThreadLoop;
	private boolean mPauseFlag;
	private taskThread mTaskThread = null;
	private int minBufferSize;
	private AcousticEchoCanceler mAcousticEchoCanceler = null;
	private NoiseSuppressor mNoiseSuppressor = null;
	private AutomaticGainControl mAutomaticGainControl = null;

	
	// for test
	private boolean isRecordToWav = true;
	String mRecordfile = "/mnt/sdcard/out.wav";
	private static RandomAccessFile randomAccessWriter = null;
	private static int  payloadSize = 0;

	public AudioSourceMic()
	{
		mRecord = null;
		mAudioSessionId = 0;
		mThreadLoop = false;
		mTaskThread = null;
	}
	
	public int Create(int mSampleRate)
	{
		Log.i(LOG_TAG, "create...mSampleRate="+mSampleRate);

		// default: mSampleRate = 8000
		int channelConfig = AudioFormat.CHANNEL_IN_MONO;
		int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
		minBufferSize = AudioRecord.getMinBufferSize(mSampleRate, channelConfig, audioEncoding);	
		Log.i(LOG_TAG, "minBufferSize: " + minBufferSize);

		mRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, //MediaRecorder.AudioSource.MIC, //
//		mRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
				mSampleRate,
				channelConfig, 
				audioEncoding,		
				minBufferSize*2);
		mAudioSessionId = mRecord.getAudioSessionId();
		Log.i(LOG_TAG, "AudioSessionId: " + mAudioSessionId);
		
		if (mAudioSessionId != 0 && android.os.Build.VERSION.SDK_INT >= 16)
		{
			if (NoiseSuppressor.isAvailable())
			{
				if (mNoiseSuppressor != null)
				{
					mNoiseSuppressor.release();
					mNoiseSuppressor = null;
				}
				
				mNoiseSuppressor = NoiseSuppressor.create(mAudioSessionId);
				if (mNoiseSuppressor != null) 
				{
					mNoiseSuppressor.setEnabled(true);
				}
				else
				{
					Log.i(LOG_TAG, "Failed to create NoiseSuppressor.");								
				}
			}
			else
			{
				Log.i(LOG_TAG, "Doesn't support NoiseSuppressor");								
			}	
			
			if (AcousticEchoCanceler.isAvailable())
			{
				if (mAcousticEchoCanceler != null)
				{
					mAcousticEchoCanceler.release();
					mAcousticEchoCanceler = null;
				}
				
				mAcousticEchoCanceler = AcousticEchoCanceler.create(mAudioSessionId);
				if (mAcousticEchoCanceler != null)
				{
					mAcousticEchoCanceler.setEnabled(true);
					// mAcousticEchoCanceler.setControlStatusListener(listener)setEnableStatusListener(listener)
			    }
				else
				{
					Log.i(LOG_TAG, "Failed to initAEC.");	
					mAcousticEchoCanceler = null;
				}
			}
			else
			{
				Log.i(LOG_TAG, "Doesn't support AcousticEchoCanceler");								
			}

			if (AutomaticGainControl.isAvailable())
			{
				if (mAutomaticGainControl != null)
				{
					mAutomaticGainControl.release();
					mAutomaticGainControl = null;
				}
				
				mAutomaticGainControl = AutomaticGainControl.create(mAudioSessionId);
				if (mAutomaticGainControl != null) 
				{
					mAutomaticGainControl.setEnabled(true);
				}
				else
				{
					Log.i(LOG_TAG, "Failed to create AutomaticGainControl.");								
				}
				
			}
			else
			{
				Log.i(LOG_TAG, "Doesn't support AutomaticGainControl");								
			}
		}
		else
		{
			Log.i(LOG_TAG, "No AcousticEchoCanceler !!!");								
		}
		
		if(isRecordToWav){
			try {
				Time t = new Time();
				t.setToNow();
				
				mRecordfile = "/mnt/sdcard/out_"+t.hour+"_"+t.minute+"_"+t.second+".wav"; 
				File f = new File(mRecordfile);
				if(f.exists()){
					f.delete();
				}
				
				randomAccessWriter = new RandomAccessFile(mRecordfile, "rw");	
				initWavHeader(randomAccessWriter, (short)channelConfig, (int)mSampleRate, (short)16);
				payloadSize = 0;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		return mAudioSessionId;
	}
	
	public int Start()
	{
		Log.i(LOG_TAG, "start...");
		if (mRecord != null)
		{
 			mThreadLoop = true;
			mTaskThread = new taskThread();
			mTaskThread.start();
			return 0;
		}else {
		
			return -1;
		}
		
	}
	
	private class taskThread extends Thread 
	{
		public taskThread()
		{
			
		}
		
		public void run() 
		{
			Log.i(LOG_TAG, "taskThread start...");

			byte[] pcmData = new byte[minBufferSize*2];
			final Object threadLock = new Object();
			Thread.currentThread().setPriority(MAX_PRIORITY);
	        Thread.currentThread().setName("AudioSourceMicr");
			mRecord.startRecording();
			
	        synchronized (threadLock) 
			{
				while (mThreadLoop && !Thread.currentThread().isInterrupted())
				{
					if (mPauseFlag) continue;
					
					int bufferReadResult = mRecord.read(pcmData, 0, minBufferSize*2-512);
					Log.i(LOG_TAG, "bufferReadResult = "+bufferReadResult);
					for (int i = 0; i < bufferReadResult; i++) {
						pcmData[i] = (byte)(pcmData[i] * 1.8);
					}
					if (bufferReadResult > 0){
	            		saveWaveData(pcmData, bufferReadResult);
                    }
					else{
						Log.i(LOG_TAG, "Error to read pcm from AudioRecorder.");
					}
				}	 
			}
			
		}
	}
 
	private void Stop()
	{
		if (mThreadLoop && mTaskThread != null)
		{
			mThreadLoop = false;
			try 
			{
				mTaskThread.join(5*1000);
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
			mTaskThread = null;
			
			if (mRecord != null)
			{
				mRecord.stop();
			}
		}	
				
		
		if(isRecordToWav){
			if(null != randomAccessWriter){
				Log.i(LOG_TAG, "STOP RECODER. write playload size");
				try {
					randomAccessWriter.seek(4);
					randomAccessWriter.write(Integer.reverseBytes(36+payloadSize));
					randomAccessWriter.seek(40);
					randomAccessWriter.write(Integer.reverseBytes(payloadSize));
					randomAccessWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		randomAccessWriter = null;
	}	
	
	private void saveWaveData(byte[] bsBuffer, int len)
	{
		if(!isRecordToWav){
			return;
		}
		if(len > 0){
			if(null != randomAccessWriter){
				try {
					randomAccessWriter.write(bsBuffer, 0, len);;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				payloadSize += len;
			}	
		}
	}
	
	public void Close()
	{
		Stop();	
		
		if (mRecord != null)
		{
			mRecord.release();
			mRecord = null;
			mAudioSessionId = 0;
		}
		
		if (mAcousticEchoCanceler != null)
		{
			mAcousticEchoCanceler.setEnabled(false);
			mAcousticEchoCanceler.release();
			mAcousticEchoCanceler = null;
		}	
		
		if (mNoiseSuppressor != null) 
		{
			mNoiseSuppressor.setEnabled(false);
			mNoiseSuppressor.release();
			mNoiseSuppressor = null;
		}
	
		if (mAutomaticGainControl != null) 
		{
			mAutomaticGainControl.setEnabled(false);
			mAutomaticGainControl.release();
			mAutomaticGainControl = null;
		}
	}
	
	public int GetAudioSessionId()
	{
		return mAudioSessionId;
	}
	
	static void initWavHeader(RandomAccessFile randomAccessWriter, short Channels, int SamplesPerSec, short BitsPerSample){	
        try {
			randomAccessWriter.setLength(0);
		// Set file length to 0, to prevent unexpected behavior in case the file already existed
	        randomAccessWriter.writeBytes("RIFF");
	        randomAccessWriter.writeInt(0); // Final file size not known yet, write 0 
	        randomAccessWriter.writeBytes("WAVE");
	        randomAccessWriter.writeBytes("fmt ");
	        randomAccessWriter.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM
	        randomAccessWriter.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM
	        randomAccessWriter.writeShort(Short.reverseBytes((short) 1));// Number of channels, 1 for mono, 2 for stereo
	        randomAccessWriter.writeInt(Integer.reverseBytes(SamplesPerSec)); // Sample rate
	        randomAccessWriter.writeInt(Integer.reverseBytes(SamplesPerSec*BitsPerSample*Channels/8)); // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8
	        randomAccessWriter.writeShort(Short.reverseBytes((short)(Channels*BitsPerSample/8))); // Block align, NumberOfChannels*BitsPerSample/8
	        randomAccessWriter.writeShort(Short.reverseBytes(BitsPerSample)); // Bits per sample
	        randomAccessWriter.writeBytes("data");
	        randomAccessWriter.writeInt(0); // Data chunk size not known yet, write 0
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void pause() {
		mPauseFlag = true;
	}
	
	public void resume() {
		mPauseFlag = false;
	}
}
