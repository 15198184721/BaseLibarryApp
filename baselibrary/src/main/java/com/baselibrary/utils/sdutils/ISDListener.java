package com.baselibrary.utils.sdutils;
/**
 * <pre>
 * File Name: ISDListener.java
 * Author lcl 
 * Date: 2015-10-31
 * Description
 * 	这个是sd卡操作方法的回调接口
 * </pre>
 */
public interface ISDListener {
	/**
	 * 开始之前的准备工作。例如需要有什么初始化操作等
	 */
	public void init();
	
	/**
	 * sd卡保存文件的时候的操作,保存过程中持续的操作回调
	 *<pre>
	 * @param currLenght 当前的进度(当前已经保存的文件字节数)
	 *</pre>
	 */
	public void call(Object currLenght);
	
	/**
	 *<pre>
	 * 检查是否要求退出，(注意：并不能保证实时退出。因为采用的是轮询方式)
	 * 	方法返回的是false：那么操作将不继续执行直接退出
	 * 	方法返回的是true:将继续执复制等行操作
	 * @return
	 * 	True：继续当前耗时操作
	 * 	False：退出当前的耗时操作
	 *</pre>
	 */
	public boolean checkExit();
}
