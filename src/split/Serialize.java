package split;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class Serialize {
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
