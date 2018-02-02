package org.profit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class ListUtils {

	/**
	 * 将一个list均分成n个list,主要通过偏移量来实现的
	 *
	 * @param source
	 * @return
	 */
	public static <T> List<List<T>> averageAssign(List<T> source,int n){
		List<List<T>> result=new ArrayList<List<T>>();
		int remaider=source.size()%n;  //(先计算出余数)
		int number=source.size()/n;  //然后是商
		int offset=0;//偏移量
		for(int i=0;i<n;i++){
			List<T> value=null;
			if(remaider>0){
				value=source.subList(i*number+offset, (i+1)*number+offset+1);
				remaider--;
				offset++;
			}else{
				value=source.subList(i*number+offset, (i+1)*number+offset);
			}
			result.add(value);
		}
		return result;
	}

	/**
	 * Sub list
	 *
	 * @return
	 */
	public static <T> List<T> sublist(List<T> list, int start, int limit) {
		int fromIndex = Math.min(start, list.size());
		int toIndex = Math.min(fromIndex + limit, list.size());
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * 从队列里移除所有该单位
	 *
	 * @param queue
	 * @param unit
	 */
	public static <T> void removeAll(Queue<T> queue, T unit) {
		Iterator<T> it = queue.iterator();
		while (it.hasNext()) {
			if (it.next().equals(unit)) {
				it.remove();
			}
		}
	}
}
