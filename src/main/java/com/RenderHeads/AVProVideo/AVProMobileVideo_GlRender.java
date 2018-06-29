//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.RenderHeads.AVProVideo;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLES30;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class AVProMobileVideo_GlRender {
    private int m_FrameBufferHandle;
    private int m_FrameBufferTextureHandle;
    private int m_FramebufferWidth;
    private int m_FramebufferHeight;
    private int m_MatrixHandle;
    private int m_VertexShaderHandle;
    private int m_FragmentShaderHandle;
    private int m_ProgramHandle;
    private int m_VertexAttribHandle;
    private int m_uvAttribHandle;
    private int m_TextureHandle;
    private FloatBuffer m_QuadPositions;
    private FloatBuffer m_QuadUVs;
    private FloatBuffer m_MatrixFloatBuffer;
    private String m_VertexShaderSource;
    private String m_FragmentShaderSource;
    private int m_iVertexArrayObject;
    private int[] m_aiVertexBufferObjects = new int[2];
    private boolean m_UseFastOesPath;
    private boolean m_HasImageData;
    private ByteBuffer m_ImageData;
    private int m_Width;
    private int m_Height;
    private boolean m_bTextureFormat_EOS;
    private boolean m_bCanUseGLBindVertexArray;
    private boolean m_bBlendEnabled;
    private boolean m_bDepthTest;
    private boolean m_bCullFace;
    private boolean m_bStencilEnabled;
    private int m_iCurrentProgram;
    private int m_iFrameBufferBinding;
    private int m_iRenderBufferBinding;

    public AVProMobileVideo_GlRender() {
    }

    public void Setup(int width, int height, byte[] data, boolean bTextureFormat_EOS, boolean bCanUseGLBindVertexArray, boolean useFastOesPath) {
        this.m_Width = width;
        this.m_Height = height;
        this.m_bTextureFormat_EOS = bTextureFormat_EOS;
        this.m_bCanUseGLBindVertexArray = bCanUseGLBindVertexArray;
        this.m_UseFastOesPath = useFastOesPath;
        if (data != null) {
            this.m_ImageData = ByteBuffer.allocate(this.m_Width * this.m_Height * 4);
            this.m_ImageData.put(data);
            (new StringBuilder("CreateGlTexture image size: ")).append(this.m_ImageData.position());
            this.m_ImageData.position(0);
            this.m_HasImageData = true;
        }

        if (!useFastOesPath) {
            if (this.m_bTextureFormat_EOS) {
                this.LoadGlShaders_TextureOES();
            } else {
                this.LoadGlShaders_Texture2D();
            }
        }

        this.CreateGlTexture();
        if (!useFastOesPath) {
            this.CreateGlShaderProgram();
            this.SetupGlShaderProgram();
            this.CreateGlQuadGeometry();
        }

    }

    public void Destroy() {
        this.DestroyGlTexture();
        this.DestroyRenderTarget();
        if (this.m_ProgramHandle > 0) {
            GLES20.glDeleteProgram(this.m_ProgramHandle);
            this.m_ProgramHandle = 0;
        }

        if (this.m_bCanUseGLBindVertexArray) {
            int[] handles;
            if (this.m_iVertexArrayObject > 0) {
                handles = new int[]{this.m_iVertexArrayObject};
                GLES30.glDeleteVertexArrays(1, handles, 0);
                GLES30.glGetError();
                this.m_iVertexArrayObject = 0;
            }

            if (this.m_aiVertexBufferObjects[0] > 0) {
                handles = new int[]{this.m_aiVertexBufferObjects[0], this.m_aiVertexBufferObjects[1]};
                GLES30.glDeleteBuffers(2, handles, 0);
                GLES30.glGetError();
                this.m_aiVertexBufferObjects[0] = 0;
                this.m_aiVertexBufferObjects[1] = 0;
            }
        }

        this.m_QuadPositions = null;
        this.m_QuadUVs = null;
        this.m_aiVertexBufferObjects = null;
        this.m_MatrixFloatBuffer = null;
    }

    public int GetGlTextureHandle(boolean actualTexture) {
        return !this.m_UseFastOesPath && !actualTexture ? this.m_FrameBufferTextureHandle : this.m_TextureHandle;
    }

    public void CreateRenderTarget(int width, int height) {
        (new StringBuilder("CreateRenderTarget() called (")).append(width).append(" x ").append(height).append(")");
        GLES20.glGetError();
        this.m_FramebufferWidth = width;
        this.m_FramebufferHeight = height;
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GLES20.glGenFramebuffers(1, intBuffer);
        this.m_FrameBufferHandle = intBuffer.get(0);
        (new StringBuilder("CreateRenderTarget m_FrameBufferHandle: ")).append(this.m_FrameBufferHandle);
        GLES20.glGenTextures(1, intBuffer);
        this.m_FrameBufferTextureHandle = intBuffer.get(0);
        (new StringBuilder("CreateRenderTarget m_FrameBufferTextureHandle: ")).append(this.m_FrameBufferTextureHandle);
        GLES20.glBindFramebuffer(36160, this.m_FrameBufferHandle);
        GLES20.glBindTexture(3553, this.m_FrameBufferTextureHandle);
        GLES20.glTexImage2D(3553, 0, 6408, this.m_FramebufferWidth, this.m_FramebufferHeight, 0, 6408, 5121, (Buffer)null);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.m_FrameBufferTextureHandle, 0);
        GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        GLES20.glClear(17664);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glGetError();
    }

    public void DestroyRenderTarget() {
        (new StringBuilder("DestroyRenderTarget m_FrameBufferHandle: ")).append(this.m_FrameBufferHandle);
        int[] handle;
        if (this.m_FrameBufferHandle != 0) {
            GLES20.glBindFramebuffer(36160, this.m_FrameBufferHandle);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, 0, 0);
            handle = new int[]{this.m_FrameBufferHandle};
            GLES20.glDeleteFramebuffers(1, handle, 0);
            GLES20.glGetError();
            this.m_FrameBufferHandle = 0;
            GLES20.glBindFramebuffer(36160, 0);
            GLES20.glGetError();
        }

        (new StringBuilder("DestroyRenderTarget m_FrameBufferTextureHandle: ")).append(this.m_FrameBufferTextureHandle);
        if (this.m_FrameBufferTextureHandle != 0) {
            handle = new int[]{this.m_FrameBufferTextureHandle};
            GLES20.glDeleteTextures(1, handle, 0);
            GLES20.glGetError();
            this.m_FrameBufferTextureHandle = 0;
        }

    }

    public void StartRender() {
        if (this.m_FrameBufferHandle != 0) {
            IntBuffer resultBuffer = IntBuffer.allocate(8);
            GLES20.glGetBooleanv(3042, resultBuffer);
            this.m_bBlendEnabled = resultBuffer.get(0) == 1;
            GLES20.glGetBooleanv(2929, resultBuffer);
            this.m_bDepthTest = resultBuffer.get(0) == 1;
            GLES20.glGetBooleanv(2884, resultBuffer);
            this.m_bCullFace = resultBuffer.get(0) == 1;
            GLES20.glGetIntegerv(35725, resultBuffer);
            this.m_iCurrentProgram = resultBuffer.get(0);
            GLES20.glGetIntegerv(36006, resultBuffer);
            this.m_iFrameBufferBinding = resultBuffer.get(0);
            GLES20.glGetIntegerv(36007, resultBuffer);
            this.m_iRenderBufferBinding = resultBuffer.get(0);
            GLES20.glGetBooleanv(2960, resultBuffer);
            this.m_bStencilEnabled = resultBuffer.get(0) == 1;
            GLES20.glBindFramebuffer(36160, this.m_FrameBufferHandle);
            GLES20.glBindRenderbuffer(36161, 0);
            GLES20.glDisable(3089);
            GLES20.glViewport(0, 0, this.m_FramebufferWidth, this.m_FramebufferHeight);
            GLES20.glClear(16384);
        }

    }

    public void EndRender() {
        if (this.m_FrameBufferHandle != 0) {
            if (this.m_bBlendEnabled) {
                GLES20.glEnable(3042);
            } else {
                GLES20.glDisable(3042);
            }

            if (this.m_bDepthTest) {
                GLES20.glEnable(2929);
            } else {
                GLES20.glDisable(2929);
            }

            if (this.m_bCullFace) {
                GLES20.glEnable(2884);
            } else {
                GLES20.glDisable(2884);
            }

            if (this.m_bStencilEnabled) {
                GLES20.glEnable(2960);
            } else {
                GLES20.glDisable(2960);
            }

            GLES20.glBindFramebuffer(36160, this.m_iFrameBufferBinding);
            GLES20.glBindRenderbuffer(36161, this.m_iRenderBufferBinding);
            GLES20.glUseProgram(this.m_iCurrentProgram);
        }

    }

    public long Blit(SurfaceTexture surfaceTexture, float[] matrix) {
        long result = 0L;
        GLES20.glBindBuffer(34963, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glDisable(2960);
        GLES20.glDisable(2929);
        GLES20.glDisable(2884);
        GLES20.glDisable(3042);
        GLES20.glColorMask(true, true, true, true);
        GLES20.glDepthMask(false);
        if (!this.m_bTextureFormat_EOS && surfaceTexture == null) {
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(770, 771);
        }

        GLES20.glUseProgram(this.m_ProgramHandle);
        if (!this.m_bCanUseGLBindVertexArray) {
            GLES20.glVertexAttribPointer(this.m_VertexAttribHandle, 3, 5126, false, 12, this.m_QuadPositions);
            GLES20.glEnableVertexAttribArray(this.m_VertexAttribHandle);
            GLES20.glVertexAttribPointer(this.m_uvAttribHandle, 2, 5126, false, 8, this.m_QuadUVs);
            GLES20.glEnableVertexAttribArray(this.m_uvAttribHandle);
        }

        int textureFormat = this.m_bTextureFormat_EOS ? '赥' : 3553;
        if (this.m_bTextureFormat_EOS) {
            if (surfaceTexture != null) {
                IntBuffer requiredTextureUnitsResultBuffer = IntBuffer.allocate(1);
                GLES20.glGetTexParameteriv(36197, 36200, requiredTextureUnitsResultBuffer);
                switch(requiredTextureUnitsResultBuffer.get(0)) {
                    case 3:
                        GLES20.glActiveTexture(33986);
                    case 2:
                        GLES20.glActiveTexture(33985);
                    default:
                        GLES20.glActiveTexture(33984);

                        try {
                            surfaceTexture.updateTexImage();
                            result = surfaceTexture.getTimestamp();
                        } catch (IllegalStateException var8) {
                            (new StringBuilder("Failed to updateTexImage in Blit: ")).append(var8);
                        }

                        if (matrix == null) {
                            matrix = new float[16];
                            surfaceTexture.getTransformMatrix(matrix);
                        }
                }
            }
        } else {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(textureFormat, this.m_TextureHandle);
        }

        if (matrix != null) {
            this.m_MatrixFloatBuffer = CreateFloatBuffer(matrix);
            GLES20.glUniformMatrix4fv(this.m_MatrixHandle, 1, false, this.m_MatrixFloatBuffer);
        }

        if (this.m_bCanUseGLBindVertexArray) {
            GLES30.glBindVertexArray(this.m_iVertexArrayObject);
            GLES20.glDrawArrays(5, 0, 4);
            GLES30.glBindVertexArray(0);
        } else {
            GLES20.glDrawArrays(5, 0, 4);
        }

        GLES20.glBindTexture(textureFormat, 0);
        GLES20.glDisableVertexAttribArray(this.m_uvAttribHandle);
        GLES20.glDisableVertexAttribArray(this.m_VertexAttribHandle);
        GLES20.glUseProgram(0);
        return result;
    }

    private void LoadGlShaders_TextureOES() {
        this.m_VertexShaderSource = "#version 100\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "precision mediump float;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "attribute vec4 vertexPosition;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "attribute vec4 vertexUV;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "uniform mat4 textureMat;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "varying highp vec2 out_uv;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "void main()\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "{\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "\tgl_Position = vec4(vertexPosition.xy, 0.0, 1.0);\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "\tout_uv = (textureMat * vertexUV).xy;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "}\n";
        this.m_FragmentShaderSource = "#version 100\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "#extension GL_OES_EGL_image_external : require\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "uniform samplerExternalOES texture;\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "varying highp vec2 out_uv;\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "void main()\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "{\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "\tgl_FragColor = texture2D(texture, out_uv);\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "}\n";
    }

    private void LoadGlShaders_Texture2D() {
        this.m_VertexShaderSource = "#version 100\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "precision mediump float;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "attribute vec4 vertexPosition;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "attribute vec4 vertexUV;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "uniform mat4 textureMat;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "varying highp vec2 out_uv;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "void main()\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "{\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "\tgl_Position = vec4(vertexPosition.xy, 0.0, 1.0);\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "\tout_uv = (textureMat * vertexUV).xy;\n";
        this.m_VertexShaderSource = this.m_VertexShaderSource + "}\n";
        this.m_FragmentShaderSource = "#version 100\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "uniform sampler2D texture;\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "varying highp vec2 out_uv;\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "void main()\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "{\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "\tgl_FragColor = texture2D(texture, out_uv);\n";
        this.m_FragmentShaderSource = this.m_FragmentShaderSource + "}\n";
    }

    private void CreateGlTexture() {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GLES20.glGenTextures(1, intBuffer);
        this.m_TextureHandle = intBuffer.get(0);
        if (this.m_TextureHandle > 0) {
            (new StringBuilder("CreateGlTexture m_TextureHandle: ")).append(this.m_TextureHandle);
            int textureFormat;
            GLES20.glBindTexture(textureFormat = this.m_bTextureFormat_EOS ? '赥' : 3553, this.m_TextureHandle);
            if (this.m_HasImageData) {
                GLES20.glTexImage2D(textureFormat, 0, 6408, this.m_Width, this.m_Height, 0, 6408, 5121, this.m_ImageData);
            }

            GLES20.glTexParameteri(textureFormat, 10241, 9729);
            GLES20.glTexParameteri(textureFormat, 10240, 9729);
            GLES20.glTexParameteri(textureFormat, 10242, 33071);
            GLES20.glTexParameteri(textureFormat, 10243, 33071);
            GLES20.glBindTexture(textureFormat, 0);
        } else {
            (new StringBuilder("Error allocating texture handle ")).append(this.m_TextureHandle);
        }
    }

    private void DestroyGlTexture() {
        if (this.m_TextureHandle > 0) {
            int[] handle = new int[]{this.m_TextureHandle};
            GLES20.glDeleteTextures(1, handle, 0);
            GLES20.glGetError();
            this.m_TextureHandle = 0;
        }

    }

    private void CreateGlShaderProgram() {
        this.m_VertexShaderHandle = this.LoadGlShader(35633, this.m_VertexShaderSource);
        this.m_FragmentShaderHandle = this.LoadGlShader(35632, this.m_FragmentShaderSource);
        int handle;
        if ((handle = GLES20.glCreateProgram()) > 0) {
            GLES20.glAttachShader(handle, this.m_VertexShaderHandle);
            GLES20.glAttachShader(handle, this.m_FragmentShaderHandle);
            GLES20.glLinkProgram(handle);
            this.m_ProgramHandle = handle;
            GLES20.glDetachShader(this.m_ProgramHandle, this.m_VertexShaderHandle);
            GLES20.glDetachShader(this.m_ProgramHandle, this.m_FragmentShaderHandle);
            GLES20.glDeleteShader(this.m_VertexShaderHandle);
            GLES20.glDeleteShader(this.m_FragmentShaderHandle);
        }

    }

    private void SetupGlShaderProgram() {
        this.m_VertexAttribHandle = GLES20.glGetAttribLocation(this.m_ProgramHandle, "vertexPosition");
        this.m_uvAttribHandle = GLES20.glGetAttribLocation(this.m_ProgramHandle, "vertexUV");
        this.m_MatrixHandle = GLES20.glGetUniformLocation(this.m_ProgramHandle, "textureMat");
    }

    private void CreateGlQuadGeometry() {
        float[] TriangleVerticesData = new float[]{-1.0F, -1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F};
        float[] UVData = new float[]{0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F};
        this.m_QuadPositions = CreateFloatBuffer(TriangleVerticesData);
        this.m_QuadUVs = CreateFloatBuffer(UVData);
        if (this.m_bCanUseGLBindVertexArray) {
            IntBuffer intVertexArrayBuffer = IntBuffer.allocate(1);
            GLES30.glGenVertexArrays(1, intVertexArrayBuffer);
            this.m_iVertexArrayObject = intVertexArrayBuffer.get(0);
            GLES30.glBindVertexArray(this.m_iVertexArrayObject);
            IntBuffer intVertexBuffersBuffer = IntBuffer.allocate(2);
            GLES30.glGenBuffers(2, intVertexBuffersBuffer);
            this.m_aiVertexBufferObjects[0] = intVertexBuffersBuffer.get(0);
            this.m_aiVertexBufferObjects[1] = intVertexBuffersBuffer.get(1);
            GLES30.glBindBuffer(34962, this.m_aiVertexBufferObjects[0]);
            GLES30.glBufferData(34962, 48, this.m_QuadPositions, 35044);
            GLES30.glVertexAttribPointer(this.m_VertexAttribHandle, 3, 5126, false, 0, 0);
            GLES30.glEnableVertexAttribArray(this.m_VertexAttribHandle);
            GLES30.glBindBuffer(34962, this.m_aiVertexBufferObjects[1]);
            GLES30.glBufferData(34962, 32, this.m_QuadUVs, 35044);
            GLES30.glVertexAttribPointer(this.m_uvAttribHandle, 2, 5126, false, 0, 0);
            GLES30.glEnableVertexAttribArray(this.m_uvAttribHandle);
            GLES30.glBindVertexArray(0);
        }

    }

    private int LoadGlShader(int type, String source) {
        int handle;
        if ((handle = GLES20.glCreateShader(type)) > 0) {
            GLES20.glShaderSource(handle, source);
            GLES20.glCompileShader(handle);
        }

        return handle;
    }

    private static FloatBuffer CreateFloatBuffer(float[] values) {
        ByteBuffer bytes;
        (bytes = ByteBuffer.allocateDirect(values.length * 4)).order(ByteOrder.nativeOrder());
        FloatBuffer result;
        (result = bytes.asFloatBuffer()).put(values);
        result.position(0);
        return result;
    }
}
