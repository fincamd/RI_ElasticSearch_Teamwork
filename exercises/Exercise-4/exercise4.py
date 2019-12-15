from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit
import requests
import random
import numpy as np
import matplotlib.pyplot as plotter 

from elasticsearch import Elasticsearch

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()

op1 = "suicide OR suicidal OR kill myself OR killing myself OR end my life"
op2 = "self harm"


def main():
    program(op1, "suicide")
    program(op2, "self_harm")


def program(op, fileName):
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

    # Collect the different authors for the retrieved posts
    authors = []
    for result in results:
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
        subredditToNPosts[line["key"]] = int(line["doc_count"])
        totalNumberOfPosts += int(line["doc_count"])
    dumpLines = []
    print("---------------------------")
    print("Overall statistics: ")
    print("\tTotal number of documents found:", totalNumberOfPosts)
    print("\tSubreddit to doc_count data:")
    dumpLines.append("---------------------------")
    dumpLines.append("Overall statistics:")
    dumpLines.append("\tTotal number of documents found: %d" %
                     totalNumberOfPosts)
    dumpLines.append("\tSubreddit to doc_count data:")
    for key, value in subredditToNPosts.items():
        dumpLine = "\t\tSubreddit: %25s" % (key) + " || Percentage of total count: %3.3f%s" % (
            int(value) / totalNumberOfPosts * 100, "%")
        print(dumpLine)
        dumpLines.append(dumpLine)

    # Plot the statistics into a graph
    labels = []
    values = []
    for key, value in subredditToNPosts.items():
        labels.append(str(key))
        values.append(int(value))
    plotter.barh(labels, values)
    plotter.ylabel("Subreddit names")
    plotter.xlabel("# documents per subreddit")
    plotter.show()

    with open(f"statistics_{fileName}.txt", "w") as dumpFile:
        for line in dumpLines:
            dumpFile.write(line)
            dumpFile.write("\n")


if __name__ == "__main__":
    main()
