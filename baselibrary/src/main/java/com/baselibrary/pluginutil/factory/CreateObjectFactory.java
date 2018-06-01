package com.baselibrary.pluginutil.factory;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <pre>
 * File Name: COFactory.java
 * Author lcl
 * Date: 2015-7-20 下午2:28:47
 * Description
 * 	创建新的对象的工厂类，可以通过这个类实例化任何类对象(CO=CreateObject)
 * 	用法：
 * 	UserInfo u=COFactory.getObject(
 * 		UserInfo.class,//需要创建的类的class文件
 * 		"sssss",//构造的参数列表1
 * 		11,//构造的参数列表2
 * 		"ssss");
 * Version: v0.0.0.1 //版本信息
 * Function List: // 主要函数及其功能
 * Others: // 其它内容的说明
 * History: // 历史修改记录
 * </pre>
 */
public class CreateObjectFactory {
	/**
	 * 通过反色机制实例化类的方法。可以实例化任何类
	 *
	 * @param t
	 *            返回的类型。根据泛型返回
	 * @param isAccess
	 *            是否运行执行私有构造方法(T=允许，F=不允许),允许可能造成意想不到的错误
	 * @param params
	 *            参数列表。与构造函数相同的参数列表
	 * @return Null：没有找到相关的构造方法或者没有找到构造成功 T ：成功构造了指定对象。返回与T相同的对象
	 */
	public static <T> T getObject(
			Class<T> t, boolean isAccess , Object[] params) throws Exception{
		Class<?> classType = t;
		Constructor<?>[] construct = null;
		try {
			construct = classType.getDeclaredConstructors();
		}catch (Error e){
			e.printStackTrace();
			Log.e("this",e.toString());
		}
		T resul = null;
		if(params == null){
			params = new Object[0];
		}
		for (Constructor<?> c : construct) {
			Class<?>[] construParams = c.getParameterTypes();
			if (construParams.length == params.length) {
				boolean flg = true;
				// 循环当前参数列表
				for (int i = 0;i < construParams.length; i++) {
					// 获取构造方法的参数列表的指定位置的指定class类型，有可能出现基础类型
					Class<?> param = construParams[i];
					// 获取传入的参数列表对应位置的class类型。这个一定是引用的类型
					Class<?> temParam = params[i].getClass();
					if (param.isPrimitive()) {
						// 如果是基础类型。那么就拿到基础类型的引用类型
						param = CreateObjectFactory.getTypeClass(param);
						if (param == null)
							break;
					}
					if(!param.isAssignableFrom(temParam)){
						flg = false;
						break;
					}
				}
				// 这个构造方法参数列表完全匹配
				if (flg) {
					try {
						if (isAccess){
							c.setAccessible(true);
						}
						resul = (T) c.newInstance(params);
					} catch (Exception e) {
						throw new ClassCastException("对象创建错误："+e.getMessage());
					}
					break;
				}
			}
			continue;
		}
		return resul;
	}

	/**
	 * 通过反色机制实例化类的方法。可以实例化任何类
	 *
	 * @param t
	 *            返回的类型。根据泛型返回
	 * @param params
	 *            参数列表。与构造函数相同的参数列表
	 * @return Null：没有找到相关的构造方法或者没有找到构造成功 T ：成功构造了指定对象。返回与T相同的对象
	 */
	public static <T> T getObject(Class<T> t,Object[] params) throws Exception{
		T result = null;
		try {
			result = getObject(t,false,params);
		}catch (Exception e){
			throw e;
		}
		return result;
	}

	/**
	 * 通过包的路径来创建对象
	 * @param packPathName
	 *            类的完整路径-包路径.类名(点分隔)
	 * @param isAccess
	 *            是否运行执行私有构造方法(T=运行，F=不允许)
	 * @param params
	 *            构造的方法的参数列表
	 * @return 具体的对象
	 */
	public static <T> T getObject(String packPathName, boolean isAccess , Object[] params) throws Exception{
		Class<?> classType = null;
		try {
			classType = Class.forName(packPathName);
		} catch (ClassNotFoundException e1) {
			throw new ClassCastException("class文件未找到"+e1.getMessage());
		}
		if (classType == null)
			return null;
		T resul = null;
		try {
			resul = (T) getObject(classType,isAccess,params);
		}catch (Exception e){
			throw e;
		}
		return resul;
	}

