//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Handler;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.ParametersBuilder;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.twobigears.audio360.AudioEngine;
import com.twobigears.audio360.ChannelMap;
import com.twobigears.audio360.EngineInitSettings;
import com.twobigears.audio360.SpatDecoderQueue;
import com.twobigears.audio360.TBQuat;
import com.twobigears.audio360.TBVector;
import com.twobigears.audio360exo2.Audio360Sink;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

public class AVProVideoExoPlayer extends AVProVideoPlayer implements EventListener, MediaSourceEventListener, VideoListener {
    private static final String PREFERENCE_NAME = "com.off2.amaze.AVProVideo";
    private static final String SAVED_MAX_INITIAL_BITRATE_KEY = "max_init_bitrate";

    private Factory m_AdaptiveTrackSelectionFactory;
    private Handler m_MainHandler;
    private SimpleExoPlayer m_ExoPlayer;
    private MediaSource m_MediaSource;
    private DefaultTrackSelector m_TrackSelector;
    private EventLogger m_EventLogger;
    private String m_UserAgent;
    private com.google.android.exoplayer2.upstream.DataSource.Factory m_MediaDataSourceFactory;
    private String m_PendingFilePath;
    private long m_PendingFileOffset;
    private Surface m_Surface;
    private Surface m_Surface_ToBeReleased;
    private AtomicBoolean m_bUpdateSurface = new AtomicBoolean();
    private AtomicBoolean m_bSurfaceTextureBound;
    float[] m_textureTransform;
    private SpatDecoderQueue m_Spat;
    private AudioEngine m_AudioEngine;
    private static ChannelMap[] m_ChannelMap;
    private AudioSink m_Sink;
    private DefaultBandwidthMeter m_BandwidthMeter;
    private long m_LastAbsoluteTime;
    private int m_ForcedFileFormat;
    private boolean m_bDebugLogStateChange = false;

    private void initChannelMap() {
        (m_ChannelMap = new ChannelMap[27])[0] = ChannelMap.TBE_8_2;
        m_ChannelMap[1] = ChannelMap.TBE_8;
        m_ChannelMap[2] = ChannelMap.TBE_6_2;
        m_ChannelMap[3] = ChannelMap.TBE_6;
        m_ChannelMap[4] = ChannelMap.TBE_4_2;
        m_ChannelMap[5] = ChannelMap.TBE_4;
        m_ChannelMap[6] = ChannelMap.TBE_8_PAIR0;
        m_ChannelMap[7] = ChannelMap.TBE_8_PAIR1;
        m_ChannelMap[8] = ChannelMap.TBE_8_PAIR2;
        m_ChannelMap[9] = ChannelMap.TBE_8_PAIR3;
        m_ChannelMap[10] = ChannelMap.TBE_CHANNEL0;
        m_ChannelMap[11] = ChannelMap.TBE_CHANNEL1;
        m_ChannelMap[12] = ChannelMap.TBE_CHANNEL2;
        m_ChannelMap[13] = ChannelMap.TBE_CHANNEL3;
        m_ChannelMap[14] = ChannelMap.TBE_CHANNEL4;
        m_ChannelMap[15] = ChannelMap.TBE_CHANNEL5;
        m_ChannelMap[16] = ChannelMap.TBE_CHANNEL6;
        m_ChannelMap[17] = ChannelMap.TBE_CHANNEL7;
        m_ChannelMap[18] = ChannelMap.HEADLOCKED_STEREO;
        m_ChannelMap[19] = ChannelMap.HEADLOCKED_CHANNEL0;
        m_ChannelMap[20] = ChannelMap.HEADLOCKED_CHANNEL1;
        m_ChannelMap[21] = ChannelMap.AMBIX_4;
        m_ChannelMap[22] = ChannelMap.AMBIX_9;
        m_ChannelMap[23] = ChannelMap.AMBIX_9_2;
        m_ChannelMap[24] = ChannelMap.AMBIX_16;
        m_ChannelMap[25] = ChannelMap.AMBIX_16_2;
        m_ChannelMap[26] = ChannelMap.STEREO;
    }

    public AVProVideoExoPlayer(int playerIndex, boolean watermarked, Random random) {
        super(playerIndex, watermarked, random);
        this.m_bUpdateSurface.set(false);
        this.m_bSurfaceTextureBound = new AtomicBoolean();
        this.m_bSurfaceTextureBound.set(false);
        if (m_ChannelMap == null) {
            this.initChannelMap();
        }

    }

    public float GetBufferingProgressPercent() {
        if (this.m_ExoPlayer != null) {
            this.m_fBufferingProgressPercent = (float)this.m_ExoPlayer.getBufferedPercentage();
            return this.m_fBufferingProgressPercent;
        } else {
            return 0.0F;
        }
    }

    public void SetHeadRotation(float x, float y, float z, float w) {
        if (this.m_AudioEngine != null) {
            this.m_AudioEngine.setListenerRotation(new TBQuat(x, y, z, w));
        }

    }

