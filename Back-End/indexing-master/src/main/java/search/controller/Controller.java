package search.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;
import search.Hit;
import search.Query_Hit;
import search.Search;

@RestController
public class Controller {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {

        return name;
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public Query_Hit get(@RequestParam Map<String, String> customQuery) {
        boolean usePageRank = true;

        String query = customQuery.get("q");
        String applyPR = customQuery.get("applyPR");
        String expansion_method = customQuery.get("exp");
//        String currentpage = customQuery.get("currentpage");

        if( applyPR.equals("false")) {
            usePageRank = false;
        }

        System.out.println("customQuery = q " + query + customQuery.containsKey("q"));
        System.out.println("customQuery = applyPR " + applyPR + customQuery.containsKey("applyPR"));
        System.out.println("customQuery = exp " + expansion_method + customQuery.containsKey("exp"));
//        System.out.println("customQuery = currentpage " + currentpage + customQuery.containsKey("currentpage"));

        try{
            Search search = new Search("index");
            Query_Hit query_hit = search.queryIndex(query, usePageRank, expansion_method);
            search.close();

            return query_hit;
        }
        catch(IOException e){
            List<Hit> resultHits = new ArrayList<>();
            return new Query_Hit("", resultHits);
        }
    }
}