	/**
	 * 通过包的路径来创建对象
	 * @param packPathName
	 *            类的完整路径-包路径.类名(点分隔)
	 * @param params
	 *            构造的方法的参数列表
	 * @return 具体的对象
	 */
	public static <T> T getObject(String packPathName, Object[] params) throws Exception{
		T result = null;
		try {
			result = getObject(packPathName,false,params);
		} catch (Exception e1) {
			throw e1;
		}
		return result;
	}

	/**
	 * <pre>
	 * 泛型的类型操作,例如：<br>
	 *     class Obj&lt;T,E&gt;{
	 *         public void test(){
	 *         	//这里表示的是：获取当前对象的第1个泛型(T)对应类型所创建的对象
	 *             T t = COFactory.getT(this,0);
	 *         	//这里表示的是：获取当前对象的第2个泛型(E)对应类型所创建的对象
	 *             E e = COFactory.getT(this,1);
	 *         }
	 *
	 *         具体示例：
	 *         public static void getTTest(){
	 *             Obj&lt;Integer,Object&gt o = new Obj();
	 *             COFactory.getT(o,0);//返回的则是第1个泛型位置对应的Integer类型创建的对象
	 *             COFactory.getT(o,2);//返回的则是第2个泛型位置对应的Object类型创建的对象
	 *         }
	 *     }
	 * </pre>
	 * @param obj 当前这个实体对象
	 * @param i 获取泛型参数的下标
	 * @param newParams 构造参数(可以省略，省略调用默认的构造方法)
	 * @param <T> i位置上对应的泛型所创建的对象
	 * @return i位置上泛型对应的类型创建的对象
	 */
	public static <T> T getT(Object obj, int i,Object... newParams) {
		try {
			Class<T> czz = getTClass(obj,i);
			return getObject(czz,newParams);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取对象所带所有泛型类型对应的Class集合,顺序和泛型顺序一致
	 * @param obj 带有泛型的对象
	 * @param <T> 泛型
     * @return
     */
	public static <T> Class<T>[] getTClass(Object obj){
		try {
			Type[] ts = ((ParameterizedType)(obj.getClass()
					.getGenericSuperclass())).getActualTypeArguments();
			Class<T>[] ct = new Class[ts.length];
			for (int i = 0; i < ts.length; i++) {
				ct[i] = (Class<T>) ts[i];
			}
			return ct;
		}catch (Exception e){
			return null;
		}
	}

	/**
	 * 获取对象的指定下标的泛型
	 * @param obj 带有泛型的对象
	 * @param posT 第几个泛型对应的Class类型
	 * @param <T> 泛型
     * @return
     */
	public static <T> Class<T> getTClass(Object obj,int posT){
		try {
			Class<T>[] cs = getTClass(obj);
			return cs[posT];
		}catch (Exception e){
			return null;
		}
	}


	/**
	 * 反射执行指定对象的方法
	 *
	 * @param obj
	 *            该方法所在的对象
	 * @param methodName
	 *            方法名称
	 * @param params
	 *            参数(可选)
	 * @return 返回值
	 */
	public static Object runMenthod(
			Object obj, String methodName,Object[] params) throws Exception{
		return runMenthod(obj, methodName, false, params);
	}

	/**
	 * 反射执行指定对象的方法(该类必须有默无参认构造方法，公有私有无限制)
	 * @param objClass 在该class中执行方法(方法所在的class)
	 * @param methodName 方法名称
	 * @param params 方法参数列表
	 * @return
	 */
	public static Object runMenthod(
			Class<?> objClass, String methodName,Object[] params) throws Exception{
		Object result = null;
		try {
			result =  runMenthod(objClass, methodName, false, params);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 反射执行指定对象的方法(该类必须有默无参认构造方法，公有私有无限制)
	 * @param objClass 在该class中执行方法(方法所在的class)
	 * @param methodName 方法名称
	 * @param isAccess 是否允许调用私有的方法
	 * @param params 方法参数列表
	 * @return
	 */
	public static Object runMenthod(
			Class<?> objClass, String methodName,boolean isAccess,Object[] params) throws Exception{
		Object result = null;
		try {
			Object o = getObject(objClass,true,null);
			result =  runMenthod(o, methodName, isAccess, params);
			o = null;
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 反射执行指定对象的方法
	 *
	 * @param obj
	 *            该方法所在的对象
	 * @param methodName
	 *            方法名称
	 * @param isAccess
	 *            是否运行执行私有方法(T=允许,F=不允许)
	 * @param params
	 *            参数(可选)
	 * @return 返回值
	 */
	public static Object runMenthod(Object obj, String methodName,
									boolean isAccess, Object[] params) {
		if (obj == null || methodName == null || "".equals(methodName)) {
			return null;
		}
		List<Method> mts = new ArrayList<>();
		addArray2List(mts,obj.getClass().getMethods());//先将公共的方法加入到集合中以及继承的方法
		addArray2List(mts,obj.getClass().getDeclaredMethods());//然后加入私有的方法
//		boolean flg = false;//是否运行成功，展示不关心
		Object result = null;//返回结果
		for (Method m : mts) {
			if (m.getName().equals(methodName)) {
				try {
					if(isAccess){
						m.setAccessible(true);
					}
					result = m.invoke(obj, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 将数组中的数据加入到List集合中
	 * @param list
	 * @param t
	 * @param <T>
     */
	public static final <T> void addArray2List(List<T> list,T[] t){
		for (int i = 0; i < t.length; i++) {
			if(!list.contains(t[i])){
				list.add(t[i]);
			}
		}
	}

	/**
	 * 获取指定元素的指定类型的注解，允许的对象为：Field、Method、Class(默认是Class类注解)
	 *
	 * @param obj
	 * @param AnnotatClass
	 * @return 存在指定注解。返回注解.否则返回null
	 */
	public static final <A extends Annotation, T> A getAnnota(T obj, Class<A> AnnotatClass) {
		try {
			if (obj instanceof Field) {
				return ((Field) obj).getAnnotation(AnnotatClass);
			} else if (obj instanceof Class) {
				return (A) ((Class) obj).getAnnotation(AnnotatClass);
			} else if (obj instanceof Method) {
				return ((Method) obj).getAnnotation(AnnotatClass);
			}
			return obj.getClass().getAnnotation(AnnotatClass);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 如果是基本类型。那么就调用这个方法判断。返回这个基本类型的对应的引用类型的class类型
	 *
	 * @param classes
	 *            基本类型的class类型
	 * @return 这个基本类型对应的引用类型的Class类型
	 */
	public static Class<?> getTypeClass(Class<?> classes) {
		// 保存基础类型对应的class类型
		List<Class<?>> listType = new ArrayList<Class<?>>();
		// 保存引用类型的class类型
		List<Class<?>> listClass = new ArrayList<Class<?>>();
		// 加入基础类型的class类型和对引用类型的class类型
		listClass.add(Integer.class);
		listType.add(Integer.TYPE);
		listClass.add(Boolean.class);
		listType.add(Boolean.TYPE);
		listClass.add(Character.class);
		listType.add(Character.TYPE);
		listClass.add(Byte.class);
		listType.add(Byte.TYPE);
		listClass.add(Short.class);
		listType.add(Short.TYPE);
		listClass.add(Long.class);
		listType.add(Long.TYPE);
		listClass.add(Float.class);
		listType.add(Float.TYPE);
		listClass.add(Double.class);
		listType.add(Double.TYPE);
		listClass.add(Void.class);
		listType.add(Void.TYPE);
		// 加入结束
		for (int i = 0; listClass.size() == listType.size()
				&& i < listClass.size(); i++) {
			if (listType.get(i).equals(classes)) {
				return listClass.get(i);
			}
		}
		return null;
	}
}
