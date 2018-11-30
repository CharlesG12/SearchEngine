package search;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import search.Cluster_Structure;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

public class MetricCluster {
    IndexSearcher searcher;
    Analyzer analyzer;
    Query query;
    ScoreDoc[] hits;
    Vector<String> vocab;

    public MetricCluster(IndexSearcher searcher,Analyzer analyzer)
    {
        this.analyzer = analyzer;
        this.searcher =searcher;
        this.vocab = new Vector<String>();
    }

    public Query local_cluster(Query query,ScoreDoc[] hits) throws IOException {
        Vector<Document> local_docs = new Vector<Document>();
        for (int i = 0; i < 10; i++) {
            local_docs.add(searcher.doc(hits[i].doc));
        }
        Vector<QueryDoc> DocVector = convert_to_doc_vector(local_docs);
        Document doc = new Document();
        String queryString = query.toString();
        String[] split_string = queryString.split("contents:");
        String final_query_string = String.join("",split_string);
        doc.add(new TextField("contents",final_query_string, Field.Store.YES));
        Vector<Document> querydocvec = new Vector<Document>();
        querydocvec.add(doc);
        Vector<QueryDoc> queryVector = convert_to_doc_vector(querydocvec);
        Query expandedQuery = get_expanded_query(queryVector.elementAt(0),DocVector);
        return expandedQuery;
    }
    public Vector<QueryDoc> convert_to_doc_vector(Vector<Document> local_docs) throws IOException
    {
        Vector<QueryDoc> queryDocs = new Vector<QueryDoc>();
        for(int i=0;i<local_docs.size();i++)
        {
            Document doc = local_docs.elementAt(i);
            QueryDoc qd = new QueryDoc();
            qd.stems = get_stems_from_document(doc);
            queryDocs.add(qd);
        }
        return queryDocs;
    }

    public HashMap<String,Integer> get_stems_from_document(Document doc) throws IOException {
        QueryParser parser = new QueryParser("contents", analyzer);
        HashMap<String,Integer> stems = new HashMap<String,Integer>();
        String queryString = doc.get("contents");
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(queryString));
        if (stream != null) {
            stream.reset();
            OffsetAttribute offsetAttribute = stream.getAttribute(OffsetAttribute.class);
            CharTermAttribute termAttribute = stream.getAttribute(CharTermAttribute.class);
            List<String> terms = new ArrayList<String>();
            boolean hasMoreTokens = false;
            try {
                while (stream.incrementToken()) {
                    int startOffset = offsetAttribute.startOffset();
                    int endOffset = offsetAttribute.endOffset();
                    terms.add(termAttribute.toString());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            stream.reset();
            Stemmer stemmer = new Stemmer();
            int position=0;
            for(int i=0;i<terms.size();i++)
            {
                stemmer.add(terms.get(i).toCharArray(),terms.get(i).length());
                stemmer.stem();
                if(stems.containsKey(terms.get(i)))
                {
                    //do nothing, taking the smallest possible position
                }
                else {
                    if(!this.vocab.contains(terms.get(i)))
                    {
                        this.vocab.add(terms.get(i));
                    }
                    stems.put(terms.get(i),++position);
                }
            }
            return stems;
        }
        else {
            throw new IOException();
        }

    }



    public Query get_expanded_query(QueryDoc queryDoc, Vector<QueryDoc> docVector)throws IOException {

        HashMap<String, Cluster_Structure> mapped_cluster_structure = new HashMap<String, Cluster_Structure>();
        HashMap<String, Cluster_Structure> mapped_cluster_structure_norm = new HashMap<String, Cluster_Structure>();

        for (int j = 0; j < docVector.size(); j++) {

            Iterator it = queryDoc.stems.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                for (int l = 0; l < vocab.size(); l++) {
                    if (docVector.elementAt(j).stems.containsKey(this.vocab.elementAt(l)) && docVector.elementAt(j).stems.containsKey(pair.getKey())) {
                        float distance = Math.abs((docVector.elementAt(j).stems.get(pair.getKey()) - docVector.elementAt(j).stems.get(this.vocab.elementAt(l))));
                        if (!mapped_cluster_structure.containsKey(((String) pair.getKey()) + "," + this.vocab.elementAt(l))&&!mapped_cluster_structure.containsKey(this.vocab.elementAt(l)+","+((String) pair.getKey()))) {
                            Cluster_Structure cs = new Cluster_Structure();
                            cs.query_term = (String) pair.getKey();
                            cs.co_term = this.vocab.elementAt(l);
                            if(distance!=0)
                            {
                                cs.value = 1/distance;
                            }
                            else cs.value = 0;
                            mapped_cluster_structure.put((pair.getKey()) + "," + this.vocab.elementAt(l), cs);
                        }
                        else {
                            Cluster_Structure cs = mapped_cluster_structure.get((pair.getKey()) + "," + this.vocab.elementAt(l));
                            if(cs!=null)
                            {
                                if(distance!=0)
                                {
                                    cs.value += 1/distance;
                                }
                            }
                            else {
                                Cluster_Structure cs1 = mapped_cluster_structure.get(this.vocab.elementAt(l)+","+(pair.getKey()));
                                if(cs1!=null)
                                {
                                    if(distance!=0)
                                    {
                                        cs1.value += 1/distance;
                                    }
                                }

                            }

                        }
                    }
                }
            }

        }

        for(int j=0;j<docVector.size();j++) {
            Iterator it = queryDoc.stems.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry pair = (Map.Entry)it.next();
                for(int l =0;l<vocab.size();l++)
                {
                    if(docVector.elementAt(j).stems.containsKey(this.vocab.elementAt(l))&&docVector.elementAt(j).stems.containsKey(pair.getKey()))
                    {
                        if(mapped_cluster_structure.containsKey(pair.getKey()+","+this.vocab.elementAt(l)))
                        {
                            float cluster_sum = get_count((String)pair.getKey(),docVector.elementAt(j))+get_count(this.vocab.elementAt(l),docVector.elementAt(j));
                            float normalized_value = mapped_cluster_structure.get(pair.getKey()+","+this.vocab.elementAt(l)).value/cluster_sum;
                            if(!mapped_cluster_structure_norm.containsKey(((String)pair.getKey())+","+this.vocab.elementAt(l)))
                            {
                                Cluster_Structure cs = new Cluster_Structure();
                                cs.query_term = (String)pair.getKey();
                                cs.co_term =this.vocab.elementAt(l);
                                cs.value = normalized_value;
                                mapped_cluster_structure_norm.put((pair.getKey())+","+this.vocab.elementAt(l),cs);
                            }

                        }

                    }
                }

            }
        }

