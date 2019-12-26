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
                        "size": 100,
                        "chi_square": {},
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

    # ppPrinter.pprint(results)

    rows = list(relevantTerms)
    columns = list(relevantTerms)

    for row in rows:
        for column in columns:
            

if __name__ == "__main__":
    main()
