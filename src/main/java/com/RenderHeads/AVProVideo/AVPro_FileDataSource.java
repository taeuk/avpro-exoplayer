//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class AVPro_FileDataSource implements DataSource {
    private final TransferListener<? super AVPro_FileDataSource> listener;
    private RandomAccessFile file;
    private Uri uri;
    private long bytesRemaining;
    private boolean opened;
    private long m_FileOffset;

    public AVPro_FileDataSource() {
        this((TransferListener)null);
    }

    public AVPro_FileDataSource(long fileOffset) {
        this((TransferListener)null);
        this.m_FileOffset = fileOffset;
    }

    public AVPro_FileDataSource(TransferListener<? super AVPro_FileDataSource> listener) {
        this.listener = listener;
    }

    public final long open(DataSpec dataSpec) throws FileDataSourceException {
        try {
            this.uri = dataSpec.uri;
            this.file = new RandomAccessFile(dataSpec.uri.getPath(), "r");
            long iSkipAmount = dataSpec.position + this.m_FileOffset;
            this.file.seek(iSkipAmount);
            this.bytesRemaining = dataSpec.length == -1L ? this.file.length() - dataSpec.position - this.m_FileOffset : dataSpec.length;
            if (this.bytesRemaining < 0L) {
                throw new EOFException();
            }
        } catch (IOException var4) {
            throw new FileDataSourceException(var4);
        }

        this.opened = true;
        if (this.listener != null) {
            this.listener.onTransferStart(this, dataSpec);
        }

        return this.bytesRemaining;
    }

    public final int read(byte[] buffer, int offset, int readLength) throws FileDataSourceException {
        if (readLength == 0) {
            return 0;
        } else if (this.bytesRemaining == 0L) {
            return -1;
        } else {
            int bytesRead;
            try {
                bytesRead = this.file.read(buffer, offset, (int)Math.min(this.bytesRemaining, (long)readLength));
            } catch (IOException var6) {
                throw new FileDataSourceException(var6);
            }

            if (bytesRead > 0) {
                this.bytesRemaining -= (long)bytesRead;
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this, bytesRead);
                }
            }

            return bytesRead;
        }
    }

    public final Uri getUri() {
        return this.uri;
    }

    public final void close() throws FileDataSourceException {
        this.uri = null;

        try {
            if (this.file != null) {
                this.file.close();
            }
        } catch (IOException var5) {
            throw new FileDataSourceException(var5);
        } finally {
            this.file = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }

        }

    }
}