        Comparator<Map.Entry<String,Cluster_Structure>> Cluster_Struct_Comparator = new Comparator<Map.Entry<String, Cluster_Structure>>() {
            @Override
            public int compare(Map.Entry<String, Cluster_Structure> o1, Map.Entry<String, Cluster_Structure> o2) {
                Cluster_Structure obj1 = o1.getValue();
                Cluster_Structure obj2 = o2.getValue();
                if(obj1.value>obj2.value)
                    return -1;
                else if (obj2.value>obj1.value)
                    return 1;
                else
                    return 0;
            }
        };


        LinkedHashMap<String,Cluster_Structure> sorted_cluster_structure_map_norm;
        sorted_cluster_structure_map_norm=mapped_cluster_structure_norm.entrySet().stream().sorted(Cluster_Struct_Comparator).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));;

        Iterator it = queryDoc.stems.entrySet().iterator();
        Vector<String> expanded_terms= new Vector<String>();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            int count =0;
            Iterator ti = sorted_cluster_structure_map_norm.entrySet().iterator();
            while (ti.hasNext())
            {
                Map.Entry cs_pair = (Map.Entry)ti.next();
                if((!(cs_pair.getKey().equals(pair.getKey()+","+pair.getKey())))&&((String)cs_pair.getKey()).split(",")[0].equals(pair.getKey()))
                {
                    if(count<2)
                    {
                        count++;
                        String pair_string =(String)cs_pair.getKey();
                        String[] values=pair_string.split(",");
                        if(!expanded_terms.contains(values[1])) {
                            expanded_terms.add(values[1]);
                        }
                        else {
                            count--;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        it = queryDoc.stems.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if(!expanded_terms.contains((String)pair.getKey()))
            {
                expanded_terms.add((String)pair.getKey());
            }
        }
        StringBuilder sb =new StringBuilder();
        for(int i=0;i<expanded_terms.size();i++)
        {
            sb.append(expanded_terms.elementAt(i)+" ");
        }

        Query expandedQuery=null;
        try {
            expandedQuery = new QueryParser("contents", analyzer ).parse(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return expandedQuery;
    }
    
    public int get_count(String key,QueryDoc doc)
    {
        int freq = 0;
        for (Map.Entry<String,Integer> entry:
             doc.stems.entrySet()) {
            if(key.equals(entry.getKey()))
            {
                freq++;
            }
        }
        return freq;
    }
}

