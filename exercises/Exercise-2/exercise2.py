# Pseudo-code for related terms search emulation

"""
    We could start by searching for the n most relevant documents on alcoholism.

    And then feed each one to a query to ElasticSearch such as this one:

    where {postToFeedSelfText} would be the selftext content of each post taken from
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

    syntax taken from the link provided in the examples

    This could be generated using some python script using string composition and
    python f"" formatting
"""
