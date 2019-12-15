## Pseudo-code for related terms search emulation

"""
    We could start by searching for the 10 most relevant documents on alcoholism.

    And then feed each one to a query to ElasticSearch such as this one:

    where {postToFeedSelfText} would be the selftext content of each post get from
    the previous request

    body = {
        "query": {
            "more_like_this" : {
                "fields" : ["selftext"],
                "like" : {postToFeedSelfText},
                "min_term_freq" : 1,
                "max_query_terms" : 12
            }
        }
    }
"""