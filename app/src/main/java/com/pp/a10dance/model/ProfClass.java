package com.pp.a10dance.model;

/**
 * Created by saketagarwal on 12/12/15.
 */
public class ProfClass {

    private String _id;
    private String _rev;
    private String createdAt;
    private String name;
    private String description;

    public ProfClass() {
    }

    public ProfClass(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String get_id() {

        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "ProfClass{" + "_id='" + _id + '\'' + ", _rev='" + _rev + '\''
                + ", createdAt='" + createdAt + '\'' + ", name='" + name + '\''
                + ", description='" + description + '\'' + '}';
    }
}
