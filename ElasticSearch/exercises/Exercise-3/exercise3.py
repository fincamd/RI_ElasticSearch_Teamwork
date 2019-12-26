# Para poder usar la función print e imprimir sin saltos de línea
from __future__ import print_function

import json  # Para poder trabajar con objetos JSON
import pprint  # Para poder hacer uso de PrettyPrinter
import sys  # Para poder usar exit
import requests
import random

from elasticsearch import Elasticsearch

_WIKIDATA_INDEX_URL = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=%SEARCH%&language=en&format=json"
_WIKIDATA_DICTIONARY_URL = "https://www.wikidata.org/wiki/Special:EntityData/%ITEM_ID%.json"

ppPrinter = pprint.PrettyPrinter()
searchEngine = Elasticsearch()

debugLog = []


def isMedicine(word):
    # Get the medicine json from wikidata
    articleIndexJson = requests.get(
        _WIKIDATA_INDEX_URL.replace("%SEARCH%", word)).json()
    articleIndexJson = articleIndexJson["search"]
    # Take the Q from the results json
    for item in articleIndexJson:
        itemId = item["title"]
        # Query wikidata again to get json using Q param
        wikidataItemsIndex = requests.get(
            _WIKIDATA_DICTIONARY_URL.replace("%ITEM_ID%", itemId)).json()
        wikidataItemsIndex = wikidataItemsIndex["entities"][itemId]["claims"]
        # Take P31, look for Q for medication/medicine and for pharmaceutical product
        if wikidataItemsIndex.get("P31") != None:
            instanceOfProperty = wikidataItemsIndex["P31"]
            for superclass in instanceOfProperty:
                resultId = superclass["mainsnak"]["datavalue"]["value"]["id"]
                if resultId == "Q12140": # Medication class' id
                    return True
    return False


def main():
    results = searchEngine.search(
        index="reddit-mentalhealth4",
        body={
            "size": 0,
            "query": {"match": {"selftext": "prescribe"}},
            "aggs": {
                "medicines": {
                    "significant_terms": {
                        "field": "selftext",
                        # "exclude": ".*(prescri|doctor|med|take|dose|psychiatrist|[0-9]+|mg).*",
                        "size": 100,
                    },
                },
            }
        },
        request_timeout=30
    )

    results = results["aggregations"]["medicines"]["buckets"]

    singleTerms = []
    for result in results:
        result = result["key"].replace("_", "").strip()
        if singleTerms.count(result) == 0:
            singleTerms.append(result)

    actualMedicines = 0
    totalItems = 0

    for term in singleTerms:
        totalItems += 1
        if isMedicine(term):
            actualMedicines += 1
        print("/-------------------------------------------------------------/")
        print("Term just processed -->", term)
        print("Terms ratio --> Total terms:", totalItems,
              "|| Actual medicines:", actualMedicines)
        print("The percentage of medicines from extracted terms is: %.4f%s" %
              (actualMedicines / totalItems * 100, "%"))
        debugLog.append(
            "/-------------------------------------------------------------/\n")
        debugLog.append("Term just processed --> " + str(term) + "\n")
        debugLog.append("Terms ratio --> Total terms: " + str(totalItems) +
                        " || Actual medicines: " + str(actualMedicines) + "\n")
        formattedPercentageLog = ("The percentage of medicines from extracted terms is: %.4f%s" % (
            actualMedicines / totalItems * 100, "%\n"))
        debugLog.append(formattedPercentageLog)

    with open("medicationFound_" + str(random.randint(0, sys.maxsize)) + ".txt", "w") as dumpFile:
        dumpFile.writelines(debugLog)


if __name__ == "__main__":
    main()
