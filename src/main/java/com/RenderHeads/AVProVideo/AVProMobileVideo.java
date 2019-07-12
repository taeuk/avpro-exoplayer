//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;

public class AVProMobileVideo {
    private final boolean m_bWatermarked = false;
    private final String PLUGIN_VERSION = "1.9.10";
    public static final int MEDIAPLAYER = 1;
    public static final int EXOPLAYER = 2;
    public static final int kUnityGfxRendererOpenGLES20 = 8;
    public static final int kUnityGfxRendererOpenGLES30 = 11;
    private static boolean s_PreviousContextFail = false;
    private static int s_PreviousDeviceIndex = -1;
    private Map<Integer, AVProVideoPlayer> m_Players = new HashMap();
    private Random m_Random = new Random();
    private Context m_Context = null;
    private int m_iOpenGLVersion = -1;
    private static AVProMobileVideo s_Interface = null;
    private static List<AVProVideoPlayer> _renderFree = new ArrayList();
    private static final ReentrantLock _renderMutex = new ReentrantLock();

    public AVProMobileVideo() {
        if (s_Interface == null) {
            s_Interface = this;
        }

    }

    public static void Deinitialise() {
        s_Interface = null;
    }

    public void SetContext(Context context) {
        this.m_Context = context;
        int iPackageManagerOpenGLESVersion = getVersionFromPackageManager(this.m_Context);
        int iDeviceInfoOpenGLESVersion = getGlVersionFromDeviceConfig(this.m_Context);
        if (iPackageManagerOpenGLESVersion >= 3 && iDeviceInfoOpenGLESVersion >= 3) {
            this.m_iOpenGLVersion = 3;
        } else {
            if (iPackageManagerOpenGLESVersion >= 2 && iDeviceInfoOpenGLESVersion >= 2) {
                this.m_iOpenGLVersion = 2;
            }

        }
    }

    public String GetPluginVersion() {
        return "1.9.10";
    }

    public AVProVideoPlayer CreatePlayer(int api, boolean useOesRenderingPath, boolean enableAudio360, int audio360Channels, boolean preferSoftware) {
        if (s_Interface != this) {
            return null;
        } else {
            int index = -1;

            for(int i = 0; i < 256; ++i) {
                if (!this.m_Players.containsKey(i)) {
                    index = i;
                    break;
                }
            }

            if (index < 0) {
                return null;
            } else {
                switch(api) {
                    case 1:
                        AVProVideoMediaPlayer mediaplayer;
                        (mediaplayer = new AVProVideoMediaPlayer(index, false, this.m_Random)).Initialise(this.m_Context, useOesRenderingPath, enableAudio360, audio360Channels, preferSoftware);
                        this.m_Players.put(index, mediaplayer);
                        return (AVProVideoPlayer)this.m_Players.get(index);
                    case 2:
                        AVProVideoExoPlayer exoplayer;
                        (exoplayer = new AVProVideoExoPlayer(index, false, this.m_Random)).Initialise(this.m_Context, useOesRenderingPath, enableAudio360, audio360Channels, preferSoftware);
                        this.m_Players.put(index, exoplayer);
                        return (AVProVideoPlayer)this.m_Players.get(index);
                    default:
                        return null;
                }
            }
        }
    }

    public void DestroyPlayer(int playerIndex) {
        AVProVideoPlayer theClass;
        if ((theClass = this.GetAVProClassForPlayerIndex(playerIndex)) != null) {
            theClass.Deinitialise();
            this.RemovePlayer(playerIndex);
            _renderMutex.lock();
            _renderFree.add(theClass);
            _renderMutex.unlock();
        }

    }

    private void RemovePlayer(int playerIndex) {
        if (this.m_Players.containsKey(playerIndex)) {
            this.m_Players.remove(playerIndex);
        }

    }

    private AVProVideoPlayer GetAVProClassForPlayerIndex(int playerIndex) {
        AVProVideoPlayer returnPlayerClass = null;
        if (this.m_Players != null ? this.m_Players.containsKey(playerIndex) : false) {
            returnPlayerClass = (AVProVideoPlayer)this.m_Players.get(playerIndex);
        }

        return returnPlayerClass;
    }

