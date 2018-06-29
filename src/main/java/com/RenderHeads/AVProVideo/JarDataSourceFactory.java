//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;

public final class JarDataSourceFactory implements Factory {
    private String m_Path;

    public JarDataSourceFactory(String path) {
        this.m_Path = path;
    }

    public final DataSource createDataSource() {
        return new JarDataSource(this.m_Path);
    }
}