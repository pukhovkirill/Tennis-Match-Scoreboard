package org.tms.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tms.entity.Match;
import org.tms.service.FinishedMatchesPersistenceService;

import java.io.IOException;
import java.util.List;

@MultipartConfig
public class MatchesServlet extends HttpServlet {
    private static final int NUMBER_OF_MATCHES_PER_PAGE = 5;
    private FinishedMatchesPersistenceService persistenceService;

    @Override
    public void init() throws ServletException {
        this.persistenceService = new FinishedMatchesPersistenceService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var filter = req.getParameter("filter_by_player_name");
        int page = paginationToPage(req.getParameter("page"));

        persistUri(filter, req);

        if(filter == null)
            displayAllMatches(req, resp, page);
        else
            displayMatchesByFilter(filter, req, resp, page);
    }

    private void displayAllMatches(HttpServletRequest req, HttpServletResponse resp, int page) throws ServletException, IOException {
        var matches = persistenceService.findAllFinishedMatches();
        var paginatedMatches = getPaginate(matches, page);

        int lastPage = Math.ceilDiv(matches.size(), NUMBER_OF_MATCHES_PER_PAGE);

        req.setAttribute("page", page);
        req.setAttribute("matches", paginatedMatches);
        req.setAttribute("lastPage", lastPage);
        req.getRequestDispatcher("/views/persistMatches.jsp").forward(req, resp);
    }

    private void displayMatchesByFilter(String filter, HttpServletRequest req, HttpServletResponse resp, int page) throws ServletException, IOException {
        var matches = persistenceService.findMatchByPlayer(filter);
        var paginatedMatches = getPaginate(matches, page);

        int lastPage = Math.ceilDiv(matches.size(), NUMBER_OF_MATCHES_PER_PAGE);

        req.setAttribute("page", page);
        req.setAttribute("player", filter);
        req.setAttribute("matches", paginatedMatches);
        req.setAttribute("lastPage", lastPage);
        req.getRequestDispatcher("/views/persistMatches.jsp").forward(req, resp);
    }

    private void persistUri(String filter, HttpServletRequest req){
        StringBuilder builder = new StringBuilder("/matches");

        if(filter != null)
            builder.append("?filter_by_player_name=").append(filter).append("&");
        else
            builder.append("?");

        builder.append("page=");

        req.setAttribute("uri", builder.toString());
    }

    private int paginationToPage(String pagination){
        int page = pagination == null ? 1 : Integer.parseInt(pagination);
        return Math.max(page, 1);
    }

    private List<Match> getPaginate(List<Match> source, int page){
        int left = NUMBER_OF_MATCHES_PER_PAGE * (page - 1);
        int right = NUMBER_OF_MATCHES_PER_PAGE * page;

        if(source.size() < right)
            right = source.size();

        return source.subList(left, right);
    }
}
