package org.felipeagger.ecommerce.service;

public class ElasticQuery {

    public static String getQuery(String filter){
        String jsonQry = 
        "{                                            "+
        "    \"query\": {                             "+
        "      \"bool\": {                            "+
        "        \"should\": [                        "+
        "          {                                  "+
        "            \"match\": {                     "+
        "              \"title\": \"%s\"              "+
        "            }                                "+
        "          },                                 "+ 
        "          {                                  "+
        "            \"match\": {                     "+
        "              \"description\": \"%s\"        "+
        "            }                                "+
        "          }                                  "+
        "        ]                                    "+
        "      }                                      "+
        "    }                                        "+
        "  }                                          "; 

        return String.format(jsonQry, filter, filter);
    }

    public static String getQueryById(String filter){
        String jsonQry = 
        "{                                            "+
        "    \"query\": {                             "+
        "      \"match\": {                           "+
        "          \"id.keyword\": \"%s\"             "+
        "        }                                    "+
        "     }                                       "+
        " }                                           "; 

        return String.format(jsonQry, filter);
    }
    

}