//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;

public class AVPro_FileDataSourceFactory implements Factory {
    private long m_FileOffset;

    public AVPro_FileDataSourceFactory(long fileOffset) {
        this.m_FileOffset = fileOffset;
    }

    public DataSource createDataSource() {
        return new AVPro_FileDataSource(this.m_FileOffset);
    }
}
