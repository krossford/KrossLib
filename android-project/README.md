# KrossAndroidLib

* [app](https://github.com/krossford/KrossLib/tree/master/android-project#app)
* [views](https://github.com/krossford/KrossLib/tree/master/android-project#views)

## app

这个Module里面有各种积累的代码，包括一些算法的实现，一些绘图的实现，一些花心思写的工具函数。

### graphics

BezierCurve 贝塞尔曲线

Primitive2D 二维图元
```
//bresenham画线算法实现
line(Point, Point)

//画圆算法实现
circle(int, int, float)
```

ImageProcess 图像处理
```
//获取马赛克图片的方法
fastMosaic(Bitmap)
```
### codec

LZW LZW编解码的实现

RunLength 游程编解码的实现

### math

PolarPointF 极坐标系中的点
```
//将笛卡尔坐标系的点转换成极坐标系
parseFromCartesianCoordinatePoint(float, float)

//将极坐标系的点转换成笛卡尔坐标系的点
toCartesianCoordinatePoint()
```

Util 处理一些常规计算
```
//通过四个点定义的两条直线得到交点
getCrossPoint(float, float, float, float, float, float, float, float)
```

### util

FileUtil 一些和文件相关的操作
```
//获取文件的所有字节
getBytesFromFile(File)

//将两个字节组成整型
twoBytesToInt(byte[])

//将四个字节组成整型
fourBytesToInt(byte[])

//判断是否是png文件
isPNG(byte[])

//判断是否是jpg文件
isJPG(byte[])

//判断是否是bmp文件
isBMP(byte[])

//判断是否是gif文件
isGIF(byte[])

//获取文件名的后缀
getSuffix(String)
```

### views

**javaprogram**

**myphotoshop**

**testplugin**
