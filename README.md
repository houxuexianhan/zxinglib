# zxinglib
[原始项目源码zxing](https://github.com/yuzhiqiang1993/zxing) 
该项目实现了扫一扫的基本功能如：扫描二维码，扫描条码，闪光灯，解析二维码图片，解析条码图片，生成二维码，使用起来也非常方便。

![](https://github.com/yuzhiqiang1993/zxing/raw/master/img/scanTm.gif)![](https://github.com/yuzhiqiang1993/zxing/raw/master/img/scanEwm.gif)

上面的库我在使用过程中遇到了一些问题，因为库依赖于AppCompat主题，所以我的应用的主题Theme必须是AppComat的，这一点让我的应用无法使用该库（试了改主题，但界面乱了),所以下载了该源码，将其中对AppCompat依赖的东西全部去除了，然后我重新通过JitPack发布了一个版本供自己的项目使用。[修改后的Zxinglib源码库](https://github.com/houxuexianhan/zxinglib)
### 1）添加依赖
在根目录 build.gradle 中加入
```
allprojects {
    repositories {
        google()
        mavenCentral()  //如果其它仓库被墙了，使用这个
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
在app目录的 build.gradle 中加入

```
dependencies {
 ...   
   /*添加依赖*/
   implementation 'com.github.houxuexianhan:zxinglib:1.1.1'
}

```
### 2）添加权限依赖
demo使用的权限申请是严大的一个开源库，地址：https://github.com/yanzhenjie/AndPermission 感谢严大！
```
dependencies {
 ...   
   /*添加依赖*/
   implementation 'com.yanzhenjie:permission:2.0.0-rc4'
}
```
需要申请的权限
Manifest.permission.CAMERA
Manifest.permission.READ_EXTERNAL_STORAGE
### 3）跳转到扫一扫界面
使用默认配置项，两行代码即可

```
Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
startActivityForResult(intent, REQUEST_CODE_SCAN);
```
自定义配置项

```
Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
/*ZxingConfig是配置类
*可以设置是否显示底部布局，闪光灯，相册，
* 是否播放提示音  震动
* 设置扫描框颜色等
* 也可以不传这个参数
* */
ZxingConfig config = new ZxingConfig();
config.setPlayBeep(true);//是否播放扫描声音 默认为true
config.setShake(true);//是否震动  默认为true
config.setDecodeBarCode(false);//是否扫描条形码 默认为true
config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为淡蓝色
config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
startActivityForResult(intent, REQUEST_CODE_SCAN);
```
### 4）接收扫码结果
注意：Constant.CODED_CONTENT引的是这个com.yzq.zxinglibrary.common.Constant
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                result.setText("扫描结果为：" + content);
            }
        }
    }
```
### 5）生成二维码

```
String contentEtString = contentEt.getText().toString().trim();
                
      if (TextUtils.isEmpty(contentEtString)) {
            Toast.makeText(this, "contentEtString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = null;
        try {
            /*
            * contentEtString：字符串内容
            * w：图片的宽
            * h：图片的高
            * logo：不需要logo的话直接传null
            * */

            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);
        } catch (WriterException e) {
            e.printStackTrace();
        }
```
