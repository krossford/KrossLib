# KrossAndroidLib
编码: UTF-8


* [app](https://github.com/krossford/KrossLib/tree/master/android-project#app)
    * [graphics](https://github.com/krossford/KrossLib/tree/master/android-project#graphics) 各种图形相关
    * [codec](https://github.com/krossford/KrossLib/tree/master/android-project#codec) 编解码相关
    * [math](https://github.com/krossford/KrossLib/tree/master/android-project#math) 数学相关
    * [util](https://github.com/krossford/KrossLib/tree/master/android-project#util) 各种常用工具类
    * [views](https://github.com/krossford/KrossLib/tree/master/android-project#views) 各种好用的View，妥妥的
* [myphotoshop](https://github.com/krossford/KrossLib/tree/master/android-project#myphotoshop)

## app

这个Module里面有各种积累的代码，包括一些算法的实现，一些绘图的实现，一些花心思写的工具函数。

### graphics

* **BezierCurve** 贝塞尔曲线
    * build
    * progressWith
    * setPointSequence
* **Primitive2D** 二维图元
    * circle
    * line
* **ImageProcess** 图像处理
    * fastMosaic 马赛克算法

### codec

* **LZW** LZW编解码的实现
* **RunLength** 游程编解码的实现

### math

* **PolarPointF** 极坐标系中的点
    * parseFromCartesianCoordinatePoint
    * toCartesianCoordinatePoint
* **Util** 处理一些常规计算
    * getCrossPoint

### util

* **FileUtil** 一些和文件相关的操作
    * fourBytesToInt
    * getBytesFromFile
    * getSuffix
    * isBMP
    * isGIF
    * isJPG
    * isPNG
    * twoBytesToInt

### views
* **BottomLoadingListView** 滑动到底部触发能触发回调的ListView，支持自定义 loading view
    * setTriggerMode 设置触发模式
    * setBottomLoadingView 设置底部加载view
    * hideBottomLoadingView 隐藏底部加载view
    * showBottomLoadingView 显示底部加载view
    * setListener 设置监听器

## myphotoshop
这个目前来说是个坑
