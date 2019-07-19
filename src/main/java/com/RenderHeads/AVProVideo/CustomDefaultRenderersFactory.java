//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.content.Context;
import android.os.Handler;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomDefaultRenderersFactory implements RenderersFactory {
    private boolean m_PreferSoftware;
    private CustomMediaCodecSelector m_CodecSelector;
    private RenderersFactory m_DefaultRenderersFactory;
    private Context m_Context;
    private int m_ExtensionRenderersMode;
    private DrmSessionManager<FrameworkMediaCrypto> m_DrmSessionManager;

    public CustomDefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode, boolean preferSoftware) {
        this.m_PreferSoftware = preferSoftware;
        this.m_CodecSelector = new CustomMediaCodecSelector(this.m_PreferSoftware);
        this.m_DefaultRenderersFactory = new DefaultRenderersFactory(context, drmSessionManager, extensionRendererMode);
        this.m_Context = context;
        this.m_ExtensionRenderersMode = extensionRendererMode;
        this.m_DrmSessionManager = drmSessionManager;
    }

    public Renderer[] createRenderers(Handler eventHandler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textRendererOutput, MetadataOutput metadataRendererOutput, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        Renderer[] defaultRenderers = this.m_DefaultRenderersFactory.createRenderers(eventHandler, videoRendererEventListener, audioRendererEventListener, textRendererOutput, metadataRendererOutput, drmSessionManager);
        if (!this.m_PreferSoftware) {
            return defaultRenderers;
        } else {
            ArrayList<Renderer> defaultRenderersList = new ArrayList(Arrays.asList(defaultRenderers));
            ArrayList<Renderer> renderers = new ArrayList();

            int extensionRendererIndex;
            for(extensionRendererIndex = 0; extensionRendererIndex < defaultRenderersList.size(); ++extensionRendererIndex) {
                if (((Renderer)defaultRenderersList.get(extensionRendererIndex)).getTrackType() != 2) {
                    renderers.add(defaultRenderersList.get(extensionRendererIndex));
                }
            }

            renderers.add(new MediaCodecVideoRenderer(this.m_Context, this.m_CodecSelector, 5000L, this.m_DrmSessionManager, false, eventHandler, videoRendererEventListener, 50));
            if (this.m_ExtensionRenderersMode != 0) {
                extensionRendererIndex = renderers.size();
                if (this.m_ExtensionRenderersMode == 2) {
                    --extensionRendererIndex;
                }

                try {
                    Renderer renderer = (Renderer)Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(Boolean.TYPE, Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(true, 5000, eventHandler, videoRendererEventListener, 50);
                    renderers.add(extensionRendererIndex, renderer);
                } catch (ClassNotFoundException var13) {
                } catch (Exception var14) {
                    throw new RuntimeException("Error instantiating VP9 extension", var14);
                }
            }

            return (Renderer[])renderers.toArray(new Renderer[renderers.size()]);
        }
    }
}
