package com.example.demo.constant;

import java.util.*;

/**
 * @author Artem Kovalov on 21.05.2023
 */
public enum PermittedURLPath {

    LOGIN("/demo/loginServlet"),
    LOGOUT("/demo/logoutServlet"),
    DELETE("/demo/deleteServlet"),
//    SAVE("/demo/saveServlet"),
    VIEW("/demo/viewServlet"),
    VIEW_ID("/demo/viewByIDServlet"),
    PUT("/demo/putServlet");

    private final String path;

    PermittedURLPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
    public static List<String> getAllPaths() {
        List<PermittedURLPath> pathList = List.of(PermittedURLPath.values());
        return pathList.stream().map(PermittedURLPath::getPath).toList();
    }
}
