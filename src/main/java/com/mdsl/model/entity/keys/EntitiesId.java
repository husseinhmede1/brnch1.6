package com.mdsl.model.entity.keys;

import java.io.Serializable;
import java.util.Objects;

public class EntitiesId implements Serializable {

    private String entityId;
    private String institution;

    public EntitiesId() {}

    public EntitiesId(String entityId, String institution) {
        this.entityId = entityId;
        this.institution = institution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntitiesId)) return false;
        EntitiesId that = (EntitiesId) o;
        return Objects.equals(entityId, that.entityId) &&
               Objects.equals(institution, that.institution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, institution);
    }
}
