//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.content.Context;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;

public class AVPro_AssetSourceFactory implements Factory {
    private String m_FilePath;
    private long m_FileOffset;
    private Context m_Context;

    public AVPro_AssetSourceFactory(long fileOffset, Context context) {
        this.m_FileOffset = fileOffset;
        this.m_Context = context;
    }

    public DataSource createDataSource() {
        return new AVPro_AssetDataSource(this.m_FileOffset, this.m_Context);
    }
}
