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
            "size": 0,
            "query": {
                "query_string": {"default_field": "selftext", "query": "prescribed"}
            },
            "aggs": {
                "words": {
                    "significant_text": {
                        "field": "selftext",
                        "size": 40,
                        # "include": ".*x.*"
                        "include": "[A-Z][A-Za-z]*"
                    },
                }
            }
        },
        request_timeout=30
    )

    ppPrinter.pprint(results)

    with open("medicationFound.json", "wt") as dumpFile:
        pass

if __name__ == "__main__":
    main()
