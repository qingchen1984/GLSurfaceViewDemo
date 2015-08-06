uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
attribute vec2 aTexCoor;
varying vec2 vTextureCoord;
void main() 
{
  gl_Position = uMVPMatrix * aPosition;
   vTextureCoord =  aTexCoor;
 }