package org.profit.util;

public final class Pair<FIRST, SECOND> {

	public final FIRST first;
	public final SECOND second;

	public Pair(FIRST first, SECOND second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode() {
		return 17 * ((first != null) ? first.hashCode() : 0) + 17
				* ((second != null) ? second.hashCode() : 0);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair<?, ?>)) {
			return false;
		}

		Pair<?, ?> that = (Pair<?, ?>) o;
		return equal(this.first, that.first) && equal(this.second, that.second);
	}

	private static boolean equal(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	@Override
	public String toString() {
		return String.format("{%s,%s}", first, second);
	}
}
