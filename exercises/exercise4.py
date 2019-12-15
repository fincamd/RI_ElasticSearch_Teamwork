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
    program(op1)


def program(op):
    results = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 0,
            "query": {
                "match": {"selftext": op},
            },
            "aggs": {
                "authors": {
                    "significant_terms": {
                        "field": "author",
                        "size": 1000,
                    },
                },
            },
            "_source": ["selftext", "author", "title", "subreddit"]
        },
        request_timeout=30
    )

    results = results["aggregations"]["authors"]["buckets"]

    authors = []

    for result in results:
        one_user = searchEngine.search(
            index="reddit-mentalhealth4",
            body={
                "size": 10,
                "query": {
                    "bool": {
                        "must": {
                            "match": {"selftext": op}
                        },
                        "filter": {
                            "term": {"author": str(result['key'])}
                        },
                    }
                },
                "_source": ["selftext", "author", "title", "subreddit"]
            },
            request_timeout=30
        )
        # print(result['key'])
        posts = one_user["hits"]["hits"]

        # for post in posts:
        #     print("--->", post["_source"]["title"],
        #           post["_source"]["subreddit"])
        authors.append(result["key"])

    subs = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 0,
            "query": {
                "bool": {
                    "must": {
                        "terms": {"author": authors}
                    }
                }
            },
            "aggs": {
                "subs": {
                    "terms": {
                        "field": "subreddit",
                        "size": 1000
                    },
                },
            }
        },
        request_timeout=30
    )

    subredditToNPosts = {}
    totalNumberOfPosts = 0

    lines = subs["aggregations"]["subs"]["buckets"]
    print("--subs--")
    for line in lines:
        # print(line['key'], line["doc_count"])
        subredditToNPosts[line["key"]] = int(line["doc_count"])
        totalNumberOfPosts += int(line["doc_count"])
    print("---------------------------")
    print("Overall statistics: ")
    print("\tTotal number of documents found:", totalNumberOfPosts)
    print("\tSubreddit to doc_count data:")
    for key, value in subredditToNPosts.items():
        print("\t\tSubreddit: %25s" %(key), "|| Percentage of total count: %3.3f%s" %(int(value) / totalNumberOfPosts * 100, "%"))


if __name__ == "__main__":
    main()
