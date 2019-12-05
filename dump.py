from __future__ import print_function

import json
import sys
import pprint

from elasticsearch import Elasticsearch
from elasticsearch import helpers

es = Elasticsearch()

results = helpers.scan(es,
    index="reddit-mentalhealth",
    query= {
        "query": {
            "match": {
                "selftext": "the"
            }
        }
    }
)

with open("scan_dump.txt", "wb") as dumpFile:
    for hit in results:
        dumpFile.write(hit["_source"]["selftext"].encode("UTF-8"))