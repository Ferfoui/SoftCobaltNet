package fr.ferfoui.softcobalt.api.requestformat.datasending;

import fr.ferfoui.softcobalt.api.requestformat.request.Request;

import java.util.List;

public interface RequestReader {

    /**
     * Return the requests contained in the data.
     *
     * @return The list of requests.
     */
    List<? extends Request> getRequests();

    /**
     * Return the number of requests contained in the data.
     *
     * @return The number of requests.
     */
    int getRequestsCount();

}
