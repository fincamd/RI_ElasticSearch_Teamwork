# Para poder usar la función print e imprimir sin saltos de línea
from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit

from elasticsearch import Elasticsearch

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()

def main():
    results = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 10,
            "query": {
                "query_string": {"default_field": "selftext", "query": "medication OR medicine"}
            },
            "aggs": {
                "por_palabbres2": {
                    "significant_text": {
                        "field": "selftext",
                        "size": 20,
                        "gnd": {}
                    },
                },
            },
        }
    )

    keys = results["aggregations"]["por_palabbres2"]["buckets"]

    for key in keys:
        term = key["key"].replace("_", "").strip()
        print(term)

    with open("medicationFound.json", "wt") as dumpFile:
        pass

if __name__ == "__main__":
    main()
