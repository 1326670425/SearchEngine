package split;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
/**
 * 
 * @ClassName Serialize
 * @Description 对象的序列化和反序列化
 * @author 吴扬颉
 * @date 2018年6月4日
 *
 */

public class Serialize {
	/**
	 * 
	 * @Title write
	 * @Description 序列化List对象
	 * @param List<T> list 待序列化的列表
	 * @return String 返回序列化后的字符串
	 */
	public static <T> String write(List<T> list){
		String str="";
		@SuppressWarnings("unchecked")
		T[] array = (T[])list.toArray();
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(array);
			oos.flush();
			str = Base64.encode(bos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 
	 * @Title read
	 * @Description TODO
	 * @param str 待反序列化的字符串
	 * @return List<E> 序列化后的列表
	 */
	@SuppressWarnings("unchecked")
	public static <E> List<E> read(String str){
		E[] object;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(str));
			ObjectInputStream ois = new ObjectInputStream(bis);
			object = (E[]) ois.readObject();
			return Arrays.asList(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}

}
