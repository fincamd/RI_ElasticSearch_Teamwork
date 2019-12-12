# Para poder usar la función print e imprimir sin saltos de línea
from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit

from elasticsearch import Elasticsearch
from elasticsearch.client import IndicesClient

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()

relevantTerms = []
postsId = []
postsToDump = {}

def main():
    results = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 0,
            "query": {
                "query_string": {"default_field": "selftext", "query": "alcoholism"}
            },
            "aggs": {
                "por_palabbres": {
                    "significant_text": {
                        "field": "selftext",
                        "size": 40,
                        "jlh": {},
                    },
                },
            },
        },
    )

    keys = results["aggregations"]["por_palabbres"]["buckets"]

    for key in keys:
        term = key["key"].replace("_", "").strip()
        if relevantTerms.count(term) == 0:
            relevantTerms.append(term)
            print(term)

    terms1 = list(relevantTerms)
    terms2 = list(relevantTerms)
    terms3 = list(relevantTerms)

    for term1 in terms1:
        for term2 in terms2:
            if (term1 != term2):
                for term3 in terms3:
                    if (term3 != term2 and term3 != term1):
                        termPairResults = searchEngine.search(
                            index="reddit-mentalhealth4", 
                            body={
                                "size": 10,
                                "query": {
                                    "query_string": {"default_field": "selftext", "query": term1 + " AND " + term2 + " AND " + term3}
                                },
                                "_source": ["id", "created", "selftext", "author"]
                            }
                        )
                        termPairResults = termPairResults["hits"]["hits"]
                        for post in termPairResults:
                            if (postsToDump.get(post["_source"]["id"]) == None): # Ad-Hoc solution
                                postsToDump[post["_source"]["id"]] = post

    postsSortedByScore = {}
    for key in postsToDump:
        postsSortedByScore[key] = postsToDump[key]["_score"]

    postsSortedByScore = sorted(postsSortedByScore.items(), key = lambda kv:(kv[1], kv[0]), reverse=True)

    with open("relevantPosts.json", "wt") as dumpFile:
        print(postsSortedByScore)
        print(postsSortedByScore[0])
        for key, value in postsSortedByScore:
            print(key)
            # postToSave = postsToDump[key]
            # dumpFile.write(postToSave)
            # dumpFile.write("\n")

if __name__ == "__main__":
    main()
