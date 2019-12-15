from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit
import requests
import random

from elasticsearch import Elasticsearch

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()

op1 = "suicide OR sucidal OR kill myself OR killing myself OR end my life"
op2 = "self harm"

def main():
    program(op2)

def program(op):
    results = searchEngine.search(
    index="reddit-mentalhealth4",
    body={
        "size": 0,
        "query": { "match": { "selftext": op} },
        "aggs": {
            "authors": { 
                "significant_terms": { 
                    "field": "author",
                    "size": 100,
                },
            },
        }
    },
    request_timeout=30
    )

    results = results["aggregations"]["authors"]["buckets"]
    
    for result in results:      
        one_user = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 10,
            "query":{
                "bool":{
                    "must": {
                        "match" : {"selftext":op}
                            
                    },
                    "filter":{
                        "term": { "author": str(result['key'])}  
                    }
                }
            },
            "_source": ["selftext","author","title","subreddit"]
            
        },
        request_timeout=30
        )    
        print(result['key'])
        posts = one_user["hits"]["hits"]
        
        for post in posts:
            print("--->",post["_source"]["title"],post["_source"]["subreddit"])    
    
        subs = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 0,
            "query": {
                "bool":{
                    "must": {
                         "term": { "author": str(result['key'])}                   
                }
                }
            },
            "aggs": {
                "subs": { 
                    "terms": { 
                        "field": "subreddit",
                        "size": 200
                    },
                },
            }
        },
        request_timeout=30
        )
            
        lines = subs["aggregations"]["subs"]["buckets"]
        print("--subs--")
        for line in lines:
            print(line['key'])
        print("---------------------------")


if __name__ == "__main__":
    main()