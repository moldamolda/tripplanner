package dat.daos;

import dat.dtos.TripDTO;
import dat.exceptions.ApiException;

import java.util.Set;

public interface ITripGuideDAO {
    void addGuideToTrip(int tripId, int guideId) throws ApiException;
    Set<TripDTO> getTripsByGuide(int guideId) throws ApiException;
}
