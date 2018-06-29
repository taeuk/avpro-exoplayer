//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.Metadata.Entry;
import com.google.android.exoplayer2.metadata.MetadataRenderer.Output;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.GeobFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.id3.UrlLinkFrame;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

final class EventLogger implements EventListener, AudioRendererEventListener, com.google.android.exoplayer2.drm.DefaultDrmSessionManager.EventListener, Output, AdaptiveMediaSourceEventListener, com.google.android.exoplayer2.source.ExtractorMediaSource.EventListener, VideoRendererEventListener {
    private static final NumberFormat TIME_FORMAT;
    private final MappingTrackSelector trackSelector;
    private final Window window;
    private final Period period;
    private final long startTimeMs;

    public EventLogger(MappingTrackSelector trackSelector) {
        this.trackSelector = trackSelector;
        this.window = new Window();
        this.period = new Period();
        this.startTimeMs = SystemClock.elapsedRealtime();
    }

    public final void onLoadingChanged(boolean isLoading) {
        (new StringBuilder("loading [")).append(isLoading).append("]");
    }

    public final void onPlayerStateChanged(boolean playWhenReady, int state) {
        StringBuilder var10000 = (new StringBuilder("state [")).append(this.getSessionTimeString()).append(", ").append(playWhenReady).append(", ");
        String var10001;
        switch(state) {
            case 1:
                var10001 = "I";
                break;
            case 2:
                var10001 = "B";
                break;
            case 3:
                var10001 = "R";
                break;
            case 4:
                var10001 = "E";
                break;
            default:
                var10001 = "?";
        }

        var10000.append(var10001).append("]");
    }

    public final void onRepeatModeChanged(int repeatMode) {
        StringBuilder var10000 = new StringBuilder("repeatMode [");
        String var10001;
        switch(repeatMode) {
            case 0:
                var10001 = "OFF";
                break;
            case 1:
                var10001 = "ONE";
                break;
            case 2:
                var10001 = "ALL";
                break;
            default:
                var10001 = "?";
        }

        var10000.append(var10001).append("]");
    }

    public final void onPositionDiscontinuity(int reason) {
    }