    public long GetEstimatedBandwidthUsed() {
        return this.m_BandwidthMeter == null ? 0L : this.m_BandwidthMeter.getBitrateEstimate();
    }

    public void SetFocusRotation(float x, float y, float z, float w) {
        if (this.m_Spat != null) {
            this.m_Spat.setFocusOrientationQuat(new TBQuat(x, y, z, w));
        }

    }

    public void SetFocusProps(float offFocusLevel, float widthDegrees) {
        if (this.m_Spat != null) {
            this.m_Spat.setFocusProperties(offFocusLevel, widthDegrees);
        }

    }

    public void SetPositionTrackingEnabled(boolean enabled) {
        if (this.m_AudioEngine != null) {
            this.m_AudioEngine.enablePositionalTracking(enabled, new TBVector(0.0F, 0.0F, 0.0F));
        }

    }

    public void SetFocusEnabled(boolean enabled) {
        if (this.m_Spat != null) {
            this.m_Spat.enableFocus(enabled, true);
        }

    }

    private ChannelMap getChannelMap(int channelID) {
        if (m_ChannelMap == null) {
            this.initChannelMap();
        }

        return channelID >= 0 && channelID < m_ChannelMap.length ? m_ChannelMap[channelID] : ChannelMap.TBE_8_2;
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (this.m_Width != width || this.m_Height != height) {
            System.out.println("AVProVideo changing video size " + this.m_Width + "x" + this.m_Height + " to " + width + "x" + height);

            SharedPreferences sharedPref = m_Context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(SAVED_MAX_INITIAL_BITRATE_KEY, height > 2000 ? 20000000 : 10000000);
            editor.apply();

            switch(unappliedRotationDegrees) {
                case 90:
                    this.m_textureTransform = new float[]{0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F};
                    if (this.m_bDebugLogStateChange) {
                        Log.e("AVProVideo", "Texture transform set for 90 degrees");
                    }
                    break;
                case 180:
                    this.m_textureTransform = new float[]{-1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F};
                    if (this.m_bDebugLogStateChange) {
                        Log.e("AVProVideo", "Texture transform set for 180 degrees");
                    }
                    break;
                case 270:
                    this.m_textureTransform = new float[]{0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F};
                    if (this.m_bDebugLogStateChange) {
                        Log.e("AVProVideo", "Texture transform set for 270 degrees");
                    }
                    break;
                default:
                    if (this.m_bDebugLogStateChange) {
                        Log.e("AVProVideo", "NO texture transform set");
                    }
            }

            synchronized(this) {
                this.m_Width = width;
                this.m_Height = height;
                this.m_bSourceHasVideo = true;
                this.m_bVideo_CreateRenderSurface.set(true);
                this.m_bVideo_DestroyRenderSurface.set(true);
            }
        }
    }

    public void onRenderedFirstFrame() {
    }

    public double GetCurrentAbsoluteTimestamp() {
        if (this.m_ExoPlayer != null && this.m_ExoPlayer.getPlaybackState() != 1) {
            if (this.m_ExoPlayer.getPlaybackState() == 2) {
                return (double)this.m_LastAbsoluteTime / 1000.0D;
            } else {
                Timeline tl;
                if ((tl = this.m_ExoPlayer.getCurrentTimeline()) == null) {
                    System.out.println("[AVProVideoTimestampTest] timeline is null");
                    return 0.0D;
                } else {
                    int windowIndex = this.m_ExoPlayer.getCurrentWindowIndex();
                    if (tl.getWindowCount() <= windowIndex) {
                        return 0.0D;
                    } else {
                        Window window = new Window();
                        Window populatedWindow = tl.getWindow(windowIndex, window);
                        this.m_LastAbsoluteTime = window.windowStartTimeMs == -9223372036854775807L ? 0L : populatedWindow.windowStartTimeMs + this.m_ExoPlayer.getCurrentPosition();
                        return (double)this.m_LastAbsoluteTime / 1000.0D;
                    }
                }
            }
        } else {
            return 0.0D;
        }
    }

    public float[] GetSeekableTimeRange() {
        return this.CalculateSeekableTimeRangeForPeriod();
    }

