# Para poder usar la función print e imprimir sin saltos de línea
from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit
import datetime

from elasticsearch import Elasticsearch

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()

relevantTerms = []
postsId = []
postsToDump = {}


def main():
    results = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 20,
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
        request_timeout=30
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
                            # Ad-Hoc solution
                            if (postsToDump.get(post["_source"]["id"]) == None):
                                postsToDump[post["_source"]["id"]] = post

    postsSortedByScore = {}
    for key in postsToDump:
        postsSortedByScore[key] = postsToDump[key]["_score"]

    postsSortedByScore = sorted(postsSortedByScore.items(
    ), key=lambda kv: (kv[1], kv[0]), reverse=True)

    lines = []
    for id, score in postsSortedByScore:
        for post in results["hits"]["hits"]:
            if str(post["_id"]) == str(id):
                postData = {}
                postData["author"] = post["_source"]["author"]
                ts_epoch = post["_source"]["created_utc"]
                timestamp = datetime.datetime.fromtimestamp(ts_epoch).strftime('%Y-%m-%d %H:%M:%S')
                postData["creation_date"] = timestamp
                postData["selftext"] = post["_source"]["selftext"]
                lines.append(json.dumps(postData))
                print(postData)

    with open("relevantPosts.json", "wt") as dumpFile:
        for key, value in postsSortedByScore:
            print(key)


if __name__ == "__main__":
    main()
