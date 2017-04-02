
package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedAt {

    @SerializedName("$date")
    @Expose
    private Long $date;

    /**
     * No args constructor for use in serialization
     *
     */
    public CreatedAt() {
    }

    /**
     *
     * @param $date
     */
    public CreatedAt(Long $date) {
        this.$date = $date;
    }

    /**
     *
     * @return
     *     The $date
     */
    public Long get$date() {
        return $date;
    }

    /**
     *
     * @param $date
     *     The $date
     */
    public void set$date(Long $date) {
        this.$date = $date;
    }

}
