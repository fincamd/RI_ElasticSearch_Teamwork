# Para poder usar la función print e imprimir sin saltos de línea
from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit

from elasticsearch import Elasticsearch
from elasticsearch.client import IndicesClient

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()
indicesClient = IndicesClient(searchEngine)


def main():
    # domains = searchEngine.search(index="reddit-mentalhealth", q="domain")

    # domains = searchEngine.search(
    #     index="reddit-mentalhealth",
    #     body={
    #         "query": {"match": {"selftext": {"query": "depression"}}},
    #         "_source": [
    #             "category",
    #             "distinguished",
    #             "downs",
    #             "ups",
    #             "guilded",
    #             "saved",
    #             "subreddit_id",
    #             "subreddit_subs",
    #             "title",
    #             "domain",
    #             "subreddit",
    #             "selftext",
    #         ],
    #         "size": 10,
    #         "track_total_hits": True,
    #     },
    # )

    domains = searchEngine.search(
        index="reddit-mentalhealth",
        body={
            "size": 0,
            "query": {"match": {"selftext": "depression"}},
            "aggs": {"mostCommonTerms": {"terms": {"field": "selftext", "size": 20}}},
        },
    )

    # domains = searchEngine.search(
    #     index="reddit-mentalhealth",
    #     body={
    #         "settings" : {
    #             "analysis" : {
    #                 "analyzer" : {
    #                     "my_stopper" : {
    #                         "type" : "stop",
    #                         "stopwords" : "_english_"
    #                     }
    #                 }
    #             }
    #         }
    #     }
    # )

    # indicesClient.put_settings(
    #     body={
    #         "settings": {
    #             "analysis": {
    #                 "analyzer": {"my_stopper": {"type": "stop", "stopwords": "english"}}
    #             }
    #         }
    #     },
    #     index="reddit-mentalhealth",
    # )

    ppPrinter.pprint(domains)
    print(str(domains) + 'resultados para la query q="diagnosed"')


if __name__ == "__main__":
    main()
