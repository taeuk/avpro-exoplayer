//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.os.Handler;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.twobigears.audio360exo2.OpusRenderer;

public class OpusRenderersFactory implements RenderersFactory {
    private AudioSink m_Sink;
    private RenderersFactory m_RenderersFactory;

    public OpusRenderersFactory(AudioSink sink, RenderersFactory renderersFactory) {
        this.m_Sink = sink;
        this.m_RenderersFactory = renderersFactory;
    }

    public Renderer[] createRenderers(Handler eventHandler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textRendererOutput, MetadataOutput metadataRendererOutput, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        Renderer[] renderers = this.m_RenderersFactory.createRenderers(eventHandler, videoRendererEventListener, audioRendererEventListener, textRendererOutput, metadataRendererOutput, drmSessionManager);

        for(int i = 0; i < renderers.length; ++i) {
            if (renderers[i].getTrackType() == 1) {
                renderers[i] = new OpusRenderer(this.m_Sink);
            }
        }

        return renderers;
    }
}
