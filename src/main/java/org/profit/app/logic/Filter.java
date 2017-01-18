package org.profit.app.logic;

import org.profit.persist.domain.stock.Daily;

/**
 * Created by Ying on 2014/5/5.
 */
public enum Filter {

    INSTANCE;

    public boolean filterVolume(Daily daily) {
        return daily.getVolume() != 0;
    }
}