    public final void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        (new StringBuilder("playbackParameters ")).append(String.format("[speed=%.2f, pitch=%.2f]", playbackParameters.speed, playbackParameters.pitch));
    }

    public final void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    public final void onSeekProcessed() {
    }

    public final void onTimelineChanged(Timeline timeline, Object manifest) {
        int periodCount = timeline.getPeriodCount();
        int windowCount = timeline.getWindowCount();
        (new StringBuilder("sourceInfo [periodCount=")).append(periodCount).append(", windowCount=").append(windowCount);

        int i;
        for(i = 0; i < Math.min(periodCount, 3); ++i) {
            timeline.getPeriod(i, this.period);
            (new StringBuilder("  period [")).append(getTimeString(this.period.getDurationMs())).append("]");
        }

        for(i = 0; i < Math.min(windowCount, 3); ++i) {
            timeline.getWindow(i, this.window);
            (new StringBuilder("  window [")).append(getTimeString(this.window.getDurationMs())).append(", ").append(this.window.isSeekable).append(", ").append(this.window.isDynamic).append("]");
        }

    }

    public final void onPlayerError(ExoPlaybackException e) {
        Log.e("AVProVideo: EventLogger", "playerFailed [" + this.getSessionTimeString() + "]", e);
    }

    public final void onTracksChanged(TrackGroupArray ignored, TrackSelectionArray trackSelections) {
        MappedTrackInfo mappedTrackInfo;
        if ((mappedTrackInfo = this.trackSelector.getCurrentMappedTrackInfo()) != null) {
            int groupIndex;
            String adaptiveSupport;
            for(int rendererIndex = 0; rendererIndex < mappedTrackInfo.length; ++rendererIndex) {
                TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
                TrackSelection trackSelection = trackSelections.get(rendererIndex);
                if (rendererTrackGroups.length > 0) {
                    (new StringBuilder("  Renderer:")).append(rendererIndex).append(" [");

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
                        (new StringBuilder("    Group:")).append(groupIndex).append(", adaptive_supported=").append(adaptiveSupport).append(" [");

                        for(int trackIndex = 0; trackIndex < trackGroup.length; ++trackIndex) {
                            String status = getTrackStatusString(trackSelection != null && trackSelection.getTrackGroup() == trackGroup && trackSelection.indexOf(trackIndex) != -1);
                            String formatSupport = getFormatSupportString(mappedTrackInfo.getTrackFormatSupport(rendererIndex, groupIndex, trackIndex));
                            (new StringBuilder("      ")).append(status).append(" Track:").append(trackIndex).append(", ").append(Format.toLogString(trackGroup.getFormat(trackIndex))).append(", supported=").append(formatSupport);
                        }
                    }

                    if (trackSelection != null) {
                        for(groupIndex = 0; groupIndex < trackSelection.length(); ++groupIndex) {
                            Metadata metadata;
                            if ((metadata = trackSelection.getFormat(groupIndex).metadata) != null) {
                                printMetadata(metadata, "      ");
                                break;
                            }
                        }
                    }
                }
            }

            TrackGroupArray unassociatedTrackGroups;
            if ((unassociatedTrackGroups = mappedTrackInfo.getUnassociatedTrackGroups()).length > 0) {
                for(groupIndex = 0; groupIndex < unassociatedTrackGroups.length; ++groupIndex) {
                    (new StringBuilder("    Group:")).append(groupIndex).append(" [");
                    TrackGroup trackGroup = unassociatedTrackGroups.get(groupIndex);

                    for(groupIndex = 0; groupIndex < trackGroup.length; ++groupIndex) {
                        String status = getTrackStatusString(false);
                        adaptiveSupport = getFormatSupportString(0);
                        (new StringBuilder("      ")).append(status).append(" Track:").append(groupIndex).append(", ").append(Format.toLogString(trackGroup.getFormat(groupIndex))).append(", supported=").append(adaptiveSupport);
                    }
                }
            }

        }
    }

    public final void onMetadata(Metadata metadata) {
        printMetadata(metadata, "  ");
    }

    public final void onAudioEnabled(DecoderCounters counters) {
        (new StringBuilder("audioEnabled [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onAudioSessionId(int audioSessionId) {
        (new StringBuilder("audioSessionId [")).append(audioSessionId).append("]");
    }

    public final void onAudioDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        (new StringBuilder("audioDecoderInitialized [")).append(this.getSessionTimeString()).append(", ").append(decoderName).append("]");
    }

    public final void onAudioInputFormatChanged(Format format) {
        (new StringBuilder("audioFormatChanged [")).append(this.getSessionTimeString()).append(", ").append(Format.toLogString(format)).append("]");
    }

    public final void onAudioDisabled(DecoderCounters counters) {
        (new StringBuilder("audioDisabled [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        this.printInternalError("audioTrackUnderrun [" + bufferSize + ", " + bufferSizeMs + ", " + elapsedSinceLastFeedMs + "]", (Exception)null);
    }

    public final void onVideoEnabled(DecoderCounters counters) {
        (new StringBuilder("videoEnabled [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onVideoDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        (new StringBuilder("videoDecoderInitialized [")).append(this.getSessionTimeString()).append(", ").append(decoderName).append("]");
    }

    public final void onVideoInputFormatChanged(Format format) {
        (new StringBuilder("videoFormatChanged [")).append(this.getSessionTimeString()).append(", ").append(Format.toLogString(format)).append("]");
    }

    public final void onVideoDisabled(DecoderCounters counters) {
        (new StringBuilder("videoDisabled [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onDroppedFrames(int count, long elapsed) {
        (new StringBuilder("droppedFrames [")).append(this.getSessionTimeString()).append(", ").append(count).append("]");
    }

    public final void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        (new StringBuilder("videoSizeChanged [")).append(width).append(", ").append(height).append("]");
    }

    public final void onRenderedFirstFrame(Surface surface) {
        (new StringBuilder("renderedFirstFrame [")).append(surface).append("]");
    }

    public final void onDrmSessionManagerError(Exception e) {
        this.printInternalError("drmSessionManagerError", e);
    }

    public final void onDrmKeysRestored() {
        (new StringBuilder("drmKeysRestored [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onDrmKeysRemoved() {
        (new StringBuilder("drmKeysRemoved [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onDrmKeysLoaded() {
        (new StringBuilder("drmKeysLoaded [")).append(this.getSessionTimeString()).append("]");
    }

    public final void onLoadError(IOException error) {
        this.printInternalError("loadError", error);
    }

    public final void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
    }

    public final void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
        this.printInternalError("loadError", error);
    }

    public final void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
    }

    public final void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
    }

    public final void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {
    }

    public final void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {
    }

    private void printInternalError(String type, Exception e) {
        Log.e("AVProVideo: EventLogger", "internalError [" + this.getSessionTimeString() + ", " + type + "]", e);
    }

    private static void printMetadata(Metadata metadata, String prefix) {
        for(int i = 0; i < metadata.length(); ++i) {
            Entry entry;
            if ((entry = metadata.get(i)) instanceof TextInformationFrame) {
                TextInformationFrame textInformationFrame = (TextInformationFrame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s: value=%s", textInformationFrame.id, textInformationFrame.value));
            } else if (entry instanceof UrlLinkFrame) {
                UrlLinkFrame urlLinkFrame = (UrlLinkFrame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s: url=%s", urlLinkFrame.id, urlLinkFrame.url));
            } else if (entry instanceof PrivFrame) {
                PrivFrame privFrame = (PrivFrame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s: owner=%s", privFrame.id, privFrame.owner));
            } else if (entry instanceof GeobFrame) {
                GeobFrame geobFrame = (GeobFrame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s: mimeType=%s, filename=%s, description=%s", geobFrame.id, geobFrame.mimeType, geobFrame.filename, geobFrame.description));
            } else if (entry instanceof ApicFrame) {
                ApicFrame apicFrame = (ApicFrame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s: mimeType=%s, description=%s", apicFrame.id, apicFrame.mimeType, apicFrame.description));
            } else if (entry instanceof CommentFrame) {
                CommentFrame commentFrame = (CommentFrame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s: language=%s, description=%s", commentFrame.id, commentFrame.language, commentFrame.description));
            } else if (entry instanceof Id3Frame) {
                Id3Frame id3Frame = (Id3Frame)entry;
                (new StringBuilder()).append(prefix).append(String.format("%s", id3Frame.id));
            } else if (entry instanceof EventMessage) {
                EventMessage eventMessage = (EventMessage)entry;
                (new StringBuilder()).append(prefix).append(String.format("EMSG: scheme=%s, id=%d, value=%s", eventMessage.schemeIdUri, eventMessage.id, eventMessage.value));
            }
        }

    }

    private String getSessionTimeString() {
        return getTimeString(SystemClock.elapsedRealtime() - this.startTimeMs);
    }

    private static String getTimeString(long timeMs) {
        return timeMs == -9223372036854775807L ? "?" : TIME_FORMAT.format((double)((float)timeMs / 1000.0F));
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

    static {
        (TIME_FORMAT = NumberFormat.getInstance(Locale.US)).setMinimumFractionDigits(2);
        TIME_FORMAT.setMaximumFractionDigits(2);
        TIME_FORMAT.setGroupingUsed(false);
    }
}
