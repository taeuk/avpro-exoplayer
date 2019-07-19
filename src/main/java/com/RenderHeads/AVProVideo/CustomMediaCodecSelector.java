//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import java.util.List;

public class CustomMediaCodecSelector implements MediaCodecSelector {
    private boolean m_PreferSoftware;

    public CustomMediaCodecSelector(boolean preferSoftware) {
        this.m_PreferSoftware = preferSoftware;
    }

    public MediaCodecInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws DecoderQueryException {
        if (mimeType.contains("video") && this.m_PreferSoftware) {
            List<MediaCodecInfo> videoCodecs = MediaCodecUtil.getDecoderInfos(mimeType, requiresSecureDecoder);
            MediaCodecInfo ret = null;

            for(int i = 0; i < videoCodecs.size(); ++i) {
                if (((MediaCodecInfo)videoCodecs.get(i)).name.toLowerCase().startsWith("omx.google")) {
                    ret = (MediaCodecInfo)videoCodecs.get(i);
                }
            }

            return ret;
        } else {
            return MediaCodecSelector.DEFAULT.getDecoderInfo(mimeType, requiresSecureDecoder);
        }
    }

    public MediaCodecInfo getPassthroughDecoderInfo() throws DecoderQueryException {
        return MediaCodecSelector.DEFAULT.getPassthroughDecoderInfo();
    }
}
