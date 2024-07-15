/**
 * DtoConvertor.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * DtoConvertor. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public abstract class DtoConverter<T extends Serializable, R extends Serializable> {

    public List<T> convertDtoFromEntities(List<R> entities, String fieldsString) {
        if (entities == null) {
            return new ArrayList<>();
        }

        List<T> result = new LinkedList<>();
        for (R entity : entities) {
            result.add(fetchEntity(entity, fieldsString));
        }

        return result;
    }

    /**
     * @param entity
     * @return
     */
    protected T fetchEntity(R entity, String fieldsString) {
        return null;
    }
}
