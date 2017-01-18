package org.profit.util;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class ListUtils {

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
