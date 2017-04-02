
package com.android.showmeeapp.model.UserDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class When {

    @SerializedName("$date")
    @Expose
    private Integer $date;

    /**
     * No args constructor for use in serialization
     *
     */
    public When() {
    }

    /**
     *
     * @param $date
     */
    public When(Integer $date) {
        this.$date = $date;
    }

    /**
     *
     * @return
     *     The $date
     */
    public Integer get$date() {
        return $date;
    }

    /**
     *
     * @param $date
     *     The $date
     */
    public void set$date(Integer $date) {
        this.$date = $date;
    }

}
