开源换肤代码Android-skin-support，后续根据我们当前情况修改的aar，以下是使用说明：
 	1.Application 必须实现方法

	SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())   // 基础控件换肤
                .setSkinStatusBarColorEnable(true)              // 关闭状态栏换肤
                .setSkinAllActivityEnable(true)                // true: 默认所有的Activity都换肤; false: 只有实现SkinCompatSupportable接口的Activity换肤
                .loadSkin();
        SkinCompatManager.getInstance().addDefaultTextTypefacePath("fonts/HelveticaLT33ThinExtended.ttf");//默认字体
        SkinCompatManager.getInstance().addTextTypefacePath(SkinCompatManager.SKIN_NAME_RS,"fonts/SketchRockwell-Bold.ttf");//其他主题自由体
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

	
	2. 需要换肤的 自定义View 不能  new 必须用LayoutInflater.from(getContext()).inflate(layoutResId, this);
	   因为换肤需要 AttributeSet attrs, int defStyleAttr  
		自定义方式
		2.1 实现接口SkinCompatSupportable
		   
		    换肤实现	SkinCompatBackgroundHelper mBackgroundHelper;
    		    换字体实现	SkinCompatTextHelper mTextHelper;
		2.2继承 已有的 SkinCompatView SkinCompatButton等系列
	
	3.在WindowManager 的View  必须用LayoutInflater.from(getApplicationContext()).inflate(layoutResId, this);

	4.制作皮肤是 必须保证 图片名字一致，DrawableSelector 、ColorSelector 名字一致，  XXX.skin 文件 就是一个apk的改名，只需要添加资源内容就可以


	AAR使用方法
	build.gradle
		android {
			...
			repositories {
			flatDir {
				dirs 'libs'
			}
			}
		}
		
		dependencies { 
			...
			implementation(name: 'skinlib-release', ext: 'aar') 
		}