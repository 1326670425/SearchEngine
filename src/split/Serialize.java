package split;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
/**
 * 
 * @ClassName Serialize
 * @Description ��������л��ͷ����л�
 * @author �����
 * @date 2018��6��4��
 *
 */

public class Serialize {
	/**
	 * 
	 * @Title write
	 * @Description ���л�List����
	 * @param List<T> list �����л����б�
	 * @return String �������л�����ַ���
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
	 * @param str �������л����ַ���
	 * @return List<E> ���л�����б�
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