    protected boolean InitialisePlayer(boolean enableAudio360, int audio360Channels, boolean preferSoftwareDecoder) {
        if (this.m_Context == null) {
            return false;
        } else {
            String version;
            try {
                String packageName = this.m_Context.getPackageName();
                version = this.m_Context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            } catch (NameNotFoundException var13) {
                version = "?";
            }

            this.m_UserAgent = "AVProMobileVideo/" + version + " (Linux;Android " + VERSION.RELEASE + ") ExoPlayerLib/2.8.4";

            SharedPreferences sharedPref = m_Context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            long maxInitBitrate = sharedPref.getLong(SAVED_MAX_INITIAL_BITRATE_KEY, 20000000L);
            System.out.println("AVProVideo: InitializePlayer with init bitrate " + maxInitBitrate);

            this.m_BandwidthMeter = new DefaultBandwidthMeter.Builder()
                    .setInitialBitrateEstimate(maxInitBitrate)
                    .build();

            Activity activity = (Activity)this.m_Context;
            this.m_MainHandler = new Handler(activity.getMainLooper());
            this.m_MediaDataSourceFactory = this.BuildDataSourceFactory(true, "");
            this.m_AdaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(
                    this.m_BandwidthMeter,
                    2000,
                    AdaptiveTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS,
                    AdaptiveTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,
                    0.9f);

           this.m_TrackSelector = new DefaultTrackSelector(AVProVideoExoPlayer.this.m_AdaptiveTrackSelectionFactory);
           this.m_TrackSelector.setParameters(this.m_TrackSelector.buildUponParameters());
            this.m_EventLogger = new EventLogger(this.m_TrackSelector);

            CustomDefaultRenderersFactory defaultRenderersFactory;
            RenderersFactory rFactory = defaultRenderersFactory = new CustomDefaultRenderersFactory(this.m_Context, (DrmSessionManager)null, 1, preferSoftwareDecoder);
            if (enableAudio360) {
                EngineInitSettings engineSettings;
                (engineSettings = new EngineInitSettings()).getAudioSettings().setBufferSize(1024);
                engineSettings.getAudioSettings().setSampleRate(48000.0F);
                engineSettings.getMemorySettings().setSpatQueueSizePerChannel(8192);
                this.m_AudioEngine = AudioEngine.create(engineSettings, this.m_Context);
                this.m_Spat = this.m_AudioEngine.createSpatDecoderQueue();
                double latency = this.m_AudioEngine.getOutputLatencyMs();
                ChannelMap channelMap = this.getChannelMap(audio360Channels);
                this.m_Sink = new Audio360Sink(this.m_Spat, channelMap, latency);
                rFactory = new OpusRenderersFactory(this.m_Sink, defaultRenderersFactory);
            }

            this.m_ExoPlayer = ExoPlayerFactory.newSimpleInstance((RenderersFactory)rFactory, this.m_TrackSelector);
            this.m_ExoPlayer.addListener(this);
            this.m_ExoPlayer.addVideoListener(this);
            return true;
        }
    }

    protected void CloseVideoOnPlayer() {
        this.m_LastAbsoluteTime = 0L;
        this._stop();
        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.clearVideoSurface();
        }

        if (this.m_Surface != null) {
            this.m_Surface.release();
            this.m_Surface = null;
        }

