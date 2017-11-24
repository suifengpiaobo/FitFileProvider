# FitFileProvider
android 7.0之后使用Intent打开相机获取照片，下载Apk安装，微信，QQ分享本地图片等需要进行FileProvider适配，通过增加定义好的规则，一行代码适配FileProvider
##### 使用方法
+ 1.gradle添加：compile 'com.lx:fit7:2.0' 
+ 2.代码中使用：
```java
 File file = new File(path); //根据path创建File
 String contentURl = Fit7Utils.getUriForFile(context, file); //获取File的Content地址
```