package org.example.devsync1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.Tag;
import org.example.devsync1.services.TagService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@WebServlet(name = "TagServlet", urlPatterns = {"/tags"})
public class TagServlet extends HttpServlet {
    TagService tagService;

    @Override
    public void init() throws ServletException {
        tagService = new TagService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deleteTag(request, response);
        } else {
            List<Tag> tags = tagService.findAll();
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/WEB-INF/views/tag/tags.jsp").forward(request, response);
        }
    }
    private void deleteTag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long tagId = Long.parseLong(request.getParameter("id"));
        Tag tag = tagService.findById(tagId);
        tagService.delete(tag);
        response.sendRedirect(request.getContextPath() + "/tags");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createTag(request);
        } else if ("update".equals(action)) {
            updateTag(request);
        }
        response.sendRedirect(request.getContextPath() + "/tags");
    }

    private void createTag(HttpServletRequest request) {
        String tagName = request.getParameter("name");
        Tag tag = new Tag(tagName);
        tagService.save(tag);
    }

    private void updateTag(HttpServletRequest request) {
        Long tagId = Long.parseLong(request.getParameter("id"));
        String tagName = request.getParameter("name");
        Tag tag = tagService.findById(tagId);
        tag.setName(tagName);
        tagService.update(tag);
    }
}
