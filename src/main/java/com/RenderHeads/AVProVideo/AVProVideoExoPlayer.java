//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Build.VERSION;
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
import com.google.android.exoplayer2.SimpleExoPlayer.VideoListener;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection.Factory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.ParametersBuilder;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.twobigears.audio360.AudioEngine;
import com.twobigears.audio360.ChannelMap;
import com.twobigears.audio360.EngineInitSettings;
import com.twobigears.audio360.SpatDecoderQueue;
import com.twobigears.audio360.TBQuat;
import com.twobigears.audio360.TBVector;
import com.twobigears.audio360exo2.Audio360Sink;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class AVProVideoExoPlayer extends AVProVideoPlayer implements EventListener, VideoListener, AdaptiveMediaSourceEventListener, ExtractorMediaSource.EventListener, BandwidthMeter.EventListener {
    private static final String PREFERENCE_NAME = "com.off2.amaze.AVProVideo";
    private static final String SAVED_MAX_INITIAL_BITRATE_KEY = "max_init_bitrate";

    private Factory m_AdaptiveTrackSelectionFactory;
    private Handler m_MainHandler;
    private SimpleExoPlayer m_ExoPlayer;
    private DefaultTrackSelector m_TrackSelector;
    private EventLogger m_EventLogger;
    private String m_UserAgent;
    private com.google.android.exoplayer2.upstream.DataSource.Factory m_MediaDataSourceFactory;
    private String m_PendingFilePath;
    private long m_PendingFileOffset;
    private Surface m_Surface;
    float[] m_textureTransform;
    private SpatDecoderQueue m_Spat;
    private AudioEngine m_AudioEngine;
    private AudioSink m_Sink;
    private DefaultBandwidthMeter m_BandwidthMeter;
    private long m_LastAbsoluteTime;
    private static ChannelMap[] m_ChannelMap;
    private int m_ForcedFileFormat;

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
            this.m_Width = width;
            this.m_Height = height;
            this.m_bSourceHasVideo = true;
            this.m_bVideo_CreateRenderSurface = true;
            this.m_bVideo_DestroyRenderSurface = false;

            SharedPreferences sharedPref = m_Context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(SAVED_MAX_INITIAL_BITRATE_KEY, height > 2000 ? 20000000 : 800000);
            editor.apply();

            switch(unappliedRotationDegrees) {
                case 90:
                    this.m_textureTransform = new float[]{0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F};
                    return;
                case 180:
                    this.m_textureTransform = new float[]{-1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F};
                    return;
                case 270:
                    this.m_textureTransform = new float[]{0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F};
                    break;
                default:
                    return;
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

    protected boolean InitialisePlayer(final boolean enableAudio360, final int audio360Channels, final boolean preferSoftwareDecoder) {
        if (this.m_Context == null) {
            return false;
        } else {
            String version;
            try {
                String packageName = this.m_Context.getPackageName();
                version = this.m_Context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            } catch (NameNotFoundException var7) {
                version = "?";
            }

            this.m_UserAgent = "AVProMobileVideo/" + version + " (Linux;Android " + VERSION.RELEASE + ") ExoPlayerLib/2.8.4";
            final Activity activity = (Activity)((Activity)this.m_Context);
            activity.runOnUiThread(new Runnable() {
                public final void run() {
                    SharedPreferences sharedPref = m_Context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                    long maxInitBitrate = sharedPref.getLong(SAVED_MAX_INITIAL_BITRATE_KEY, 64000L);
                    System.out.println("AVProVideo: InitializePlayer with init bitrate " + maxInitBitrate);

                    AVProVideoExoPlayer.this.m_BandwidthMeter = new DefaultBandwidthMeter.Builder()
                            .setInitialBitrateEstimate(maxInitBitrate)
                            .setEventListener(new Handler(), AVProVideoExoPlayer.this)
                            .build();

                    AVProVideoExoPlayer.this.m_MainHandler = new Handler(activity.getMainLooper());
                    AVProVideoExoPlayer.this.m_MediaDataSourceFactory = AVProVideoExoPlayer.this.BuildDataSourceFactory(true);
                    AVProVideoExoPlayer.this.m_AdaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(
                            AVProVideoExoPlayer.this.m_BandwidthMeter,
                            2000,
                            AdaptiveTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS,
                            AdaptiveTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,
                            AdaptiveTrackSelection.DEFAULT_BANDWIDTH_FRACTION);

                    AVProVideoExoPlayer.this.m_TrackSelector = new DefaultTrackSelector(AVProVideoExoPlayer.this.m_AdaptiveTrackSelectionFactory);
                    AVProVideoExoPlayer.this.m_TrackSelector.setParameters(
                        AVProVideoExoPlayer.this.m_TrackSelector
                                .buildUponParameters()
                                .setMaxVideoSize(4096, 4096)
                                .setMaxVideoBitrate(1024*1024*1024)
                                .setExceedRendererCapabilitiesIfNecessary(false));

                    AVProVideoExoPlayer.this.m_EventLogger = new EventLogger(AVProVideoExoPlayer.this.m_TrackSelector);
                    CustomDefaultRenderersFactory defaultRenderersFactory;
                    RenderersFactory rFactory = defaultRenderersFactory = new CustomDefaultRenderersFactory(AVProVideoExoPlayer.this.m_Context, preferSoftwareDecoder);
                    if (enableAudio360) {
                        EngineInitSettings engineSettings;
                        (engineSettings = new EngineInitSettings()).getAudioSettings().setBufferSize(1024);
                        engineSettings.getAudioSettings().setSampleRate(48000.0F);
                        engineSettings.getMemorySettings().setSpatQueueSizePerChannel(8192);
                        AVProVideoExoPlayer.this.m_AudioEngine = AudioEngine.create(engineSettings, AVProVideoExoPlayer.this.m_Context);
                        AVProVideoExoPlayer.this.m_Spat = AVProVideoExoPlayer.this.m_AudioEngine.createSpatDecoderQueue();
                        double latency = AVProVideoExoPlayer.this.m_AudioEngine.getOutputLatencyMs();
                        ChannelMap channelMap = AVProVideoExoPlayer.this.getChannelMap(audio360Channels);
                        AVProVideoExoPlayer.this.m_Sink = new Audio360Sink(AVProVideoExoPlayer.this.m_Spat, channelMap, latency);
                        rFactory = new OpusRenderersFactory(AVProVideoExoPlayer.this.m_Sink, defaultRenderersFactory);
                    }

                    AVProVideoExoPlayer.this.m_ExoPlayer = ExoPlayerFactory.newSimpleInstance((RenderersFactory)rFactory, AVProVideoExoPlayer.this.m_TrackSelector);
                    AVProVideoExoPlayer.this.m_ExoPlayer.addListener(AVProVideoExoPlayer.this);
                    AVProVideoExoPlayer.this.m_ExoPlayer.addVideoListener(AVProVideoExoPlayer.this);
                    AVProVideoExoPlayer.this.m_ExoPlayer.addAnalyticsListener(new com.google.android.exoplayer2.util.EventLogger(AVProVideoExoPlayer.this.m_TrackSelector));
                }
            });
            return true;
        }
    }

    protected void CloseVideoOnPlayer() {
        this.m_LastAbsoluteTime = 0L;
        if (this.m_VideoState >= 3) {
            this._stop();
        }

        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.clearVideoSurface();
        }

        if (this.m_Surface != null) {
            this.m_Surface.release();
            this.m_Surface = null;
        }

        this.m_VideoState = 0;
        this.m_PendingFilePath = null;
        this.m_PendingFileOffset = 0L;
    }

    protected void DeinitializeVideoPlayer() {
        this.m_TrackSelector = null;
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

    private com.google.android.exoplayer2.upstream.HttpDataSource.Factory BuildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return new DefaultHttpDataSourceFactory(this.m_UserAgent, useBandwidthMeter ? this.m_BandwidthMeter : null, 8000, 8000, true);
    }

    private com.google.android.exoplayer2.upstream.DataSource.Factory BuildDataSourceFactory(boolean useBandwidthMeter) {
        return new DefaultDataSourceFactory(this.m_Context, useBandwidthMeter ? this.m_BandwidthMeter : null, this.BuildHttpDataSourceFactory(useBandwidthMeter));
    }

    protected void PlayerRendererSetup() {
        if (this.m_PendingFilePath != null && this.m_PendingFilePath.length() > 0) {
            this.OpenVideoFromFileInternal(this.m_PendingFilePath, this.m_PendingFileOffset, "", this.m_ForcedFileFormat);
        }

    }

    protected void PlayerRenderUpdate() {
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

    private MediaSource BuildMediaSource(String filepath, long fileOffset) {
        Uri uri = Uri.parse(filepath);
        String lowerPath;
        if (!(lowerPath = filepath.toLowerCase()).startsWith("jar:") && !lowerPath.contains(".zip!") && !lowerPath.contains(".obb!")) {
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

            System.out.println("AvPro BuildMediaSource: " + filepath);

            switch(type) {
                case 0:
                    return new DashMediaSource(uri, this.BuildDataSourceFactory(false), new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource.Factory(this.m_MediaDataSourceFactory), this.m_MainHandler, this);
                case 1:
                    return new SsMediaSource(uri, this.BuildDataSourceFactory(false), new com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource.Factory(this.m_MediaDataSourceFactory), this.m_MainHandler, this.m_EventLogger);
                case 2:
                    return new HlsMediaSource(uri, this.m_MediaDataSourceFactory, this.m_MainHandler, this);
                case 3:
                    return new ExtractorMediaSource(uri, this.BuildDataSourceFactory(false), new DefaultExtractorsFactory(), this.m_MainHandler, this.m_EventLogger);
                default:
                    throw new IllegalStateException("Unsupported type: " + type);
            }
        } else {
            return new ExtractorMediaSource(uri, new JarDataSourceFactory(filepath, fileOffset), new DefaultExtractorsFactory(), this.m_MainHandler, this.m_EventLogger);
        }
    }

    protected boolean OpenVideoFromFileInternal(String filePath, long fileOffset, String httpHeaderJson, int forcedFileFormat) {
        boolean success = false;
        this.m_ForcedFileFormat = forcedFileFormat;
        if (this.m_ExoPlayer != null && this.m_VideoState != 2 && this.m_SurfaceTexture != null) {
            MediaSource mediaSource;
            if ((mediaSource = this.BuildMediaSource(filePath, fileOffset)) != null) {
                this.m_LastAbsoluteTime = 0L;
                this.m_VideoState = 2;
                this.BindSurfaceToPlayer();
                this.m_ExoPlayer.prepare(mediaSource, true, true);
                success = true;
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
        if (this.m_ExoPlayer != null) {
            this.m_ExoPlayer.setPlayWhenReady(true);
            this.m_VideoState = 5;
            if (this.m_AudioEngine != null) {
                this.m_AudioEngine.start();
            }
        }

    }

    protected void _pause() {
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

            if (this.m_VideoState < 6) {
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
        if (this.m_ExoPlayer != null) {
            if (this.m_Surface != null) {
                this.m_ExoPlayer.clearVideoSurface();
                this.m_Surface.release();
                this.m_Surface = null;
            }

            this.m_Surface = new Surface(this.m_SurfaceTexture);
            this.m_ExoPlayer.setVideoSurface(this.m_Surface);
        }

    }

    public void onPlayerStateChanged(boolean playWhenReady, int state) {
        switch(state) {
            case 1:
                this.m_bIsBuffering = false;
                if (this.m_VideoState != 2) {
                    this.m_VideoState = 0;
                    return;
                }
                break;
            case 2:
                if (this.m_VideoState != 2) {
                    this.m_VideoState = 4;
                    this.m_bIsBuffering = true;
                    return;
                }

                System.out.println("AVProVideo buffer preparing");
                return;
            case 3:
                if (this.m_bIsBuffering) {
                    this.m_bIsBuffering = false;
                    this.m_VideoState = this.m_ExoPlayer.getPlayWhenReady() ? 5 : 7;
                    return;
                }

                if (this.m_PendingFilePath != null && this.m_PendingFilePath.length() > 0) {
                    this.OpenVideoFromFile(this.m_PendingFilePath, 0L, "", this.m_ForcedFileFormat);
                    return;
                }

                boolean bDoSetup = false;
                if (this.m_VideoState == 2) {
                    this.m_VideoState = 3;
                    Format videoFormat = this.m_ExoPlayer.getVideoFormat();
                    Format audioFormat = this.m_ExoPlayer.getAudioFormat();
                    if (videoFormat != null) {
                        this.m_fSourceVideoFrameRate = videoFormat.frameRate;
                        if (this.m_fSourceVideoFrameRate > 0.0F) {
                            this.m_DisplayRate_FrameRate = this.m_fSourceVideoFrameRate;
                        }

                        this.m_DurationMs = this.m_ExoPlayer.getDuration();
                        if (videoFormat.rotationDegrees == 90 || videoFormat.rotationDegrees == 270) {
                            this.m_Width = videoFormat.height;
                            this.m_Height = videoFormat.width;
                        }

                        if (this.m_Width > 0 && this.m_Height > 0) {
                            this.m_bSourceHasVideo = true;
                            this.m_bVideo_CreateRenderSurface = true;
                            this.m_bVideo_DestroyRenderSurface = false;
                        }

                        bDoSetup = true;
                    } else if (audioFormat != null) {
                        this.m_DisplayRate_FrameRate = this.m_fSourceVideoFrameRate = audioFormat.frameRate;
                        this.m_Width = 0;
                        this.m_Height = 0;
                        this.m_DurationMs = this.m_ExoPlayer.getDuration();
                        bDoSetup = true;
                    }
                }

                if (bDoSetup) {
                    this.m_VideoState = this.m_ExoPlayer.getPlayWhenReady() ? 5 : 7;
                    this.m_bVideo_AcceptCommands = true;
                }

                return;
            case 4:
                this.m_VideoState = 8;
            default:
                this.m_bIsBuffering = false;
        }

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
        if (this.m_VideoState < 5 && this.m_VideoState > 0) {
            this.m_iLastError = 100;
        }

    }

    public void onTracksChanged(TrackGroupArray ignored, TrackSelectionArray trackSelections) {
        this.m_iNumberAudioTracks = 0;
        if (this.m_ExoPlayer != null && this.m_TrackSelector != null) {
            MappedTrackInfo trackinfo;
            if ((trackinfo = this.m_TrackSelector.getCurrentMappedTrackInfo()) != null) {
                for(int i = 0; i < trackinfo.getRendererCount(); ++i) {
                    if (this.m_ExoPlayer.getRendererType(i) == 1) {
                        TrackGroupArray trackGroups = trackinfo.getTrackGroups(i);
                        this.m_iNumberAudioTracks += trackGroups != null ? trackGroups.length : 0;
                    }
                }

                if (this.m_iCurrentAudioTrackIndex < 0) {
                    this.m_iCurrentAudioTrackIndex = 0;
                }

            }
        }

        // DEBUG
        if (ignored != null) {
           for (int i = 0; i < ignored.length; i++) {
               TrackGroup tg = ignored.get(i);
               if (tg != null) {
                   for (int j = 0; j < tg.length; j++) {
                       Format f = tg.getFormat(j);
                       if (f != null) {
                           System.out.println("AVPro onTracksChanged: " + f.toString());
                       }
                   }
               }
           }
        }

         MappedTrackInfo mappedTrackInfo;
        if ((mappedTrackInfo = this.m_TrackSelector.getCurrentMappedTrackInfo()) != null) {
            int groupIndex;
            String adaptiveSupport;
            for(int rendererIndex = 0; rendererIndex < mappedTrackInfo.length; ++rendererIndex) {
                TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
                TrackSelection trackSelection = trackSelections.get(rendererIndex);
                if (rendererTrackGroups.length > 0) {
                    System.out.println((new StringBuilder("  Renderer:")).append(rendererIndex).append(" ["));

                    for(groupIndex = 0; groupIndex < rendererTrackGroups.length; ++groupIndex) {
                        TrackGroup trackGroup;
                        int var10000 = (trackGroup = rendererTrackGroups.get(groupIndex)).length;
                        int var14 = mappedTrackInfo.getAdaptiveSupport(rendererIndex, groupIndex, false);
                        String var21;
                        if (var10000 < 2) {
                            var21 = "N/A";
                        } else {
                            switch(var14) {
                                case 0:
                                    var21 = "NO";
                                    break;
                                case 8:
                                    var21 = "YES_NOT_SEAMLESS";
                                    break;
                                case 16:
                                    var21 = "YES";
                                    break;
                                default:
                                    var21 = "?";
                            }
                        }

                        adaptiveSupport = var21;
                        System.out.println((new StringBuilder("    Group:")).append(groupIndex).append(", adaptive_supported=").append(adaptiveSupport).append(" ["));

                        for(int trackIndex = 0; trackIndex < trackGroup.length; ++trackIndex) {
                            String status = getTrackStatusString(trackSelection != null && trackSelection.getTrackGroup() == trackGroup && trackSelection.indexOf(trackIndex) != -1);
                            String formatSupport = getFormatSupportString(mappedTrackInfo.getTrackFormatSupport(rendererIndex, groupIndex, trackIndex));
                            System.out.println((new StringBuilder("      ")).append(status).append(" Track:").append(trackIndex).append(", ").append(Format.toLogString(trackGroup.getFormat(trackIndex))).append(", supported=").append(formatSupport));
                        }
                    }
//
//                    if (trackSelection != null) {
//                        for(groupIndex = 0; groupIndex < trackSelection.length(); ++groupIndex) {
//                            Metadata metadata;
//                            if ((metadata = trackSelection.getFormat(groupIndex).metadata) != null) {
//                                printMetadata(metadata, "      ");
//                                break;
//                            }
//                        }
//                    }
                }
            }

            TrackGroupArray unassociatedTrackGroups;
            if ((unassociatedTrackGroups = mappedTrackInfo.getUnassociatedTrackGroups()).length > 0) {
                for(groupIndex = 0; groupIndex < unassociatedTrackGroups.length; ++groupIndex) {
                    System.out.println((new StringBuilder("    Group:")).append(groupIndex).append(" ["));
                    TrackGroup trackGroup = unassociatedTrackGroups.get(groupIndex);

                    for(int groupIndex2 = 0; groupIndex2 < trackGroup.length; ++groupIndex2) {
                        String status = getTrackStatusString(false);
                        adaptiveSupport = getFormatSupportString(0);
                        System.out.println((new StringBuilder("      ")).append(status).append(" Track:").append(groupIndex2).append(", ").append(Format.toLogString(trackGroup.getFormat(groupIndex2))).append(", supported=").append(adaptiveSupport));
                    }
                }
            }

        }
    }

    private static String getFormatSupportString(int formatSupport) {
        switch(formatSupport) {
            case 0:
                return "NO";
            case 1:
                return "NO_UNSUPPORTED_TYPE";
            case 2:
            default:
                return "?";
            case 3:
                return "NO_EXCEEDS_CAPABILITIES";
            case 4:
                return "YES";
        }
    }

    private static String getTrackStatusString(boolean enabled) {
        return enabled ? "[X]" : "[ ]";
    }
    public void onLoadError(IOException error) {
        if (this.m_VideoState > 5 && this.m_VideoState > 0) {
            this.m_iLastError = 100;
        }

    }

    public void onLoadError(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
    }

    public void onLoadCanceled(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public void onLoadStarted(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        if (mediaLoadData != null && mediaLoadData.trackFormat != null) {
//            System.out.println("AVPro onLoadStarted: " + mediaLoadData.trackFormat.toString());
        }
    }

    public void onLoadCompleted(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        if (mediaLoadData != null && mediaLoadData.trackFormat != null) {
            System.out.println("AVPro onLoadCompleted: " + mediaLoadData.trackFormat.toString());
        }
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
        if (this.m_Context != null) {
            ((Activity)this.m_Context).runOnUiThread(new Runnable() {
                public final void run() {
                    Format videoFormat;
                    if (AVProVideoExoPlayer.this.m_fSourceVideoFrameRate < 0.0F && AVProVideoExoPlayer.this.m_ExoPlayer != null && (videoFormat = AVProVideoExoPlayer.this.m_ExoPlayer.getVideoFormat()) != null) {
                        AVProVideoExoPlayer.this.m_fSourceVideoFrameRate = videoFormat.frameRate;
                    }

                }
            });
        }
    }

    @Override
    public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
        System.out.printf("AVPro: Bitrate Estimate: %d (%d bytes in %dms)\n", bitrate, bytes, elapsedMs);
    }
}