    public static void RenderPlayer(int playerIndex) {
        if (s_Interface != null) {
            if (((EGL10)EGLContext.getEGL()).eglGetCurrentContext().equals(EGL10.EGL_NO_CONTEXT)) {
                s_PreviousContextFail = true;
            } else {
                if (s_PreviousContextFail && s_PreviousDeviceIndex >= 0) {
                    RendererReset(s_PreviousDeviceIndex);
                    s_PreviousContextFail = false;
                }

                AVProVideoPlayer theClass;
                if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                    theClass.Render();
                }

            }
        }
    }

    public static void WaitForNewFramePlayer(int playerIndex) {
        if (s_Interface != null) {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                theClass.WaitForNewFrame();
            }

        }
    }

    public static void RendererSetupPlayer(int playerIndex, int iDeviceIndex) {
        if (s_Interface != null) {
            s_PreviousDeviceIndex = iDeviceIndex;
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                int glesVersion = s_Interface.m_iOpenGLVersion;
                if (iDeviceIndex == 8) {
                    glesVersion = 2;
                } else if (iDeviceIndex == 11) {
                    glesVersion = 3;
                }

                theClass.RendererSetup(glesVersion);
            }

        }
    }

    public static void RendererDestroyPlayers() {
        _renderMutex.lock();

        for(int i = 0; i < _renderFree.size(); ++i) {
            AVProVideoPlayer theClass;
            if ((theClass = (AVProVideoPlayer)_renderFree.get(i)) != null) {
                theClass.DeinitialiseRender();
            }
        }

        _renderFree.clear();
        _renderMutex.unlock();
    }

    public static int _GetWidth(int playerIndex) {
        int iReturn = 0;
        if (s_Interface == null) {
            return 0;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                iReturn = theClass.GetWidth();
            }

            return iReturn;
        }
    }

    public static int _GetHeight(int playerIndex) {
        int iReturn = 0;
        if (s_Interface == null) {
            return 0;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                iReturn = theClass.GetHeight();
            }

            return iReturn;
        }
    }

    public static int _GetTextureHandle(int playerIndex) {
        int iReturn = 0;
        if (s_Interface == null) {
            return 0;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                iReturn = theClass.GetTextureHandle();
            }

            return iReturn;
        }
    }

    public static long _GetDuration(int playerIndex) {
        long iReturn = 0L;
        if (s_Interface == null) {
            return 0L;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                iReturn = theClass.GetDurationMs();
            }

            return iReturn;
        }
    }

    public static int _GetLastErrorCode(int playerIndex) {
        int iReturn = 0;
        if (s_Interface == null) {
            return 0;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                iReturn = theClass.GetLastErrorCode();
            }

            return iReturn;
        }
    }

    public static int _GetFrameCount(int playerIndex) {
        int iReturn = 0;
        if (s_Interface == null) {
            return 0;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                iReturn = theClass.GetFrameCount();
            }

            return iReturn;
        }
    }

    public static float _GetVideoDisplayRate(int playerIndex) {
        float fReturn = 0.0F;
        if (s_Interface == null) {
            return 0.0F;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                fReturn = theClass.GetDisplayRate();
            }

            return fReturn;
        }
    }

    public static boolean _CanPlay(int playerIndex) {
        boolean bReturn = false;
        if (s_Interface == null) {
            return false;
        } else {
            AVProVideoPlayer theClass;
            if ((theClass = s_Interface.GetAVProClassForPlayerIndex(playerIndex)) != null) {
                bReturn = theClass.CanPlay();
            }

            return bReturn;
        }
    }

    public static void RendererReset(int deviceIndex) {
        if (s_Interface != null) {
            Iterator var1 = s_Interface.m_Players.entrySet().iterator();

            while(var1.hasNext()) {
                RendererSetupPlayer((Integer)((Entry)var1.next()).getKey(), deviceIndex);
            }

        }
    }

    private static int getGlVersionFromDeviceConfig(Context context) {
        int iReturn = 1;
        ActivityManager activityManager;
        ConfigurationInfo configInfo;
        if (context != null && (activityManager = (ActivityManager)((ActivityManager)context.getSystemService("activity"))) != null && (configInfo = activityManager.getDeviceConfigurationInfo()) != null) {
            if (configInfo.reqGlEsVersion >= 196608) {
                iReturn = 3;
            } else if (configInfo.reqGlEsVersion >= 131072) {
                iReturn = 2;
            }
        }

        return iReturn;
    }

    private static int getVersionFromPackageManager(Context context) {
        FeatureInfo[] featureInfos;
        if (context != null && (featureInfos = context.getPackageManager().getSystemAvailableFeatures()) != null && featureInfos.length > 0) {
            FeatureInfo[] var2 = featureInfos;
            int var3 = featureInfos.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                FeatureInfo featureInfo;
                if ((featureInfo = var2[var4]).name == null) {
                    if (featureInfo.reqGlEsVersion != 0) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    }

                    return 1;
                }
            }
        }

        return 1;
    }

    private static int getMajorVersion(int glEsVersion) {
        return (glEsVersion & -65536) >> 16;
    }

    private static native void nativeInit();

    static {
        System.loadLibrary("AVProLocal");
        nativeInit();
    }
}