        this.m_bUpdateSurface.set(false);
        this.m_bSurfaceTextureBound.set(false);
        this.m_MediaSource = null;
        this.m_VideoState = 0;
        this.m_PendingFilePath = null;
        this.m_PendingFileOffset = 0L;
    }

    protected void DeinitializeVideoPlayer() {
        this.m_TrackSelector = null;
        this.m_MediaSource = null;
        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.clearVideoSurface();
        }

        if (this.m_Surface != null) {
            this.m_Surface.release();
            this.m_Surface = null;
        }

        this.m_bUpdateSurface.set(false);
        this.m_bSurfaceTextureBound.set(false);
        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.stop();
            this.m_ExoPlayer.release();
            this.m_ExoPlayer = null;
        }

        if (this.m_AudioEngine != null) {
            if (this.m_Sink != null) {
                this.m_Sink = null;
            }

            this.m_AudioEngine.destroySpatDecoderQueue(this.m_Spat);
            this.m_AudioEngine.delete();
            this.m_Spat = null;
            this.m_AudioEngine = null;
        }

    }

    public boolean IsPlaying() {
        return this.m_ExoPlayer != null && (this.m_VideoState == 5 || this.IsSeeking() && this.m_ExoPlayer.getPlayWhenReady() || this.m_VideoState == 4 && this.m_ExoPlayer.getPlayWhenReady());
    }

    public boolean IsPaused() {
        return this.m_VideoState == 7;
    }

    public boolean IsSeeking() {
        return this.m_bIsSeeking;
    }

    public boolean IsFinished() {
        return this.m_VideoState == 8;
    }

    public boolean CanPlay() {
        return this.m_VideoState == 6 || this.m_VideoState == 7 || this.m_VideoState == 5 || this.m_VideoState == 8;
    }

    private com.google.android.exoplayer2.upstream.HttpDataSource.Factory BuildHttpDataSourceFactory(boolean useBandwidthMeter, String httpHeaderJson) {
        com.google.android.exoplayer2.upstream.HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSourceFactory(this.m_UserAgent, useBandwidthMeter ? this.m_BandwidthMeter : null, 8000, 8000, true);
        if (httpHeaderJson != null && !httpHeaderJson.isEmpty()) {
            Map<String, String> httpHeaderMap = GetJsonAsMap(httpHeaderJson);
            httpDataSourceFactory.getDefaultRequestProperties().set(httpHeaderMap);
        }

        return httpDataSourceFactory;
    }

    private com.google.android.exoplayer2.upstream.DataSource.Factory BuildDataSourceFactory(boolean useBandwidthMeter, String httpHeaderJson) {
        return new DefaultDataSourceFactory(this.m_Context, useBandwidthMeter ? this.m_BandwidthMeter : null, this.BuildHttpDataSourceFactory(useBandwidthMeter, httpHeaderJson));
    }

    protected void PlayerRendererSetup() {
        if (this.m_PendingFilePath != null && this.m_PendingFilePath.length() > 0) {
            this.OpenVideoFromFileInternal(this.m_PendingFilePath, this.m_PendingFileOffset, "", this.m_ForcedFileFormat);
        }

    }

    protected void PlayerRenderUpdate() {
        this.UpdateVideoMetadata();
        if (this.m_Surface_ToBeReleased != null) {
            this.m_bSurfaceTextureBound.set(false);
            if (this.m_ExoPlayer != null) {
                this.m_ExoPlayer.clearVideoSurface();
            }

            this.m_Surface_ToBeReleased.release();
            this.m_Surface_ToBeReleased = null;
        }

        if (this.m_bUpdateSurface.get()) {
            if (this.m_ExoPlayer != null) {
                this.m_ExoPlayer.clearVideoSurface();
            }

            if (this.m_Surface != null) {
                this.m_Surface.release();
                this.m_Surface = null;
            }

            if (this.m_SurfaceTexture != null) {
                if (this.m_Surface == null) {
                    this.m_Surface = new Surface(this.m_SurfaceTexture);
                }

                if (this.m_Surface != null && this.m_ExoPlayer != null) {
                    this.m_ExoPlayer.setVideoSurface(this.m_Surface);
                    this.m_bSurfaceTextureBound.set(true);
                    this.m_bUpdateSurface.set(false);
                }
            }
        }

        if (this.m_PendingFilePath != null && this.m_PendingFilePath.length() > 0) {
            this.OpenVideoFromFileInternal(this.m_PendingFilePath, this.m_PendingFileOffset, "", this.m_ForcedFileFormat);
        }

        if (this.m_ExoPlayer != null) {
            PlaybackParameters currParams = this.m_ExoPlayer.getPlaybackParameters();
            this.m_fPlaybackRate = currParams.speed;
        }

    }

    private float[] CalculateSeekableTimeRangeForPeriod() {
        float[] result;
        (result = new float[2])[0] = 0.0F;
        result[1] = 0.0F;
        if (this.m_ExoPlayer == null) {
            return result;
        } else {
            Timeline timeline;
            if ((timeline = this.m_ExoPlayer.getCurrentTimeline()).isEmpty()) {
                return result;
            } else {
                int currentWindowIndex = this.m_ExoPlayer.getCurrentWindowIndex();
                Window currentWindow = new Window();
                if (!(currentWindow = timeline.getWindow(currentWindowIndex, currentWindow)).isSeekable) {
                    return result;
                } else {
                    int currentPeriodIndex = this.m_ExoPlayer.getCurrentPeriodIndex();
                    int windowFirstPeriodIndex = currentWindow.firstPeriodIndex;
                    if (currentPeriodIndex < windowFirstPeriodIndex) {
                        return result;
                    } else {
                        long totalPeriodDuration = 0L;

                        for(int i = windowFirstPeriodIndex; i < currentPeriodIndex; ++i) {
                            Period period = new Period();
                            period = timeline.getPeriod(i, period);
                            totalPeriodDuration += period.durationUs == -9223372036854775807L ? 0L : period.durationUs;
                        }

                        Period currPeriod = new Period();
                        currPeriod = timeline.getPeriod(currentPeriodIndex, currPeriod);
                        boolean isFirstPeriod = currentPeriodIndex == windowFirstPeriodIndex;
                        totalPeriodDuration -= isFirstPeriod ? 0L : currentWindow.positionInFirstPeriodUs;
                        long remainingWindowDuration = currentWindow.durationUs == -9223372036854775807L ? 0L : currentWindow.durationUs - totalPeriodDuration;
                        long remainingPeriodDuration = -9223372036854775807L;
                        if (currPeriod.durationUs != -9223372036854775807L) {
                            remainingPeriodDuration = isFirstPeriod ? currPeriod.durationUs - currentWindow.positionInFirstPeriodUs : currPeriod.durationUs;
                        }

                        long seekableTimeForPeriod = currPeriod.durationUs == -9223372036854775807L ? remainingWindowDuration : Math.min(remainingWindowDuration, remainingPeriodDuration);
                        long first = isFirstPeriod && currentWindow.positionInFirstPeriodUs != -9223372036854775807L ? currentWindow.positionInFirstPeriodUs : 0L;
                        if (currentWindow.windowStartTimeMs != -9223372036854775807L) {
                            first -= currentWindow.windowStartTimeMs * 1000L;
                        }

                        long second = first + seekableTimeForPeriod;
                        result[0] = (float)first / 1000.0F;
                        result[1] = (float)second / 1000.0F;
                        return result;
                    }
                }
            }
        }
    }

    private static Map<String, String> GetJsonAsMap(String json) {
        HashMap result = new HashMap();

        try {
            JSONObject jsonObj;
            Iterator keyIt = (jsonObj = new JSONObject(json)).keys();

            while(keyIt.hasNext()) {
                String key = (String)keyIt.next();
                String val = jsonObj.getString(key);
                result.put(key, val);
            }

            return result;
        } catch (Exception var6) {
            throw new RuntimeException("Couldn't parse json:" + json, var6);
        }
    }

    private MediaSource BuildMediaSource(String filePath, long fileOffset, String httpHeaderJson) {
        MediaSource mediaSource = null;
        Uri uri = Uri.parse(filePath);
        String lowerPath;
        if (!(lowerPath = filePath.toLowerCase()).startsWith("jar:") && !lowerPath.contains(".zip!") && !lowerPath.contains(".obb!")) {
            int type = 3;
            switch(this.m_ForcedFileFormat) {
                case 0:
                    type = Util.inferContentType(uri);
                    break;
                case 1:
                    type = 2;
                    break;
                case 2:
                    type = 0;
                    break;
                case 3:
                    type = 1;
            }

            com.google.android.exoplayer2.upstream.HttpDataSource.Factory httpFactory;
            switch(type) {
                case 0:
                    httpFactory = this.BuildHttpDataSourceFactory(false, httpHeaderJson);
                    com.google.android.exoplayer2.source.dash.DashChunkSource.Factory chunkSourceFactory = new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource.Factory(this.m_MediaDataSourceFactory);
                    mediaSource = (new com.google.android.exoplayer2.source.dash.DashMediaSource.Factory(chunkSourceFactory, httpFactory)).createMediaSource(uri);
                    break;
                case 1:
                    httpFactory = this.BuildHttpDataSourceFactory(false, httpHeaderJson);
                    com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory1 = new com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource.Factory(this.m_MediaDataSourceFactory);
                    mediaSource = (new com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource.Factory(chunkSourceFactory1, httpFactory)).createMediaSource(uri);
                    break;
                case 2:
                    httpFactory = this.BuildHttpDataSourceFactory(false, httpHeaderJson);
                    mediaSource = (new com.google.android.exoplayer2.source.hls.HlsMediaSource.Factory(httpFactory)).createMediaSource(uri);
                    break;
                case 3:
                    String scheme = uri.getScheme();
                    if (fileOffset > 0L && (TextUtils.isEmpty(scheme) || "file".equals(scheme))) {
                        if (filePath.startsWith("file:/")) {
                            uri = Uri.parse(filePath.substring(6));
                        }

                        DefaultExtractorsFactory defaultExtractorsFactory;
                        (defaultExtractorsFactory = new DefaultExtractorsFactory()).setMp4ExtractorFlags(1);
                        mediaSource = (new com.google.android.exoplayer2.source.ExtractorMediaSource.Factory(new AVPro_FileDataSourceFactory(fileOffset))).setExtractorsFactory(defaultExtractorsFactory).createMediaSource(uri);
                    } else {
                        if (filePath.startsWith("file:/")) {
                            uri = Uri.parse(filePath.substring(6));
                        }

                        com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = this.BuildDataSourceFactory(false, httpHeaderJson);
                        DefaultExtractorsFactory defaultExtractorsFactory;
                        (defaultExtractorsFactory = new DefaultExtractorsFactory()).setMp4ExtractorFlags(1);
                        mediaSource = (new com.google.android.exoplayer2.source.ExtractorMediaSource.Factory(dataSourceFactory)).setExtractorsFactory(defaultExtractorsFactory).createMediaSource(uri);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }
        } else {
            try {
                String searchString = "/assets/";
                int iIndexOf;
                if ((iIndexOf = filePath.lastIndexOf(searchString)) != -1) {
                    String strippedLowerFilepath = filePath.substring(iIndexOf + searchString.length());
                    AssetFileDescriptor assetDesc;
                    if ((assetDesc = this.m_Context.getAssets().openFd(strippedLowerFilepath)) != null) {
                        assetDesc.close();
                        Uri strippedUri = Uri.parse("assets:///" + strippedLowerFilepath);
                        (new StringBuilder("uri = ")).append(strippedUri).append(" | fileOffset = ").append(fileOffset);
                        DefaultExtractorsFactory defaultExtractorsFactory;
                        (defaultExtractorsFactory = new DefaultExtractorsFactory()).setMp4ExtractorFlags(1);
                        mediaSource = (new com.google.android.exoplayer2.source.ExtractorMediaSource.Factory(new AVPro_AssetSourceFactory(fileOffset, this.m_Context))).setExtractorsFactory(defaultExtractorsFactory).createMediaSource(strippedUri);
                    }
                }
            } catch (Exception var14) {
            }

            if (mediaSource == null) {
                DefaultExtractorsFactory defaultExtractorsFactory;
                (defaultExtractorsFactory = new DefaultExtractorsFactory()).setMp4ExtractorFlags(1);
                mediaSource = (new com.google.android.exoplayer2.source.ExtractorMediaSource.Factory(new JarDataSourceFactory(filePath, fileOffset))).setExtractorsFactory(defaultExtractorsFactory).createMediaSource(uri);
            }
        }

        return (MediaSource)mediaSource;
    }

    protected boolean OpenVideoFromFileInternal(String filePath, long fileOffset, String httpHeaderJson, int forcedFileFormat) {
        boolean success = false;
        if (this.m_bDebugLogStateChange) {
            Log.e("AVProVideo", "OpenVideoFromFileInternal | m_ExoPlayer.getPlaybackState() = " + (this.m_ExoPlayer != null ? this.m_ExoPlayer.getPlaybackState() : null) + " | m_VideoState = " + this.m_VideoState + " | m_SurfaceTexture = " + this.m_SurfaceTexture);
        }

        this.m_ForcedFileFormat = forcedFileFormat;
        if (!this.m_bSurfaceTextureBound.get()) {
            this.BindSurfaceToPlayer();
        }

        if (this.m_ExoPlayer != null && this.m_VideoState != 2 && this.m_ExoPlayer.getPlaybackState() == 1 && this.m_bSurfaceTextureBound.get()) {
            this.m_ExoPlayer.setPlayWhenReady(false);
            this.m_MediaSource = this.BuildMediaSource(filePath, fileOffset, httpHeaderJson);
            if (this.m_MediaSource != null) {
                this.m_LastAbsoluteTime = 0L;
                this.m_VideoState = 2;
                this.m_ExoPlayer.prepare(this.m_MediaSource, true, true);
                success = true;
            } else {
                System.out.println("[AVProVideo] error failed to prepare");
            }

            this.m_PendingFilePath = null;
            this.m_PendingFileOffset = 0L;
        } else {
            this.m_PendingFilePath = filePath;
            this.m_PendingFileOffset = fileOffset;
            success = true;
        }

        return success;
    }

    public void SetLooping(boolean bLooping) {
        this.m_bLooping = bLooping;
        if (this.m_ExoPlayer != null && this.m_VideoState >= 3) {
            this.UpdateLooping();
        } else {
            this.AddVideoCommandInt(VideoCommand_SetLooping, 0);
        }
    }

    public long GetCurrentTimeMs() {
        return this.m_ExoPlayer == null ? 0L : this.m_ExoPlayer.getCurrentPosition();
    }

    public void SetPlaybackRate(float fRate) {
        if (this.m_ExoPlayer != null) {
            PlaybackParameters currParams = this.m_ExoPlayer.getPlaybackParameters();
            this.m_ExoPlayer.setPlaybackParameters(new PlaybackParameters(fRate, currParams.pitch));
        }

    }

    public void SetAudioTrack(int iTrackIndex) {
        if (this.m_ExoPlayer != null && iTrackIndex < this.m_iNumberAudioTracks && iTrackIndex != this.m_iCurrentAudioTrackIndex) {
            for(int i = 0; i < this.m_ExoPlayer.getRendererCount(); ++i) {
                if (this.m_ExoPlayer.getRendererType(i) == 1) {
                    ParametersBuilder pb;
                    (pb = new ParametersBuilder()).clearSelectionOverrides(i);
                    pb.setRendererDisabled(i, true);
                    this.m_TrackSelector.setParameters(pb);
                }
            }

            MappedTrackInfo mappedTrackInfo;
            if ((mappedTrackInfo = this.m_TrackSelector.getCurrentMappedTrackInfo()) != null) {
                int audioTracksPassed = 0;

                for(int i = 0; i < mappedTrackInfo.getRendererCount(); ++i) {
                    TrackGroupArray trackGroups;
                    if (this.m_ExoPlayer.getRendererType(i) == 1 && (trackGroups = mappedTrackInfo.getTrackGroups(i)) != null) {
                        int index;
                        if ((index = iTrackIndex - audioTracksPassed) < trackGroups.length) {
                            TrackGroup trackGroup = trackGroups.get(index);
                            ArrayList<Integer> supportedTracks = new ArrayList();

                            for(int j = 0; j < trackGroup.length; ++j) {
                                if (mappedTrackInfo.getTrackSupport(i, index, j) == 4) {
                                    supportedTracks.add(j);
                                }
                            }

                            if (supportedTracks.size() != 0) {
                                int[] tracks = new int[supportedTracks.size()];

                                for(int j = 0; j < supportedTracks.size(); ++j) {
                                    tracks[j] = (Integer)supportedTracks.get(j);
                                }

                                SelectionOverride so = new SelectionOverride(index, tracks);
                                ParametersBuilder pb;
                                (pb = new ParametersBuilder()).setSelectionOverride(i, trackGroups, so);
                                pb.setRendererDisabled(i, false);
                                this.m_TrackSelector.setParameters(pb);
                                this.m_iCurrentAudioTrackIndex = index;
                                return;
                            }
                            break;
                        }

                        audioTracksPassed += trackGroups.length;
                    }
                }

            }
        }
    }

    protected void _play() {
        if (this.m_bDebugLogStateChange) {
            Log.e("AVProVideo", "_play called");
        }

        if (this.m_ExoPlayer != null) {
            if (this.m_VideoState == 0) {
                this.m_VideoState = 2;
                this.m_ExoPlayer.prepare(this.m_MediaSource, false, false);
            } else {
                this.m_VideoState = 5;
            }

            this.m_ExoPlayer.setPlayWhenReady(true);
            if (this.m_AudioEngine != null) {
                this.m_AudioEngine.start();
            }
        }

    }

    protected void _pause() {
        if (this.m_bDebugLogStateChange) {
            Log.e("AVProVideo", "_pause called : m_VideoState = " + this.m_VideoState);
        }

        if (this.m_ExoPlayer != null && this.m_VideoState != 6 && this.m_VideoState != 8) {
            this.m_ExoPlayer.setPlayWhenReady(false);
            this.m_VideoState = 7;
            if (this.m_AudioEngine != null) {
                this.m_AudioEngine.suspend();
            }
        }

    }

    protected void _stop() {
        if (this.m_ExoPlayer != null) {
            if (this.m_Spat != null) {
                this.m_Spat.flushQueue();
                this.m_Spat.setEndOfStream(true);
            }

            if (this.m_AudioEngine != null) {
                if (this.m_Sink != null) {
                    this.m_Sink.reset();
                }

                this.m_AudioEngine.suspend();
            }

            if (this.m_VideoState != 6) {
                this.m_ExoPlayer.stop();
            }

            this.m_VideoState = 6;
        }

    }

    protected void _seek(int timeMs) {
        if (this.m_Sink != null) {
            this.m_Sink.reset();
        }

        if (this.m_Spat != null) {
            this.m_Spat.flushQueue();
        }

        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.setSeekParameters(SeekParameters.EXACT);
            this.m_ExoPlayer.seekTo((long)timeMs);
            this.m_bIsSeeking = true;
        }

    }

    protected void _seekFast(int timeMs) {
        if (this.m_Sink != null) {
            this.m_Sink.reset();
        }

        if (this.m_Spat != null) {
            this.m_Spat.flushQueue();
        }

        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.setSeekParameters(SeekParameters.CLOSEST_SYNC);
            this.m_ExoPlayer.seekTo((long)timeMs);
            this.m_bIsSeeking = true;
        }

    }

    protected void UpdateAudioVolumes() {
        if (this.m_ExoPlayer != null) {
            float volume = 0.0F;
            if (!this.m_AudioMuted) {
                volume = this.m_AudioVolume;
            }

            this.m_ExoPlayer.setVolume(volume);
        }

    }

    protected void UpdateLooping() {
        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.setRepeatMode(this.m_bLooping ? 2 : 0);
        }

    }

    protected void BindSurfaceToPlayer() {
        if (this.m_Surface != null) {
            this.m_Surface_ToBeReleased = this.m_Surface;
            this.m_Surface = null;
        }

        this.m_bUpdateSurface.set(true);
        this.m_bSurfaceTextureBound.set(false);
        if (this.m_bDebugLogStateChange) {
            Log.e("AVProVideo", "BindSurfaceToPlayer called");
        }

    }

    public void onPlayerStateChanged(boolean playWhenReady, int state) {
        switch(state) {
            case 1:
                if (this.m_bDebugLogStateChange) {
                    System.out.println("AVProVideo video state: idle");
                }

                this.m_bIsBuffering = false;
                this.m_VideoState = 0;
                return;
            case 2:
                if (this.m_bDebugLogStateChange) {
                    System.out.println("AVProVideo video state: buffering");
                }

                if (this.m_VideoState != 2) {
                    this.m_VideoState = 4;
                }

                this.m_bIsBuffering = true;
                return;
            case 3:
                if (this.m_bDebugLogStateChange) {
                    System.out.println("AVProVideo video state: ready | m_VideoState: " + this.m_VideoState + " | m_bIsBuffering: " + this.m_bIsBuffering);
                }

                this.m_bIsBuffering = false;
                if (this.m_PendingFilePath != null && this.m_PendingFilePath.length() > 0) {
                    if (this.m_bDebugLogStateChange) {
                        System.out.println("AVProVideo video state: has pending file path");
                        return;
                    }
                } else if (this.m_VideoState >= 2) {
                    this.m_VideoState = this.m_VideoState == 2 ? 3 : this.m_VideoState;
                    Format videoFormat = this.m_ExoPlayer.getVideoFormat();
                    Format audioFormat = this.m_ExoPlayer.getAudioFormat();
                    if (videoFormat == null) {
                        if (audioFormat != null) {
                            this.m_DisplayRate_FrameRate = this.m_fSourceVideoFrameRate = audioFormat.frameRate;
                            this.m_bSourceHasVideo = false;
                            this.m_bSourceHasAudio = true;
                            this.m_Width = 0;
                            this.m_Height = 0;
                            this.m_DurationMs = this.m_ExoPlayer.getDuration();
                            this.m_VideoState = this.m_ExoPlayer.getPlayWhenReady() ? 5 : 6;
                            this.m_bVideo_AcceptCommands.set(true);
                        }

                        return;
                    }

                    this.m_fSourceVideoFrameRate = videoFormat.frameRate;
                    if (this.m_fSourceVideoFrameRate > 0.0F) {
                        this.m_DisplayRate_FrameRate = this.m_fSourceVideoFrameRate;
                    }

                    this.m_DurationMs = this.m_ExoPlayer.getDuration();
                    this.m_bSourceHasVideo = true;
                    this.m_bSourceHasAudio = audioFormat != null;
                    (new StringBuilder("videoFormat.rotationDegrees = ")).append(videoFormat.rotationDegrees);
                    if (videoFormat.rotationDegrees == 90 || videoFormat.rotationDegrees == 270) {
                        int newWidth = videoFormat.height;
                        int newHeight = videoFormat.width;
                        if (this.m_Width > 0 && this.m_Height > 0 && (newWidth != this.m_Width || newHeight != this.m_Height) && this.m_bVideo_RenderSurfaceCreated.get()) {
                            synchronized(this) {
                                this.m_Width = newWidth;
                                this.m_Height = newHeight;
                                this.m_bVideo_DestroyRenderSurface.set(true);
                                this.m_bVideo_CreateRenderSurface.set(true);
                            }
                        }
                    }

                    this.m_VideoState = this.m_ExoPlayer.getPlayWhenReady() ? 5 : 6;
                    this.m_bVideo_AcceptCommands.set(true);
                    return;
                }

                return;
            case 4:
                if (this.m_bDebugLogStateChange) {
                    System.out.println("AVProVideo video state: ended");
                }

                this.m_VideoState = 8;
                break;
            default:
                if (this.m_bDebugLogStateChange) {
                    System.out.println("AVProVideo video state: " + state);
                }
        }

        this.m_bIsBuffering = false;
    }

    public void onLoadingChanged(boolean isLoading) {
    }

    public void onPositionDiscontinuity(int reason) {
    }

    public void onShuffleModeEnabledChanged(boolean enabled) {
    }

    public void onSeekProcessed() {
        this.m_bIsSeeking = false;
    }

    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }

    public void onPlayerError(ExoPlaybackException e) {
        System.out.println("AVProVideo error " + e.getMessage());
        if (this.m_VideoState > 0 && this.m_VideoState < 5) {
            this.m_iLastError = 100;
        }

    }

    public void onTracksChanged(TrackGroupArray ignored, TrackSelectionArray trackSelections) {
        this.m_iNumberAudioTracks = 0;
        if (this.m_ExoPlayer != null && this.m_TrackSelector != null) {
            MappedTrackInfo trackinfo;
            if ((trackinfo = this.m_TrackSelector.getCurrentMappedTrackInfo()) != null) {
                (new StringBuilder("Number of tracks in source: ")).append(trackinfo.getRendererCount());

                for(int i = 0; i < trackinfo.getRendererCount(); ++i) {
                    if (this.m_ExoPlayer.getRendererType(i) == 1) {
                        TrackGroupArray trackGroups = trackinfo.getTrackGroups(i);
                        this.m_iNumberAudioTracks += trackGroups != null ? trackGroups.length : 0;
                    }
                }

                (new StringBuilder("Number of audio tracks in source: ")).append(this.m_iNumberAudioTracks);
                if (this.m_iCurrentAudioTrackIndex < 0) {
                    this.m_iCurrentAudioTrackIndex = 0;
                }

            }
        }
    }

    public void onLoadError(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
        (new StringBuilder("onLoadError (param version) : error = ")).append(error);
    }

    public void onLoadCanceled(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public void onLoadStarted(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public void onLoadCompleted(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public void onUpstreamDiscarded(int windowIndex, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
    }

    public void onReadingStarted(int windowIndex, MediaPeriodId mediaPeriodId) {
    }

    public void onDownstreamFormatChanged(int windowIndex, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
    }

    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    public void onRepeatModeChanged(int repeatMode) {
    }

    public void onMediaPeriodCreated(int windowIndex, MediaPeriodId mediaPeriodId) {
    }

    public void onMediaPeriodReleased(int windowIndex, MediaPeriodId mediaPeriodId) {
    }

    protected void UpdateVideoMetadata() {
        Format videoFormat;
        if (this.m_fSourceVideoFrameRate < 0.0F && this.m_ExoPlayer != null && (videoFormat = this.m_ExoPlayer.getVideoFormat()) != null) {
            this.m_fSourceVideoFrameRate = videoFormat.frameRate;
        }

    }
}
