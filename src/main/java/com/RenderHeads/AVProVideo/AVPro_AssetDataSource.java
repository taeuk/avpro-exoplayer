//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.AssetDataSource.AssetDataSourceException;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class AVPro_AssetDataSource implements DataSource {
    private final AssetManager m_AssetManager;
    private Uri uri;
    private InputStream inputStream;
    private long bytesRemaining;
    private boolean opened;
    private long m_FileOffset;

    public AVPro_AssetDataSource(long fileOffset, Context context) {
        this.m_AssetManager = context.getAssets();
        this.m_FileOffset = fileOffset;
    }

    public void addTransferListener(TransferListener transferListener) {
    }

    public void close() {
        this.uri = null;

        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }

            return;
        } catch (IOException var4) {
        } finally {
            this.inputStream = null;
            if (this.opened) {
                this.opened = false;
            }

        }

    }

    public Uri getUri() {
        return this.uri;
    }

    public long open(DataSpec dataSpec) throws AssetDataSourceException {
        try {
            this.uri = dataSpec.uri;
            String path;
            if ((path = this.uri.getPath()).startsWith("/android_asset/")) {
                path = path.substring(15);
            } else if (path.startsWith("/")) {
                path = path.substring(1);
            }

            this.inputStream = this.m_AssetManager.open(path, 1);
            long iSkipAmount = dataSpec.position + this.m_FileOffset;
            if (this.inputStream.skip(iSkipAmount) < iSkipAmount) {
                throw new EOFException();
            }

            if (dataSpec.length != -1L) {
                this.bytesRemaining = dataSpec.length;
            } else {
                this.bytesRemaining = (long)this.inputStream.available();
                if (this.bytesRemaining == 2147483647L) {
                    this.bytesRemaining = -1L;
                }
            }
        } catch (IOException var5) {
        }

        this.opened = true;
        return this.bytesRemaining;
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        } else if (this.bytesRemaining == 0L) {
            return -1;
        } else {
            int bytesRead = -1;

            try {
                int bytesToRead = this.bytesRemaining == -1L ? readLength : (int)Math.min(this.bytesRemaining, (long)readLength);
                bytesRead = this.inputStream.read(buffer, offset, bytesToRead);
            } catch (IOException var6) {
            }

            if (bytesRead == -1) {
                if (this.bytesRemaining != -1L) {
                    throw new EOFException();
                } else {
                    return -1;
                }
            } else {
                if (this.bytesRemaining != -1L) {
                    this.bytesRemaining -= (long)bytesRead;
                }

                return bytesRead;
            }
        }
    }
}
