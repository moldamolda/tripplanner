package dat.daos;

import dat.enums.Speciality;
import dat.exceptions.ApiException;

import java.time.LocalDate;
import java.util.List;

public interface IDAO<T, I> {
    T read(I id) throws ApiException;
    List<T> readAll() throws ApiException;

    T create(T entity) throws ApiException;
    T update(I id, T entity) throws ApiException;
    void delete(I id) throws ApiException;
}
