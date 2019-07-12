//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.AssetDataSource.AssetDataSourceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class JarDataSource implements DataSource {
    private static final String[] extensions = new String[]{"obb!/", "apk!/"};
    private Uri m_Uri;
    private String m_Path;
    private long m_FileOffset;
    private InputStream m_File;
    private ZipFile m_ZipFile;

    public JarDataSource(String path, long fileOffset) {
        this.m_Path = path;
        this.m_FileOffset = fileOffset;
        this.m_Uri = Uri.parse(path);
        this.m_File = null;
        this.m_ZipFile = null;
    }

    public final void close() {
        if (this.m_File != null) {
            try {
                this.m_File.close();
            } catch (IOException var3) {
                System.err.println(var3.getMessage());
            }
        }

        if (this.m_ZipFile != null) {
            try {
                this.m_ZipFile.close();
            } catch (IOException var2) {
                System.err.println(var2.getMessage());
            }
        }

        this.m_ZipFile = null;
        this.m_File = null;
    }

    public final Uri getUri() {
        return this.m_Uri;
    }

    public final long open(DataSpec dataSpec) throws AssetDataSourceException {
        if (this.m_Uri == null) {
            return 0L;
        } else {
            for(int i = 0; i < extensions.length; ++i) {
                String lookFor = extensions[i];
                int iIndexIntoString;
                if ((iIndexIntoString = this.m_Path.lastIndexOf(lookFor)) >= 0) {
                    String zipPathName = this.m_Path.substring(11, iIndexIntoString + lookFor.length() - 2);
                    String zipFileName = this.m_Path.substring(iIndexIntoString + lookFor.length());

                    long fileSize;
                    try {
                        this.m_ZipFile = new ZipFile(zipPathName);
                        ZipEntry entry;
                        if ((entry = this.m_ZipFile.getEntry(zipFileName)) == null) {
                            throw new AssetDataSourceException(new IOException("Unable to locate file " + zipFileName + " in zip " + zipPathName));
                        }

                        this.m_File = this.m_ZipFile.getInputStream(entry);
                        fileSize = entry.getSize() - this.m_FileOffset;
                        if (this.m_File.skip(dataSpec.position + this.m_FileOffset) < dataSpec.position) {
                            throw new AssetDataSourceException(new IOException("End of file reached"));
                        }
                    } catch (IOException var10) {
                        throw new AssetDataSourceException(var10);
                    }

                    if (this.m_File != null) {
                        if (dataSpec.length == -1L) {
                            return fileSize;
                        }

                        return dataSpec.length;
                    }
                }
            }

            return 0L;
        }
    }

    public final int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (this.m_File == null) {
            return 0;
        } else {
            long bytesToRead = (long)Math.min(readLength, this.m_File.available());
            return this.m_File.read(buffer, offset, (int)bytesToRead);
        }
    }
}
